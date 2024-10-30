package com.example.travelocity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.travelocity.Activities.tour_details;
import com.example.travelocity.Domains.Tour;
import com.example.travelocity.R;

import java.util.ArrayList;

public class DestinationHolder extends RecyclerView.Adapter<DestinationHolder.viewHolder> {
    ArrayList<Tour> tourModel;
    Context context;

    public DestinationHolder(ArrayList<Tour> tourModel) {
        this.tourModel = tourModel;
    }

    @NonNull
    @Override
    public DestinationHolder.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_viewholder, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationHolder.viewHolder holder, int position) {
        holder.titleTxt.setText(tourModel.get(position).getTitle());
        holder.locationTxt.setText(tourModel.get(position).getLocation());
        holder.scoreTxt.setText(String.valueOf(tourModel.get(position).getScore()));
        Glide.with(context)
                .load(tourModel.get(position).getPic())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.picTour);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, tour_details.class);
            intent.putExtra("object", tourModel.get(position));
            context.startActivity(intent);
        });
    }

        @Override
    public int getItemCount() {
        return tourModel.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, locationTxt, scoreTxt;
        ImageView picTour;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            picTour = itemView.findViewById(R.id.picImg);
        }
    }
}