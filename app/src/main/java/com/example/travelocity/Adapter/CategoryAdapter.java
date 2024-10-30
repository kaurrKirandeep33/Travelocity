package com.example.travelocity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelocity.Domains.Category;
import com.example.travelocity.R;

import java.util.ArrayList;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public CategoryAdapter(ArrayList<Category> categoryModels) {
        this.categoryModels = categoryModels;
    }

    ArrayList<Category> categoryModels;

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_categories,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.titletxt.setText(categoryModels.get(position).getTitle());
        int  drawableResourceId = holder.itemView.getResources().getIdentifier(categoryModels.get(position).getPicPath(),
                "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.picImg);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }
    public class ViewHolder  extends  RecyclerView.ViewHolder{
        TextView titletxt;
        ImageView picImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.titleTxt);
            picImg = itemView.findViewById(R.id.catImage);
        }
    }
}
