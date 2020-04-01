package com.example.cinemavip.UI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.Adapters.UnifiedMediaAdapter;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Details.MovieDetailsActivity;
import com.example.cinemavip.UI.Details.SerieDetailsActivity;
import com.example.cinemavip.Utils.rvItemClickListenner;

import java.util.ArrayList;

public class SeeAllActivity extends AppCompatActivity {

    private RecyclerView rvMedia;
    private UnifiedMediaAdapter adapterMedia;
    private ArrayList<Object> contentMovies;
    private ArrayList<Object> contentSeries;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        hideSystemUI();
        setContentView(R.layout.activity_see_all);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rvMedia = findViewById(R.id.rvMediaContent);
        rvMedia.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvMedia.setHasFixedSize(true);

        Intent i = getIntent();
        if (i.getStringExtra("type").equals("movie")){
            contentMovies = (ArrayList<Object>) i.getSerializableExtra("mediaContent");
            adapterMedia = new UnifiedMediaAdapter(contentMovies, null);
            adapterMedia.setOnItemClickListener(new rvItemClickListenner() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent moviesDetail = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                    moviesDetail.putExtra("movieSelected", (Movie)contentMovies.get(position));
                    startActivity(moviesDetail);
                }

                @Override
                public void onItemLongClick(int position, View v) {

                }
            });
            rvMedia.setAdapter(adapterMedia);
        }else{
            contentSeries = (ArrayList<Object>) i.getSerializableExtra("mediaContent");
            adapterMedia = new UnifiedMediaAdapter(null, contentSeries);
            adapterMedia.setOnItemClickListener(new rvItemClickListenner() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent seriesDetail = new Intent(getApplicationContext(), SerieDetailsActivity.class);
                    seriesDetail.putExtra("serieSelected", (Serie)contentSeries.get(position));
                    startActivity(seriesDetail);
                }

                @Override
                public void onItemLongClick(int position, View v) {

                }
            });
            rvMedia.setAdapter(adapterMedia);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar, menu);
        return true;
    }
}
