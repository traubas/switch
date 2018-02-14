package com.gameswapper.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameswapper.R;
import com.gameswapper.models.Game;

import java.util.List;

/**
 * Created by user on 14/02/2018.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    private List<Game> gameList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView gameName, gameDescription;
        public ImageView gameImage;

        public MyViewHolder(View view) {
            super(view);
            gameName = (TextView) view.findViewById(R.id.gameName);
            gameDescription = (TextView) view.findViewById(R.id.gameDescription);
            gameImage = (ImageView) view.findViewById(R.id.gameImage);
        }
    }

    public GameAdapter(List<Game> gameList) {
        this.gameList = gameList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.gameName.setText(game.getName());
        holder.gameDescription.setText(game.getDesc());
        holder.gameImage.setBackgroundResource(game.getImage());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}
