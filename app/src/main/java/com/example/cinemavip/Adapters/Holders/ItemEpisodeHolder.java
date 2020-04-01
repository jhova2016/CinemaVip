package com.example.cinemavip.Adapters.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.cinemavip.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ItemEpisodeHolder extends ChildViewHolder {
    public ImageView episodeImage;
    public TextView episodeNumber;
    public TextView episodeName;
    public CardView episodioContainer;
    public LinearLayout ars;
    public ItemEpisodeHolder(@NonNull View itemView) {
        super(itemView);
        episodeImage = itemView.findViewById(R.id.cardItemImage);
        episodeNumber = itemView.findViewById(R.id.cardItemTitle);
        episodeName = itemView.findViewById(R.id.cardItemSubTitle);
        episodioContainer = itemView.findViewById(R.id.cardEpisode);
        ars = itemView.findViewById(R.id.serieARS);

    }
}
