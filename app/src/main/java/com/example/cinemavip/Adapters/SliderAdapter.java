package com.example.cinemavip.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Details.MovieDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Activity context;
    private ArrayList<Movie> mList;

    public SliderAdapter(Activity context, ArrayList<Movie> items) {
        this.context = context;
        this.mList = items;
        inflater = context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View imageLayout = inflater.inflate(R.layout.slider_item, container, false);
        assert imageLayout != null;
        ImageView imageView = imageLayout.findViewById(R.id.image);
        TextView textTitle = imageLayout.findViewById(R.id.text);
        TextView textSubTitle = imageLayout.findViewById(R.id.textSub);
        CardView rootLayout = imageLayout.findViewById(R.id.cardView);

        textTitle.setSelected(true);

        final Movie itemSlider = mList.get(position);
        Picasso.get().load(itemSlider.getPosterPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(imageView);
        textTitle.setText(itemSlider.getTitle());

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moviesDetail = new Intent(context, MovieDetailsActivity.class);
                moviesDetail.putExtra("movieSelected", itemSlider);
                context.startActivity(moviesDetail);
            }
        });

        container.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }
}