package com.example.cinemavip.UI.Details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.Adapters.EpisodeExpadedAdapter;
import com.example.cinemavip.Models.Series.Episodio;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.Models.Series.Temporada;
import com.example.cinemavip.Models.SeriesAltern.EpisodeContainer;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Player.ChoosePlayerActivity;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.appbar.AppBarLayout;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SerieDetailsActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    private Serie serieToShow;

    private ImageView posterSerie, backdropSerie;
    private TextView genreList, launchDate, sinopsis;
    private RatingView ratingview;

    private ArrayList<Temporada> temporadas = new ArrayList<>();
    private ArrayList<String> temporadasName = new ArrayList<>();
    private ImageView imgSeason;
    private ArrayList<Episodio> episodios = new ArrayList<>();
    private Spinner spinnerSeason;
    private RecyclerView rvEpisodios;
    private EpisodeExpadedAdapter episodioAdapter;

    private Button trailer;

    private SharedPreferences favSeries, seenEps;
    private SharedPreferences.Editor favEditor, arsEditor;
    private AdView mAdView0, mAdView1;
    private ImageView supportImg;

    private InterstitialAd mInterstitialAd;
    private int positionSelected;

    private Set<String> allReadySeen = new HashSet<>();
    private int currentSeason = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        hideSystemUI();
        setContentView(R.layout.activity_serie_details);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.intersticialAdsId));
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("19710009EFF315F6A8E81BC719DBA6E0").build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                TastyToast.makeText(getApplicationContext(), "Disfruta tu Episodio", TastyToast.LENGTH_LONG, TastyToast.INFO);
                go2ChooseServer();
            }
        });

        supportImg = findViewById(R.id.supportLink);
        supportImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://darkiptv.com/apoyo/publi.php")));
            }
        });

        mAdView0 = findViewById(R.id.adView0);
        mAdView1 = findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView0.loadAd(adRequest);
        mAdView1.loadAd(adRequest);

        favSeries = getApplicationContext().getSharedPreferences("favoriteSeries", Context.MODE_PRIVATE);
        favEditor = favSeries.edit();

        seenEps = getApplicationContext().getSharedPreferences("serieARS", Context.MODE_PRIVATE);
        arsEditor = seenEps.edit();

        Intent intent = getIntent();
        serieToShow = (Serie) intent.getSerializableExtra("serieSelected");

        allReadySeen = seenEps.getStringSet(serieToShow.getName(), null);

        if(allReadySeen != null){
            Log.e("EPISODIOS VISTOS -> ", allReadySeen.toString());
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
            toolbar.setSubtitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navAdd2Fav) {
                    if (favSeries.getInt(serieToShow.getName(), -1) != -1){
                        favEditor.remove(serieToShow.getName());
                        favEditor.apply();
                        Toast.makeText(getApplicationContext(), "Eliminada de favoritos", Toast.LENGTH_SHORT).show();
                        item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    }else{
                        favEditor.putInt(serieToShow.getName(), serieToShow.getId());
                        favEditor.apply();
                        Toast.makeText(getApplicationContext(), "Añadida de favoritos", Toast.LENGTH_SHORT).show();
                        item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    }
                    return true;
                }
                return true;
            }
        });

        posterSerie = findViewById(R.id.imgPosterSerie);
        backdropSerie = findViewById(R.id.imgBackdrop);
        genreList = findViewById(R.id.genreList);
        launchDate = findViewById(R.id.txtLanzamientoDate);
        sinopsis = findViewById(R.id.txtSinopsis);
        ratingview = findViewById(R.id.rating);
        appBarLayout = findViewById(R.id.appBarMoviesDetail);
        spinnerSeason = findViewById(R.id.spinerSeason);
        imgSeason = findViewById(R.id.imgSeasonPoster);
        rvEpisodios = findViewById(R.id.rvEposidios);
        trailer = findViewById(R.id.btnTrailer);

        if (serieToShow != null){
            Picasso.get().load(serieToShow.getPosterPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(posterSerie);
            Picasso.get().load(serieToShow.getBackdropPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(backdropSerie);
            genreList.setText(serieToShow.getGenreslist().toString());
            launchDate.setText(serieToShow.getFirstAirDate());
            sinopsis.setText(serieToShow.getOverview());
            setToolbarTitle(serieToShow.getName());
            ratingview.setRating(serieToShow.getVoteAverage()/2);
            temporadas.addAll(serieToShow.getSeasons());
            Picasso.get().load(temporadas.get(0).getPosterPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(imgSeason);
            episodios.addAll(temporadas.get(0).getEpisodes());
        }

        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serieToShow.getPreviewPath() != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(serieToShow.getPreviewPath())));
                } else {
                    trailer.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Trailer no disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpEpisodioAdapter();

        rvEpisodios.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        rvEpisodios.setAdapter(episodioAdapter);

        for (int i = 0; i< temporadas.size(); i++){
            temporadasName.add(temporadas.get(i).getName());
        }

        spinnerSeason.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, temporadasName));
        spinnerSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                currentSeason = pos;
                Picasso.get().load(temporadas.get(pos).getPosterPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(imgSeason);
                episodios.clear();
                episodios.addAll(temporadas.get(pos).getEpisodes());
                setUpEpisodioAdapter();
                rvEpisodios.setAdapter(episodioAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.darkTech));
                }else{
                    toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.transparent));
                }
            }
        });
    }

    private void setUpEpisodioAdapter(){

        ArrayList<EpisodeContainer> groupedEpisodes = new ArrayList<>();
        ArrayList<Episodio>aux_episodes = new ArrayList<>();
        int firstEpisodeOfGroup = 1;
        for (int i=1; i<=episodios.size(); i++){
            aux_episodes.add(episodios.get(i-1));
            if(i%9 == 0){
                groupedEpisodes.add(new EpisodeContainer(firstEpisodeOfGroup + " - " + i, aux_episodes));
                aux_episodes = new ArrayList<>();
                firstEpisodeOfGroup = i+1;
            }
        }
        groupedEpisodes.add(new EpisodeContainer(firstEpisodeOfGroup + " - " + episodios.size(), aux_episodes));

        episodioAdapter = new EpisodeExpadedAdapter(groupedEpisodes, allReadySeen);
        if (!episodioAdapter.isGroupExpanded(groupedEpisodes.get(0))){
            episodioAdapter.toggleGroup(groupedEpisodes.get(0));
        }
        episodioAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {

                positionSelected = position;

                if(allReadySeen == null){
                    allReadySeen = new HashSet<>();
                    allReadySeen.add(String.valueOf(episodios.get(position).getId()));
                    arsEditor.putStringSet(serieToShow.getName(), allReadySeen);
                    arsEditor.apply();
                }else{

                    if (!allReadySeen.contains(String.valueOf(episodios.get(position).getId()))){
                        allReadySeen.add(String.valueOf(episodios.get(position).getId()));
                        arsEditor.putStringSet(serieToShow.getName(), allReadySeen);
                        arsEditor.apply();
                    }
                }

                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else {
                   go2ChooseServer();
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

    }

    private void go2ChooseServer(){
        Intent chooseServer = new Intent(getApplicationContext(), ChoosePlayerActivity.class);
        chooseServer.putExtra("selectedVideo", (Serializable) episodios.get(positionSelected).getVideos());
        chooseServer.putExtra("backgroundImage", serieToShow.getPosterPath());

        startActivity(chooseServer);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
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

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        if(favSeries != null){
            if(favSeries.getInt(serieToShow.getName(), -1) != -1){
                menu.findItem(R.id.navAdd2Fav).setIcon(R.drawable.ic_favorite_black_24dp);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
