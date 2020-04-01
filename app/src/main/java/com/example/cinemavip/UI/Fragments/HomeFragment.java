package com.example.cinemavip.UI.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.cinemavip.API.Callbacks.Movies.onGetMovieCallback;
import com.example.cinemavip.API.Callbacks.Series.onGetSerieCallback;
import com.example.cinemavip.API.Providers.MoviesRepository;
import com.example.cinemavip.API.Providers.SeriesRepository;
import com.example.cinemavip.Adapters.SliderAdapter;
import com.example.cinemavip.Adapters.UnifiedMediaAdapter;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Details.MovieDetailsActivity;
import com.example.cinemavip.UI.Details.SerieDetailsActivity;
import com.example.cinemavip.UI.SeeAllActivity;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private ViewPager viewPager;
    private int currentPage = 0;

    private RecyclerView rvPeliculasPolulare, rvPeliculasrecientes, rvSeriesPopulares, rvSeriesRecientes, rvPeliculasFav, rvSeriesFav;
    private CardView verTodoPP, verTodoPR, verTodoSP, verTodoSR, verTodoPF, verTodoSF;

    private SliderAdapter sliderAdapter;
    private UnifiedMediaAdapter moviesPopularAdapter;
    private UnifiedMediaAdapter moviesRecentAdapter;
    private UnifiedMediaAdapter seriesPopularAdapter;
    private UnifiedMediaAdapter seriesRecentAdapter;
    private UnifiedMediaAdapter moviesFavAdapter;
    private UnifiedMediaAdapter seriesFavAdapter;

    ArrayList<Object> PP = new ArrayList<>();
    ArrayList<Object> PR = new ArrayList<>();
    ArrayList<Object> SP = new ArrayList<>();
    ArrayList<Object> SR = new ArrayList<>();
    ArrayList<Object> PF = new ArrayList<>();
    ArrayList<Object> SF = new ArrayList<>();


    private Toolbar toolbar;

    private MoviesRepository moviesRepository;
    private SeriesRepository seriesRepository;

    private SharedPreferences favMoviesPrefs;
    private SharedPreferences RGPDSing;
    private ArrayList<Integer> favMoviesIds = new ArrayList<>();
    private SharedPreferences favSeriesPrefs;
    private ArrayList<Integer> favSeriesIds = new ArrayList<>();

    RelativeLayout rlMovFav, rlSerFav;
    private AdView mAdView0, mAdView1, mAdView2;

    private AlertDialog RGPDAlert;

    public HomeFragment() {
        // Required empty public constructor

    }

    public HomeFragment(UnifiedMediaAdapter[] adapters, SliderAdapter adapter, ArrayList<Object> pPopular, ArrayList<Object> pReciente, ArrayList<Object> sPopular, ArrayList<Object> sReciente) {
        // Required empty public constructor
        this.sliderAdapter = adapter;
        this.moviesPopularAdapter = adapters[0];
        this.moviesRecentAdapter = adapters[1];
        this.seriesPopularAdapter = adapters[2];
        this.seriesRecentAdapter = adapters[3];

        this.moviesFavAdapter = new UnifiedMediaAdapter(PF, null);
        moviesFavAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent moviesDetail = new Intent(getContext(), MovieDetailsActivity.class);
                moviesDetail.putExtra("movieSelected", (Movie)PF.get(position));
                moviesDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                moviesDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moviesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        this.seriesFavAdapter = new UnifiedMediaAdapter(null, SF);
        seriesFavAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent seriesDetail = new Intent(getContext(), SerieDetailsActivity.class);
                seriesDetail.putExtra("serieSelected", (Serie)SF.get(position));
                seriesDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                seriesDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(seriesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        moviesRepository = MoviesRepository.getInstance();
        seriesRepository = SeriesRepository.getInstance();

        this.PP = pPopular;
        this.PR = pReciente;
        this.SP = sPopular;
        this.SR = sReciente;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        RGPDSing = getContext().getSharedPreferences("rgpdSing", Context.MODE_PRIVATE);

        AlertDialog.Builder RGPD = new AlertDialog.Builder(getContext());
        RGPD.setTitle("GDPR");
        RGPD.setMessage(getResources().getString(R.string.RGPD_Policy));
        RGPD.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = RGPDSing.edit();
                editor.putBoolean("isSingned", true);
                editor.apply();
            }
        });
        RGPD.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RGPDAlert.dismiss();
            }
        });
        RGPD.setCancelable(false);
        RGPDAlert = RGPD.create();

        if (!RGPDSing.getBoolean("isSingned", false)){
            RGPDAlert.show();
        }

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mAdView0 = view.findViewById(R.id.adView0);
        mAdView1 = view.findViewById(R.id.adView1);
        mAdView2 = view.findViewById(R.id.adView2);

        AdRequest adRequest0 = new AdRequest.Builder()
                .addTestDevice("19710009EFF315F6A8E81BC719DBA6E0")
                .build();

        mAdView0.loadAd(adRequest0);
        mAdView1.loadAd(adRequest0);
        mAdView2.loadAd(adRequest0);

        rlMovFav = view.findViewById(R.id.rlPeliFav);
        rlSerFav = view.findViewById(R.id.rlSeriesFav);

        viewPager = view.findViewById(R.id.slidePager);

        viewPager.setAdapter(sliderAdapter);
        viewPager.setCurrentItem(currentPage);

        verTodoPP = view.findViewById(R.id.seeAllPP);
        verTodoPR = view.findViewById(R.id.seeAllPR);
        verTodoSP = view.findViewById(R.id.seeAllSP);
        verTodoSR = view.findViewById(R.id.seeAllSR);
        verTodoPF = view.findViewById(R.id.seeAllPM);
        verTodoSF = view.findViewById(R.id.seeAllSF);

        verTodoSF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedia = new Intent(getContext(), SeeAllActivity.class);
                viewMedia.putExtra("type", "serie");
                viewMedia.putExtra("mediaContent", SF);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedia);
            }
        });

        verTodoPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedia = new Intent(getContext(), SeeAllActivity.class);
                viewMedia.putExtra("type", "movie");
                viewMedia.putExtra("mediaContent", PF);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedia);
            }
        });

        verTodoPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedia = new Intent(getContext(), SeeAllActivity.class);
                viewMedia.putExtra("type", "movie");
                viewMedia.putExtra("mediaContent", PP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedia);
            }
        });

        verTodoPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedia = new Intent(getContext(), SeeAllActivity.class);
                viewMedia.putExtra("type", "movie");
                viewMedia.putExtra("mediaContent", PR);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedia);
            }
        });

        verTodoSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedia = new Intent(getContext(), SeeAllActivity.class);
                viewMedia.putExtra("type", "serie");
                viewMedia.putExtra("mediaContent", SP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedia);
            }
        });

        verTodoSR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedia = new Intent(getContext(), SeeAllActivity.class);
                viewMedia.putExtra("type", "serie");
                viewMedia.putExtra("mediaContent", SR);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedia);
            }
        });

        rvPeliculasPolulare = view.findViewById(R.id.rvPeliculasPopulares);
        rvPeliculasrecientes = view.findViewById(R.id.rvPeliculasRecientes);
        rvSeriesPopulares = view.findViewById(R.id.rvSeriesPopulares);
        rvSeriesRecientes = view.findViewById(R.id.rvSeriesReciente);
        rvPeliculasFav = view.findViewById(R.id.rvmoviesFav);
        rvSeriesFav = view.findViewById(R.id.rvSeriesFav);

        rvPeliculasPolulare.setHasFixedSize(true);
        rvPeliculasrecientes.setHasFixedSize(true);
        rvSeriesPopulares.setHasFixedSize(true);
        rvSeriesRecientes.setHasFixedSize(true);
        rvPeliculasFav.setHasFixedSize(true);
        rvSeriesFav.setHasFixedSize(true);

        rvPeliculasPolulare.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvPeliculasrecientes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvSeriesPopulares.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvSeriesRecientes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvPeliculasFav.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvSeriesFav.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        rvPeliculasPolulare.setAdapter(moviesPopularAdapter);
        rvPeliculasrecientes.setAdapter(moviesRecentAdapter);
        rvSeriesPopulares.setAdapter(seriesPopularAdapter);
        rvSeriesRecientes.setAdapter(seriesRecentAdapter);
        rvPeliculasFav.setAdapter(moviesFavAdapter);
        rvSeriesFav.setAdapter(seriesFavAdapter);

        return view;
    }

    private void getFavoritesMovies(){
        //Getting Favorites Movies
        favMoviesPrefs = getContext().getSharedPreferences("favoriteMovies", Context.MODE_PRIVATE);
        favMoviesIds.clear();
        favMoviesIds.addAll((Collection<? extends Integer>) favMoviesPrefs.getAll().values());

        if (favMoviesIds.size() > 0){
            rlMovFav.setVisibility(View.VISIBLE);
            rvPeliculasFav.setVisibility(View.VISIBLE);
            mAdView0.setVisibility(View.VISIBLE);
            PF.clear();
            for (Integer favId : favMoviesIds) {
                moviesRepository.getMovie(favId, new onGetMovieCallback() {
                    @Override
                    public void onSuccess(Movie movie) {
                        Log.i("MOVIES -> ", movie.getTitle());
                        PF.add(movie);
                        moviesFavAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }else {
            rlMovFav.setVisibility(View.GONE);
            rvPeliculasFav.setVisibility(View.GONE);
            mAdView0.setVisibility(View.GONE);
        }
    }

    private void getFavoritesSeries(){
        //Getting Favorites Movies
        favSeriesPrefs = getContext().getSharedPreferences("favoriteSeries", Context.MODE_PRIVATE);
        favSeriesIds.clear();
        favSeriesIds.addAll((Collection<? extends Integer>) favSeriesPrefs.getAll().values());

        if (favSeriesIds.size() > 0){
            rlSerFav.setVisibility(View.VISIBLE);
            rvSeriesFav.setVisibility(View.VISIBLE);
            mAdView0.setVisibility(View.VISIBLE);
            SF.clear();
            for (Integer favId : favSeriesIds) {
                seriesRepository.getSerie(favId, new onGetSerieCallback() {
                    @Override
                    public void onSuccess(Serie serie) {
                        Log.i("MOVIES -> ", serie.getName());
                        SF.add(serie);
                        seriesFavAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }else {
            rlSerFav.setVisibility(View.GONE);
            rvSeriesFav.setVisibility(View.GONE);
            mAdView0.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        getFavoritesMovies();
        getFavoritesSeries();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavoritesMovies();
        getFavoritesSeries();
    }
}
