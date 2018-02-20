package com.gameswapper.utils;

import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gameswapper.R;
import com.gameswapper.models.Game;

import java.util.List;

/**
 * Created by user on 14/02/2018.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    private List<Game> gameList;
    final private ListItemClickListener mOnClickListener;
    final int ps4color = R.color.ps4blue;
    final int xboxonecolor = R.color.xboxonegreen;
    final int pccolor = R.color.pcgray;
    MyViewHolder mvh;
    int ActivityNumber; //1 = AddGamesToProfileActivity, 2 = MyGamesActivity

    public GameAdapter(ListItemClickListener listener,int number) {
        mOnClickListener = listener;
        ActivityNumber = number;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView gameName, gameDescription;
        public ImageView gameImage;
        public LinearLayout LL;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            gameName = (TextView) view.findViewById(R.id.gameName);
            gameDescription = (TextView) view.findViewById(R.id.gameDescription);
            gameImage = (ImageView) view.findViewById(R.id.gameImage);
            LL = (LinearLayout) view.findViewById(R.id.consoleColor);
            imageView = (ImageView) view.findViewById(R.id.platformLogo);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public GameAdapter(List<Game> gameList, ListItemClickListener licl, int number) {
        this.gameList = gameList;
        mOnClickListener = licl;

        ActivityNumber = number;
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
        Context context = holder.LL.getContext();
        System.out.println("game is "+game.getPlatform());
        if (ActivityNumber == 2) {
            if (game.getPlatform() != null) {
                switch (game.getPlatform()) {
                    case "PS4":
                        holder.LL.setBackgroundColor(ContextCompat.getColor(context, R.color.ps4blue));
                        holder.imageView.setImageResource(R.drawable.ps4logo);
                        break;
                    case "XBOX ONE":
                        holder.LL.setBackgroundColor(ContextCompat.getColor(context, R.color.xboxonegreen));
                        holder.imageView.setImageResource(R.drawable.xboxlogo);
                        break;
                    case "PC":
                        holder.LL.setBackgroundColor(ContextCompat.getColor(context, R.color.pcgray));
                        holder.imageView.setImageResource(R.drawable.pclogo);
                        break;
                    default:

                }
            }
        }

    }
    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


}
