package com.example.cinemavip.UI.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.API.Callbacks.onGetSearchCallback;
import com.example.cinemavip.API.Providers.SearchRepository;
import com.example.cinemavip.Adapters.HomeAdapter;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Search;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Details.MovieDetailsActivity;
import com.example.cinemavip.UI.Details.SerieDetailsActivity;
import com.example.cinemavip.UI.SeeAllActivity;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private SearchRepository searchRepository;

    private RecyclerView rvMovies, rvSeries;
    private CardView verTodoP, verTodoS;

    private HomeAdapter moviesRetrievedAdapter;
    private HomeAdapter seriesRetrievedAdapter;

    private ArrayList<Movie> pRetrieved= new ArrayList<>();
    private ArrayList<Serie> sRetrieved = new ArrayList<>();

    private LinearLayout llMovies, llSeries;



    private androidx.appcompat.widget.SearchView searchView;

    public SearchFragment() {
        // Required empty public constructor
        searchRepository = SearchRepository.getInstance();

        this.moviesRetrievedAdapter = new HomeAdapter(pRetrieved, null);
        moviesRetrievedAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent moviesDetail = new Intent(getContext(), MovieDetailsActivity.class);
                moviesDetail.putExtra("movieSelected", pRetrieved.get(position));
                moviesDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                moviesDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moviesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        this.seriesRetrievedAdapter = new HomeAdapter(null, sRetrieved);
        seriesRetrievedAdapter.setOnItemClickListener(new rvItemClickListenner() {
            @Override
            public void onItemClick(int position, View v) {
                Intent seriesDetail = new Intent(getContext(), SerieDetailsActivity.class);
                seriesDetail.putExtra("serieSelected", sRetrieved.get(position));
                seriesDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                seriesDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(seriesDetail);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);



        AdRequest adRequest0 = new AdRequest.Builder().build();

        AdRequest adRequest1 = new AdRequest.Builder().build();


        llMovies = view.findViewById(R.id.llMoviesContainer);
        llSeries = view.findViewById(R.id.llSeriesContainer);

        verTodoP = view.findViewById(R.id.seeAllP);
        verTodoS = view.findViewById(R.id.seeAllS);

        verTodoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedia = new Intent(getContext(), SeeAllActivity.class);
                viewMedia.putExtra("type", "movie");
                viewMedia.putExtra("mediaContent", pRetrieved);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedia);
            }
        });

        verTodoS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedia = new Intent(getContext(), SeeAllActivity.class);
                viewMedia.putExtra("type", "serie");
                viewMedia.putExtra("mediaContent", sRetrieved);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                viewMedia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedia);
            }
        });

        rvMovies = view.findViewById(R.id.rvMovies);
        rvSeries = view.findViewById(R.id.rvSeries);

        rvMovies.setHasFixedSize(true);
        rvSeries.setHasFixedSize(true);

        rvMovies.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvSeries.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        rvMovies.setAdapter(moviesRetrievedAdapter);
        rvSeries.setAdapter(seriesRetrievedAdapter);

        searchView = view.findViewById(R.id.search);
        searchView.setQueryHint("Titulo");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMediaContent(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    private void searchMediaContent(String query){
        searchRepository.searchMedia(query, new onGetSearchCallback() {
            @Override
            public void onSuccess(Search dataRetrieved) {
                Log.i("MOVIES -> ", dataRetrieved.toString());

                pRetrieved.clear();
                sRetrieved.clear();

                pRetrieved.addAll(dataRetrieved.getMovies());
                sRetrieved.addAll(dataRetrieved.getSeries());

                if(pRetrieved != null && sRetrieved != null){
                    if (!pRetrieved.isEmpty()){
                        llMovies.setVisibility(View.VISIBLE);
                        moviesRetrievedAdapter.notifyDataSetChanged();
                    } else {
                        llMovies.setVisibility(View.GONE);
                    }
                    if (!sRetrieved.isEmpty()){
                        llSeries.setVisibility(View.VISIBLE);
                        seriesRetrievedAdapter.notifyDataSetChanged();
                    }else {
                        llSeries.setVisibility(View.GONE);
                    }
                    if (pRetrieved.isEmpty() && sRetrieved.isEmpty()){
                        TastyToast.makeText(getContext(), "No se han encontrado resultados", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                    }
                } else{
                    TastyToast.makeText(getContext(), "No se han encontrado resultados", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);

                }

            }

            @Override
            public void onError() {

            }
        });
    }

}
