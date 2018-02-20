package com.gameswapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.gameswapper.utils.OfferedGameAdapter;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ofir on 16/02/18.
 */

public class GamesOnMarketActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    OfferedGameAdapter offeredGameAdapter;
    TextView textView;
    ImageView shine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_on_market);
        textView = (TextView) findViewById(R.id.gamesOnMarketLabel);
        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout)findViewById(R.id.shimmerContainer);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.offeredGamesRecyclerView);
        shimmerFrameLayout.setRepeatDelay(800);
        shimmerFrameLayout.startShimmerAnimation();

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
