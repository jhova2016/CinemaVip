package com.example.cinemavip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.R;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemViewHolder> implements Serializable {

    private List<Movie> movieItems;
    private List<Serie> serieItems;
    private rvItemClickListenner clickListener;

    public HomeAdapter(List<Movie> movies, List<Serie> series){
        //Log.e("adapterH -> ", movies.toString());

        this.movieItems = movies;
        this.serieItems = series;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
       if(movieItems != null){
           Picasso.get().load(movieItems.get(position).getPosterPath()).fit().centerCrop().into(holder.itemImage);
           holder.itemText.setText(movieItems.get(position).getTitle());
           holder.itemImage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   clickListener.onItemClick(position, v);
               }
           });
       }else {
           Picasso.get().load(serieItems.get(position).getPosterPath()).fit().centerCrop().into(holder.itemImage);
           holder.itemText.setText(serieItems.get(position).getName());
           holder.itemImage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   clickListener.onItemClick(position, v);
               }
           });
       }
    }

    @Override
    public int getItemCount() {
        if (movieItems != null){
            return movieItems.size();
        }else {
            return serieItems.size();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(rvItemClickListenner clickListener) {
        this.clickListener = clickListener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemText;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cardItemImage);
            itemText = itemView.findViewById(R.id.cardItemTitle);
        }
    }
}
