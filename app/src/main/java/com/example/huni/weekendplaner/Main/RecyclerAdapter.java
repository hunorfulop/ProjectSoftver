package com.example.huni.weekendplaner.Main;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huni.weekendplaner.Details.DetailsActivity;
import com.example.huni.weekendplaner.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] titles = {"Concert",
            "Concert",
            "Concert",
            "Concert",
            "Concert",
            "Concert",
            "Concert",
            "Concert"};

    private String[] details = {"14 August 2019",
            "14 August 2019", "14 August 2019",
            "14 August 2019", "14 August 2019",
            "14 August 2019", "14 August 2019",
            "14 August 2019"};

    private int[] images = {  R.mipmap.ic_launcher_background_metallica,
            R.mipmap.ic_launcher_background_metallica,
            R.mipmap.ic_launcher_background_metallica,
            R.mipmap.ic_launcher_background_metallica,
            R.mipmap.ic_launcher_background_metallica,
            R.mipmap.ic_launcher_background_metallica,
            R.mipmap.ic_launcher_background_metallica,
            R.mipmap.ic_launcher_background_metallica };

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =
                    (TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Intent intent = new Intent (v.getContext(), DetailsActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemImage.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
