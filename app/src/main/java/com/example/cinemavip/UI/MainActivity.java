package com.example.cinemavip.UI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cinemavip.API.Callbacks.Movies.onGetMoviesCallback;
import com.example.cinemavip.API.Callbacks.Series.onGetSeriesCallback;
import com.example.cinemavip.API.Providers.GenrerRepository;
import com.example.cinemavip.API.Providers.MoviesRepository;
import com.example.cinemavip.API.Providers.SeriesRepository;
import com.example.cinemavip.Adapters.SliderAdapter;
import com.example.cinemavip.Adapters.UnifiedMediaAdapter;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Account.AccountActivity;
import com.example.cinemavip.UI.Details.MovieDetailsActivity;
import com.example.cinemavip.UI.Details.SerieDetailsActivity;
import com.example.cinemavip.UI.Fragments.HomeFragment;
import com.example.cinemavip.UI.Fragments.MoviesFragment;
import com.example.cinemavip.UI.Fragments.SearchFragment;
import com.example.cinemavip.UI.Fragments.SeriesFragment;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    private MoviesRepository moviesRepository;
    private SeriesRepository seriesRepository;
    private GenrerRepository genrerRepository;


    private ArrayList<Movie> Sliders = new ArrayList<>();
    private ArrayList<Object> moviesPopular = new ArrayList<>();
    private ArrayList<Object> moviesRecent = new ArrayList<>();
    private ArrayList<Object> seriesPopular = new ArrayList<>();
    private ArrayList<Object> seriesRecent = new ArrayList<>();

    private SliderAdapter sliderAdapter;
    private UnifiedMediaAdapter moviesPopularAdapter;
    private UnifiedMediaAdapter moviesRecentAdapter;
    private UnifiedMediaAdapter seriesPopularAdapter;
    private UnifiedMediaAdapter seriesRecentAdapter;
    private UnifiedMediaAdapter[] adapters;
    private UnifiedMediaAdapter moviesFragmentAdapter;
    private UnifiedMediaAdapter seriesFragmentAdapter;

    // The number of native ads to load and display.
    public static final int NUMBER_OF_ADS = 5;

    // The AdLoader used to load ads.
    private AdLoader adLoader;

    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Transition fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        getWindow().setEnterTransition(fadeIn);

        hideSystemUI();

        setContentView(R.layout.activity_main);

        genrerRepository = GenrerRepository.getInstance();
        setMoviesRepository();
        setSeriesRepository();
        setUpAdapters();
        loadNativeAds();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainNav = findViewById(R.id.mainNavigation);
        fragmentManager = getSupportFragmentManager();

        loadFragment(new HomeFragment(adapters, sliderAdapter, moviesPopular, moviesRecent, seriesPopular, seriesRecent), "Inicio", fragmentManager);
        mainNav.setSelectedItemId(R.id.navHome);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (!menuItem.isChecked()){
                    switch (menuItem.getItemId()){
                        case R.id.navHome:
                            loadFragment(new HomeFragment(adapters, sliderAdapter, moviesPopular, moviesRecent, seriesPopular, seriesRecent), "Home", fragmentManager);
                            break;
                        case R.id.navPerfil:
                            Intent accountActiv = new Intent(getApplicationContext(), AccountActivity.class);
                            accountActiv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(accountActiv);
                            break;
                        case  R.id.navMovies:
                            loadFragment(new MoviesFragment(moviesRepository, genrerRepository), "Peliculas", fragmentManager);
                            break;
                        case  R.id.navSeries:
                            loadFragment(new SeriesFragment(seriesRepository), "Series", fragmentManager);
                            break;
                        case  R.id.navCategorias:
                            loadFragment(new SearchFragment(), "Transmisió Semanal", fragmentManager);
                            break;
                    }
                }
                return true;
            }
        });
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        mainNav.setSelectedItemId(R.id.navHome);
    }

    private void setMoviesRepository(){
        moviesRepository = MoviesRepository.getInstance();
        moviesRepository.getMoviesPopular(new onGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                Log.i("MOVIES -> ", movies.toString());
                moviesPopular.addAll(movies);
                moviesPopularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
        moviesRepository.getMoviesRecent(new onGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                Log.i("MOVIES -> ", movies.toString());
                moviesRecent.addAll(movies);
                moviesRecentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
        moviesRepository.getSlider(new onGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                Sliders.addAll(movies);
                sliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSeriesRepository(){
        seriesRepository = SeriesRepository.getInstance();

        seriesRepository.getSeriesPopular(new onGetSeriesCallback() {
            @Override
            public void onSuccess(List<Serie> series) {
                Log.i("Series -> ", series.toString());
                seriesPopular.addAll(series);
                seriesPopularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });

        seriesRepository.getSeriesRecent(new onGetSeriesCallback() {
            @Override
            public void onSuccess(List<Serie> series) {
                Log.i("Series -> ", series.toString());
                seriesRecent.addAll(series);
                seriesRecentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpAdapters(){
        moviesPopularAdapter = new UnifiedMediaAdapter(moviesPopular, null);
        moviesRecentAdapter = new UnifiedMediaAdapter(moviesRecent, null);
        seriesRecentAdapter = new UnifiedMediaAdapter(null, seriesRecent);
        seriesPopularAdapter = new UnifiedMediaAdapter(null, seriesPopular);
        sliderAdapter = new SliderAdapter(this, Sliders);
        moviesFragmentAdapter = new UnifiedMediaAdapter(moviesRecent, null);
        seriesFragmentAdapter = new UnifiedMediaAdapter(null, seriesRecent);
        adapters = new UnifiedMediaAdapter[4];


        seriesFragmentAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent serieDetail = new Intent(getApplicationContext(), SerieDetailsActivity.class);
                serieDetail.putExtra("serieSelected", (Serie)seriesPopular.get(position));
                startActivity(serieDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        moviesFragmentAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent moviesDetail = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                moviesDetail.putExtra("movieSelected", (Movie)moviesPopular.get(position));
                startActivity(moviesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        moviesPopularAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent moviesDetail = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                moviesDetail.putExtra("movieSelected", (Movie)moviesPopular.get(position));
                startActivity(moviesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        moviesRecentAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent moviesDetail = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                moviesDetail.putExtra("movieSelected", (Movie)moviesRecent.get(position));
                startActivity(moviesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        seriesPopularAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent seriesDetail = new Intent(getApplicationContext(), SerieDetailsActivity.class);
                seriesDetail.putExtra("serieSelected", (Serie)seriesPopular.get(position));
                startActivity(seriesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        seriesRecentAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent seriesDetail = new Intent(getApplicationContext(), SerieDetailsActivity.class);
                seriesDetail.putExtra("serieSelected", (Serie)seriesRecent.get(position));
                startActivity(seriesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        adapters[0] = moviesPopularAdapter;
        adapters[1] = moviesRecentAdapter;
        adapters[2] = seriesPopularAdapter;
        adapters[3] = seriesRecentAdapter;
    }

    private void loadFragment(Fragment f1, String name, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, f1, name);
        ft.commit();
        setToolbarTitle(name);
    }

    private void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar, menu);
        return true;
    }

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        int offset = (moviesPopular.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad: mNativeAds) {
            moviesPopular.add(index, ad);
            index = index + offset;
        }
    }

    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        // Load the Native Express ad.
        adLoader.loadAds(new AdRequest.Builder().addTestDevice("19710009EFF315F6A8E81BC719DBA6E0").build(), NUMBER_OF_ADS);
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
}
