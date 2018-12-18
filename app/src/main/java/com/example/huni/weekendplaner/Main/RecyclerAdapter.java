package com.example.huni.weekendplaner.Main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huni.weekendplaner.Details.DetailsActivity;
import com.example.huni.weekendplaner.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<ListDataEvent> listDataEvents;
    Context context;

    //RecyclerAdapter constructor
    public RecyclerAdapter(List<ListDataEvent> listDataEvents,Context context_) {
        this.listDataEvents = listDataEvents;
        this.context = context_;
    }

    //Viewholder class
    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;


        public ViewHolder(View itemView) {
            super(itemView);
            //Initializing the view elements
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail = (TextView)itemView.findViewById(R.id.item_detail);

            //Setting onClickListeners to the elements
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    //Sending data to the DetailsActiivity about the current event
                    Intent intent = new Intent (v.getContext(), DetailsActivity.class);
                    intent.putExtra("NameofEvent",listDataEvents.get(position).getNameOfEvent());
                    intent.putExtra("DescofEvent",listDataEvents.get(position).getDescriptionOfEvent());
                    intent.putExtra("StartofEvent",listDataEvents.get(position).getStart_date());
                    intent.putExtra("EndofEvent",listDataEvents.get(position).getEnd_date());
                    intent.putExtra("AdresofEvent",listDataEvents.get(position).getAddress());
                    intent.putExtra("AuthorfEvent",listDataEvents.get(position).getAuthor());
                    intent.putExtra("ImageofEvent",listDataEvents.get(position).getImage());
                    intent.putExtra("IdofEvent",listDataEvents.get(position).getId());
                    //Starting DetailsActivity
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Initializing the cards view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    //Setting the data in the cardView elements
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(this.listDataEvents.get(i).getNameOfEvent());
        viewHolder.itemDetail.setText(this.listDataEvents.get(i).getStart_date());
        Glide.with(this.context)
                .load(this.listDataEvents.get(i).getImage())
                .into(viewHolder.itemImage);

    }

    //Getting the list size which contains all the events
    @Override
    public int getItemCount() {
        return this.listDataEvents.size();
    }
}
