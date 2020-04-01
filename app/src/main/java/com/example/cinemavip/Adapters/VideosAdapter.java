package com.example.cinemavip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.Models.Video;
import com.example.cinemavip.R;
import com.example.cinemavip.Utils.rvItemClickListenner;

import java.util.ArrayList;

public class VideosAdapter  extends RecyclerView.Adapter<VideosAdapter.ItemViewHolder> {


    private ArrayList<Video> videoItems;

    private rvItemClickListenner clickListenerPlay;
    private rvItemClickListenner clickListenerStream;
    private rvItemClickListenner clickListennerDownl;

    public VideosAdapter(ArrayList<Video> data){
        this.videoItems = data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_video, parent, false);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        holder.serverName.setText(videoItems.get(position).getServer());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerPlay.onItemClick(position, v);
            }
        });
        holder.trasnmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerStream.onItemClick(position, v);
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListennerDownl.onItemClick(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public void setOnItemClickListener(rvItemClickListenner clickListenerP, rvItemClickListenner clickListenerS, rvItemClickListenner clickListenerD) {
        this.clickListenerPlay = clickListenerP;
        this.clickListenerStream = clickListenerS;
        this.clickListennerDownl = clickListenerD;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView play, trasnmit, download;
        TextView serverName;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            play = itemView.findViewById(R.id.playVideo);
            trasnmit = itemView.findViewById(R.id.transmitVideo);
            download = itemView.findViewById(R.id.downloadVideo);
            serverName = itemView.findViewById(R.id.serverItem);
        }
    }
}
