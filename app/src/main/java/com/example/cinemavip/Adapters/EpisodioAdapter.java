package com.example.cinemavip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.Models.Series.Episodio;
import com.example.cinemavip.R;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;

public class EpisodioAdapter extends RecyclerView.Adapter<EpisodioAdapter.EpisodioItemViewHolder> {

    private List<Episodio> episodeItems;
    private rvItemClickListenner clickListener;
    private Set<String> ARS;

    public EpisodioAdapter(List<Episodio> data, Set<String> vistos){
        this.episodeItems = data;
        this.ARS = vistos;
    }

    @NonNull
    @Override
    public EpisodioItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_episodio, parent, false);
        return new EpisodioItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final EpisodioItemViewHolder holder, final int position) {
        Picasso.get().load(episodeItems.get(position).getStillPath()).into(holder.episodeImage);
        holder.episodeNumber.setText("Episodio #" + episodeItems.get(position).getEpisodeNumber());
        holder.episodeName.setText(episodeItems.get(position).getName());
        holder.episodioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(position, v);
                holder.ars.setVisibility(View.VISIBLE);
            }
        });
        if(ARS != null){
            if (ARS.contains(String.valueOf(episodeItems.get(position).getId()))){
                holder.ars.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return episodeItems.size();
    }

    public void setOnItemClickListener(rvItemClickListenner clickListener) {
        this.clickListener = clickListener;
    }

    public class EpisodioItemViewHolder extends RecyclerView.ViewHolder {
        ImageView episodeImage;
        TextView episodeNumber;
        TextView episodeName;
        CardView episodioContainer;
        LinearLayout ars;
        public EpisodioItemViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeImage = itemView.findViewById(R.id.cardItemImage);
            episodeNumber = itemView.findViewById(R.id.cardItemTitle);
            episodeName = itemView.findViewById(R.id.cardItemSubTitle);
            episodioContainer = itemView.findViewById(R.id.cardEpisode);
            ars = itemView.findViewById(R.id.serieARS);

        }
    }
}
