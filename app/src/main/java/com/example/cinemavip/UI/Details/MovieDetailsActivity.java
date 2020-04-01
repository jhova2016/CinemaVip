package com.example.cinemavip.UI.Details;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.API.Callbacks.Movies.onGetMoviesCallback;
import com.example.cinemavip.API.Providers.MoviesRepository;
import com.example.cinemavip.Adapters.HomeAdapter;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Video;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Player.ChoosePlayerActivity;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    private Movie movieToShow;

    private ImageView posterMovie, backdropMovie;
    private TextView genreList, launchDate, sinopsis;
    private RatingView ratingview;

    private FloatingActionButton fabPlay;

    private ArrayList<Video> videosList = new ArrayList<>();

    private ArrayList<Movie> relatedMovies = new ArrayList<>();
    private MoviesRepository moviesRepository;
    private HomeAdapter moviesRelatedsAdapter;
    private RecyclerView rvRelated;
    private Button trailer;
    private SharedPreferences favMovies;
    private SharedPreferences.Editor editor;
    private AdView mAdView0, mAdView1;
    private InterstitialAd mInterstitialAd;

    private Intent chooseServer;

    private ImageView supportImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        hideSystemUI();
        setContentView(R.layout.activity_movie_details);

        Intent i = getIntent();
        movieToShow = (Movie) i.getSerializableExtra("movieSelected");

        chooseServer = new Intent(getApplicationContext(), ChoosePlayerActivity.class);
        chooseServer.putExtra("selectedVideo", videosList);
        chooseServer.putExtra("backgroundImage", movieToShow.getPosterPath());

        supportImg = findViewById(R.id.supportLink);
        supportImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://darkiptv.com/apoyo/publi.php")));
            }
        });

        posterMovie = findViewById(R.id.imgPosterMovie);
        backdropMovie = findViewById(R.id.imgBackdrop);
        genreList = findViewById(R.id.genreList);
        launchDate = findViewById(R.id.txtLanzamientoDate);
        sinopsis = findViewById(R.id.txtSinopsis);
        ratingview = findViewById(R.id.rating);
        fabPlay = findViewById(R.id.fab);
        rvRelated = findViewById(R.id.rvRelacionadas);
        trailer = findViewById(R.id.btnTrailer);

        mAdView0 = findViewById(R.id.adView0);
        mAdView1 = findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("19710009EFF315F6A8E81BC719DBA6E0").build();
        mAdView0.loadAd(adRequest);
        mAdView1.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.intersticialAdsId));
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("19710009EFF315F6A8E81BC719DBA6E0").build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                TastyToast.makeText(getApplicationContext(), "Enlaces listos", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                tooglePlayVisivility();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                TastyToast.makeText(getApplicationContext(), "Enlaces listos " + "ERR: " + errorCode, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                tooglePlayVisivility();
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
                TastyToast.makeText(getApplicationContext(), "Disfruta tu pelicula", TastyToast.LENGTH_LONG, TastyToast.INFO);
                startActivity(chooseServer);
            }
        });

        TastyToast.makeText(getApplicationContext(), "Obteniendo enlaces", TastyToast.LENGTH_LONG, TastyToast.INFO);


        favMovies = getApplicationContext().getSharedPreferences("favoriteMovies", Context.MODE_PRIVATE);
        editor = favMovies.edit();

        moviesRelatedsAdapter = new HomeAdapter(relatedMovies, null);
        moviesRepository = MoviesRepository.getInstance();
        moviesRepository.getMoviesRelated(new onGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                Log.i("MOVIES -> ", movies.toString());
                relatedMovies.addAll(movies);
                moviesRelatedsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }, movieToShow.getId());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navAdd2Fav) {
                    if (favMovies.getInt(movieToShow.getTitle(), -1) != -1){
                        editor.remove(movieToShow.getTitle());
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Eliminada de favoritos", Toast.LENGTH_SHORT).show();
                        item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    }else{
                        editor.putInt(movieToShow.getTitle(), movieToShow.getId());
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Añadida de favoritos", Toast.LENGTH_SHORT).show();
                        item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    }
                    return true;
                }
                return true;
            }
        });


        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movieToShow.getPreviewPath() != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieToShow.getPreviewPath())));
                } else {
                    trailer.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Trailer no disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        moviesRelatedsAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent moviesDetail = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                moviesDetail.putExtra("movieSelected", relatedMovies.get(position));
                startActivity(moviesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        rvRelated.setHasFixedSize(true);
        rvRelated.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        rvRelated.setAdapter(moviesRelatedsAdapter);


        if (movieToShow != null){
            Picasso.get().load(movieToShow.getPosterPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(posterMovie);
            Picasso.get().load(movieToShow.getBackdropPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(backdropMovie);
            genreList.setText(movieToShow.getGenreslist().toString());
            launchDate.setText(movieToShow.getReleaseDate());
            sinopsis.setText(movieToShow.getOverview());
            setToolbarTitle(movieToShow.getTitle());
            Log.e("RATING -> ", String.valueOf(movieToShow.getPopularity()));
            ratingview.setRating(movieToShow.getVoteAverage()/2);
            videosList.addAll(movieToShow.getVideos());
        }

        appBarLayout = findViewById(R.id.appBarMoviesDetail);

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

        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else {
                    startActivity(chooseServer);
                }
            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void tooglePlayVisivility(){
        if(fabPlay.getVisibility() == View.VISIBLE){
            fabPlay.setVisibility(View.GONE);
        }else{
            fabPlay.setVisibility(View.VISIBLE);
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            fabPlay.startAnimation(pulse);
        }
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
        if(favMovies.getInt(movieToShow.getTitle(), -1) != -1){
            menu.findItem(R.id.navAdd2Fav).setIcon(R.drawable.ic_favorite_black_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
