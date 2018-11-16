package com.parcking.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.parcking.R;
import com.parcking.models.parking;

import java.util.List;


public class ParkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<parking> list ;
    private Context context;
    public ParkAdapter(Context context, List<parking> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_park, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        Holder newsHolder = (Holder) holder;
        parking parking = list.get(position);

        newsHolder.title.setText(parking.getName());
        Glide.with(context).load(parking.getImg()).into(newsHolder.img);
         //   String url = parking.getImg();



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView title ;
        ImageView img;
        public Holder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.number_park);
            img = itemView.findViewById(R.id.img);

        }

    }
}






