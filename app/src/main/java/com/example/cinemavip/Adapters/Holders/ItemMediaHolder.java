package com.example.cinemavip.Adapters.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.R;

public class ItemMediaHolder extends RecyclerView.ViewHolder {
    public ImageView itemImage;
    public TextView itemText;
    public ItemMediaHolder(@NonNull View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.cardItemImage);
        itemText = itemView.findViewById(R.id.cardItemTitle);
    }
}