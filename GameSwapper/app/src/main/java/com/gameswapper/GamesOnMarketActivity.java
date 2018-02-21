package com.gameswapper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.gameswapper.models.Game;
import com.gameswapper.models.Offer;
import com.gameswapper.utils.GameAdapter;
import com.gameswapper.utils.OfferedGameAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ofir on 16/02/18.
 */

public class GamesOnMarketActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OfferedGameAdapter.ListItemClickListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    OfferedGameAdapter offeredGameAdapter;
    TextView textView;
    ImageView shine;
    String userId;
    ProgressDialog progressDialog;
    SearchView searchView;
    private List<Offer> offerList = new ArrayList<>();
    private List<Offer> adapterList = new ArrayList<>();
    private OfferedGameAdapter gameAdapter;
    private static final String TAG = "SearchViewFilterMode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_on_market);
        searchView = (SearchView) findViewById(R.id.searchView1);
        textView = (TextView) findViewById(R.id.gamesOnMarketLabel);
        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout)findViewById(R.id.shimmerContainer);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.offeredGamesRecyclerView);
        shimmerFrameLayout.setRepeatDelay(800);
        shimmerFrameLayout.startShimmerAnimation();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else {
            userId = user.getUid();
            setupSearchView();
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            setupSearchView();
            progressDialog = new ProgressDialog(this,
                    R.style.AppTheme_Dark_Dialog);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

    }
    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint(getString(R.string.game_search));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.profileImage:
                Intent intent1 = new Intent(this, MyGamesActivity.class);
                startActivity(intent1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(final String s) {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Offer List ...");
        progressDialog.show();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                offerList.clear();
                String game_name = null;
                String platform = null;
                String user_name = null;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot users : dataSnapshot1.getChildren()) {

                         if (((String) users.getKey()).equals("user_name")) {
                            user_name = (String) users.getValue();
                        }
                    }
                    for (DataSnapshot games : dataSnapshot1.getChildren()) {
                        if (((String) games.getKey()).equals("games")) {
                            for (DataSnapshot ds2 : games.getChildren()) {
                                if (game_name == null)
                                    platform = (String) ds2.getKey();
                                for (DataSnapshot ds3 : ds2.getChildren()) {
                                    if ((ds3.getKey().toLowerCase()).contains(s.toLowerCase()))
                                        game_name = ds3.getKey();
                                    if (game_name != null && platform != null && user_name != null) {
                                        Offer offer = new Offer(user_name, game_name, platform);
                                        offerList.add(offer);
                                        System.out.println("ADDED GAME: "+game_name + " " + user_name + " " + platform);
                                        game_name = null;
                                        platform = null;

                                    }
                                }
                            }
                        }
                        user_name = null;
                    }
                }
                gameAdapter = new OfferedGameAdapter(offerList, GamesOnMarketActivity.this);
                recyclerView.setAdapter(gameAdapter);
                progressDialog.dismiss();
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                progressDialog.dismiss();
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterList.clear();
        for (Offer offer : offerList) {
            if (offer.getGame().toLowerCase().contains(s.toLowerCase())) {
                adapterList.add(offer);

            }
        }
        gameAdapter = new OfferedGameAdapter(adapterList,this);
        recyclerView.setAdapter(gameAdapter);
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
