package com.gameswapper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.util.Log;
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
 * Created by ofir on 15/02/18.
 */

public class AddGamesToProfileActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, GameAdapter.ListItemClickListener {
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;
    SearchView searchView;
    ProgressDialog progressDialog;
    private List<Game> gameList = new ArrayList<>();
    private List<Game> adapterList = new ArrayList<>();
    private GameAdapter gameAdapter;
    private static final String TAG = "SearchViewFilterMode";
    String userId;
    ImageView profileImage;
    boolean gameAddedNow = false;
    RadioButton xboxBtn;
    RadioButton ps4Btn;
    RadioButton pcBtn;
    RadioGroup radioGroup;
    ValueEventListener VEL;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_games_to_profile);
        searchView = (SearchView) findViewById(R.id.searchView);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_profile);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else {
            userId = user.getUid();

            mDatabase = FirebaseDatabase.getInstance().getReference("games");
            setupSearchView();
            progressDialog = new ProgressDialog(this,
                    R.style.AppTheme_Dark_Dialog);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating Games Data :)...");
            progressDialog.show();

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.removeEventListener(VEL);
                    Intent intent = new Intent(AddGamesToProfileActivity.this, MyGamesActivity.class);
                    startActivity(intent);
                }
            });
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    String game_name = null;
                    String game_desc = null;
                    String platform = null;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        game_name = (String) dataSnapshot1.getKey();
                        for (DataSnapshot ds2 : dataSnapshot1.getChildren()) {
                            game_desc = (String) ds2.getValue();
                        }
                        Game game = new Game(game_name, game_desc);
                        game.addImage();
                        gameList.add(game);
                    }
                    gameAdapter = new GameAdapter(gameList, AddGamesToProfileActivity.this);
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
            VEL = postListener;
        }
    }
    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint(getString(R.string.game_search));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println(newText);
        adapterList.clear();
        for (Game game : gameList) {
            if (game.getName().toLowerCase().contains(newText.toLowerCase())) {
                System.out.print(game.getName());
                adapterList.add(game);

            }
        }
        gameAdapter = new GameAdapter(adapterList,this);
        recyclerView.setAdapter(gameAdapter);
        return true;
    }
    @Override
    public void onListItemClick(int clickedItemIndex) {
        int platformId=-1;
        /**
         * hide keyaboard after item was clicekd
         */
        platformId = radioGroup.getCheckedRadioButtonId();
        if (platformId == -1) {
            Toast.makeText(this, "You must choose Platform", Toast.LENGTH_SHORT).show();
        }
        else {
            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            /**
             * create a dialog that apears after the user clicks on the item of choice.
             * instantiate the TextView and ImageView variables so we can set their text and image
             */
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(R.layout.custom_dialog)
                    .create();
            dialog.show();
            TextView text = (TextView) dialog.findViewById(R.id.textDialog);
            ImageView image = (ImageView) dialog.findViewById(R.id.image);
            /**
             * get the game name and description
             */
            View view = recyclerView.getChildAt(clickedItemIndex);

            final String game_name = ((TextView) view.findViewById(R.id.gameName)).getText().toString();
            final String game_desc = ((TextView) view.findViewById(R.id.gameDescription)).getText().toString();
            text.setText(game_name);
            text.setTextColor(Color.BLACK);
            /**
             * set the platform of the game based on the chosen radioButton
             */
            RadioButton radioButton;

            radioButton = (RadioButton) findViewById(platformId);
            final String platform = radioButton.getText().toString();
            /**
             * create a new game based on the game name
             * and add the game image to the dialog image
             */
            Game chosenGame = new Game(game_name);
            chosenGame.addImage();
            image.setImageResource(chosenGame.getImage());

            /**
             * create the doalog buttons and their click listeners
             */
            Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonYes);
            Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
            dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addGameToUser(game_name, game_desc, dialog, platform);
                    progressDialog.setMessage("Adding");
                    progressDialog.show();

                }
            });
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }
        Log.v(TAG, "List item clicked at index: " + clickedItemIndex);

    }

    public void addGameToUser(final String game, final String desc, final Dialog dialog, final String platform) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId).child("games").child(platform).child(game);
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.exists())) {
                    FirebaseDatabase.getInstance().getReference("users").child(userId).child("games").child(platform).child(game).child("description").setValue(desc);
                    Toast.makeText(getBaseContext(),"Game Added",Toast.LENGTH_SHORT).show();
                    mDatabase.removeEventListener(this);
                    progressDialog.dismiss();
                    gameAddedNow=true;
                    dialog.dismiss();
                }
                else {
                    if (gameAddedNow) {
                        gameAddedNow=false;
                    }
                    else
                        Toast.makeText(getBaseContext(), "Game Already exists", Toast.LENGTH_SHORT).show();
                    mDatabase.removeEventListener(this);
                    progressDialog.dismiss();

                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
        VEL = postListener;
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.searchTheMarketBtn:
                intent = new Intent(this,GamesOnMarketActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
