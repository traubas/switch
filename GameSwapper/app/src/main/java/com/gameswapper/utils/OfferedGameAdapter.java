package com.gameswapper.utils;

import android.content.Context;
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
import com.gameswapper.models.Offer;

import java.util.List;

/**
 * Created by ofir on 17/02/18.
 */

public class OfferedGameAdapter extends RecyclerView.Adapter<OfferedGameAdapter.MyViewHolder> {
    private List<Offer> offerList;
    final private ListItemClickListener mOnClickListener;
    final int ps4color = R.color.ps4blue;
    final int xboxonecolor = R.color.xboxonegreen;
    final int pccolor = R.color.pcgray;

    public OfferedGameAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView userOfferingName;
        public TextView gameName;
        public ImageView gameImage;
        public LinearLayout LL;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            userOfferingName = (TextView) view.findViewById(R.id.userOfferingName);
            gameName = (TextView) view.findViewById(R.id.gameNameBeingOffered);
            gameImage = (ImageView) view.findViewById(R.id.offeredGameImage);
            LL = (LinearLayout) view.findViewById(R.id.offeredGameConsole);
            imageView = (ImageView) view.findViewById(R.id.offeredGameConsoleLogo);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
    public OfferedGameAdapter(List<Offer> offerList, ListItemClickListener licl) {
        this.offerList = offerList;
        mOnClickListener = licl;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offered_game_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        Game game = new Game(offer.getGame(), offer.getPlatform());
        System.out.println("looooook "+offer.getPlatform());
        game.addImage();
        holder.gameName.setText(offer.getGame());
        holder.userOfferingName.setText(offer.getName());
        holder.gameImage.setBackgroundResource(game.getImage());
        Context context = holder.LL.getContext();
        switch (offer.getPlatform()) {
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
        }
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
