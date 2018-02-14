package com.gameswapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gameswapper.models.Game;
import com.gameswapper.utils.GameAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<Game> movieList = new ArrayList<>();
    private GameAdapter gameAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        gameAdapter = new GameAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gameAdapter);

        insertGameData();

    }

    public void insertGameData() {
        Game game = new Game("PlayerUnknown's: Battlegrounds","massive online Battle Royal",R.drawable.pubg);
        movieList.add(game);
        game = new Game("Assassin's Creed","The story explores the origins of the centuries-long conflict between the Brotherhood of Assassins, who fight for peace by promoting liberty, and The Order of thbe Ancients",R.drawable.assassinsorigins);
        movieList.add(game);
        game = new Game("Fifa 18","FIFA 18 marks the return of the popular soccer franchise, and will re-introduce fans to Journey mode, among other features .Score incredible goals in FIFA 18 as new movement and finishing animations unlock more fluid striking and heading of the ball.",R.drawable.fifa18);
        movieList.add(game);
    }
}
