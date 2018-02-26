package com.gameswapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gameswapper.models.Game;
import com.gameswapper.utils.GameAdapter;
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
 * Created by ofir on 22/02/18.
 */

public class UserProfileActivity extends AppCompatActivity implements GameAdapter.ListItemClickListener{
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String userId;
    String user_name;
    TextView listOfGames;
    TextView userProfileName;
    TextView userProfileNickName;
    ProgressDialog progressDialog;
    RatingBar ratingBar;
    LinearLayout ratingLinearLayout;
    private List<Game> gameList = new ArrayList<>();
    private GameAdapter gameAdapter;
    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    public static final String TAG = "FETCH_USER_EMAIL_TAG";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(UserProfileActivity.this,
                R.style.AppTheme_Dark_Dialog);
        ratingBar = (RatingBar) findViewById(R.id.rateTheUserBar);
        ratingLinearLayout = (LinearLayout) findViewById(R.id.ratingLinearLayout);
        userProfileName = (TextView) findViewById(R.id.userProfileName);
        userProfileNickName = (TextView) findViewById(R.id.userProfileNickName);
        if (user == null) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else {
        userId = user.getUid();
        listOfGames = (TextView) findViewById(R.id.listOfGamesTitle);
        //addGamesImage = (ImageView) findViewById(R.id.addGamesImage);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = currentUser.getUid();
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Your Games :)...");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(id);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                gameList.clear();
                String game_name = null;
                String game_desc = null;
                String platform = null;
                if (dataSnapshot.hasChild("games")) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equals("games")) {
                            for (DataSnapshot ds1 : ds.getChildren()) {
                                platform = ds1.getKey();
                                System.out.print("platform is " + ds1.getKey());
                                for (DataSnapshot ds2 : ds1.getChildren()) {
                                    game_name = (String) ds2.getKey();
                                    for (DataSnapshot ds3 : ds2.getChildren())
                                        game_desc = (String) ds3.getValue();
                                    Game game = new Game(game_name, game_desc, platform);
                                    game.addImage();
                                    gameList.add(game);
                                }

                            }

                        }
                    }
                }
                DataSnapshot userNameSnapshot = dataSnapshot.child("user_name");
                String name = (String) userNameSnapshot.getValue();
                userProfileName.setText(name);
                userProfileNickName.setText(name);
                listOfGames.setText("List Of Games");
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                gameAdapter = new GameAdapter(gameList, UserProfileActivity.this,2);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
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
        databaseReference.addValueEventListener(postListener);


    }
        /**
         * remove the rating bar if this is my profile (can't rate myself
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            ratingLinearLayout.removeAllViews();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        InputMethodManager inputMethodManager = (InputMethodManager)  getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        // In here, handle what happens when an item is clicked
        // In this case, I am just logging the index of the item clicked
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.custom_dialog)
                .create();
        dialog.show();
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText("Remove Game?");
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        View view = recyclerView.getChildAt(clickedItemIndex);
        final String game_name = ((TextView) view.findViewById(R.id.gameName)).getText().toString();
        final String game_desc = ((TextView) view.findViewById(R.id.gameDescription)).getText().toString();
        LinearLayout LL = (LinearLayout) view.findViewById(R.id.consoleColor);
        ImageView logoImage = (ImageView) view.findViewById(R.id.platformLogo);
        Game chosenGame = new Game(game_name);
        chosenGame.addImage();
        String platform;
        int color = ((ColorDrawable)LL.getBackground()).getColor();
        int ps4 = ((ColorDrawable)(getDrawable(R.color.ps4blue))).getColor();

        int xboxone = ((ColorDrawable)(getDrawable(R.color.xboxonegreen))).getColor();

        int pc = ((ColorDrawable)(getDrawable(R.color.pcgray))).getColor();
        System.out.println("color is: "+color+" compare with "+ps4+" "+xboxone+" "+pc);
        if (color == ps4) {
            platform = "PS4";
        }
        else if (color == xboxone) {
            platform = "XBOX ONE";
        }
        else {
            platform = "PC";
        }
        final String platformName = platform;
        image.setImageResource(chosenGame.getImage());
        text.setText(game_name);
        Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonYes);
        Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
        dialogButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeGameFromUser(game_name,game_desc, dialog,platformName);
                progressDialog.setMessage("Removing");
                progressDialog.show();

            }
        });
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        Log.v(TAG, "List item clicked at index: " + clickedItemIndex);
    }

    public void removeGameFromUser(final String game, final String desc, final Dialog dialog, final String platform) {
        try {
            FirebaseDatabase.getInstance().getReference("users").child(userId).child("games").child(platform).child(game).setValue(null);
            Toast.makeText(getBaseContext(),"Game Removed",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            dialog.dismiss();
        }
        catch (Exception e){
            Toast.makeText(getBaseContext(),"Could not connect to databse",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            dialog.dismiss();
        }

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
                Intent intent1 = new Intent(this, UserProfileActivity.class);
                startActivity(intent1);
                return true;
            case R.id.addGames:
                Intent intent2 = new Intent(this,AddGamesToProfileActivity.class);
                startActivity(intent2);
                return true;
            case R.id.searchTheMarketBtn:
                Intent intent3 = new Intent(this,GamesOnMarketActivity.class);
                startActivity(intent3);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}