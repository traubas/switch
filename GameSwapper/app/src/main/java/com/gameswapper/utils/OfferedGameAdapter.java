package com.gameswapper.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.gameswapper.R;
import com.gameswapper.models.Game;
import com.gameswapper.models.Offer;

import java.util.List;

/**
 * Created by ofir on 17/02/18.
 */

public class OfferedGameAdapter extends RecyclerSwipeAdapter<OfferedGameAdapter.MyViewHolder> {
    private List<Offer> offerList;
    final private ListItemClickListener mOnClickListener;
    final int ps4color = R.color.ps4blue;
    final int xboxonecolor = R.color.xboxonegreen;
    final int pccolor = R.color.pcgray;
    SwipeItemRecyclerMangerImpl mItemManger;

    public OfferedGameAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView userOfferingName;
        public TextView gameName;
        public ImageView gameImage;
        public LinearLayout LL;
        public ImageView imageView;
        public SwipeLayout swipeLayout;
        public Button buttonDelete;


        public MyViewHolder(View view) {
            super(view);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            userOfferingName = (TextView) view.findViewById(R.id.userOfferingName);
            gameName = (TextView) view.findViewById(R.id.gameNameBeingOffered);
            gameImage = (ImageView) view.findViewById(R.id.offeredGameImage);
            LL = (LinearLayout) view.findViewById(R.id.offeredGameConsole);
            imageView = (ImageView) view.findViewById(R.id.offeredGameConsoleLogo);
            buttonDelete = (Button) itemView.findViewById(R.id.delete);
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Offer offer = offerList.get(position);
        Game game = new Game(offer.getGame(), offer.getPlatform());
        System.out.println("looooook "+offer.getPlatform());
        game.addImage();
        holder.gameName.setText(offer.getGame());
        holder.userOfferingName.setText(offer.getName());
        holder.gameImage.setBackgroundResource(game.getImage());
        holder.swipeLayout.getDragEdgeMap().clear();
        /**
         * next line is to change the swipe direction of items. set it from left to right. default right to left.
         */
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.swipeLayout.findViewById(R.id.bottom_wrapper));
        final Context context = holder.LL.getContext();
        mItemManger = new SwipeItemRecyclerMangerImpl(this);
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
        /**
         * handle the swipes
         */
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
            @Override
            public void onStartOpen(SwipeLayout layout) {
                mItemManger.closeAllExcept(layout);
            }
        });
        holder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mItemManger.removeShownLayouts(holder.swipeLayout);
                offerList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, offerList.size());
              //  mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + holder.gameName.getText().toString() + "!", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * end handle swipe
         */
        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
