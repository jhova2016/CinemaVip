package com.example.cinemavip.UI.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.API.Callbacks.Genres.onGetGenreList;
import com.example.cinemavip.API.Callbacks.Genres.onGetGenreListSerie;
import com.example.cinemavip.API.Callbacks.Series.onGetSeriePageCallback;
import com.example.cinemavip.API.Providers.GenrerRepository;
import com.example.cinemavip.API.Providers.SeriesRepository;
import com.example.cinemavip.Adapters.MovieAdapter;
import com.example.cinemavip.Models.Genre.GenreList;
import com.example.cinemavip.Models.Genre.GenreSerieList;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Details.SerieDetailsActivity;
import com.example.cinemavip.Utils.rvItemClickListenner;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesFragment extends Fragment {

    private RecyclerView rvSeries;
    private MovieAdapter seriesAdapter;
    private SeriesRepository seriesRepository;
    private GridLayoutManager GLM;
    private boolean isFetchingSeries;
    private int currentPage = 1;

    private Toolbar toolbar;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Spinner spinnerGenrer;

    private GenrerRepository genrerRepository;
    private ArrayList<GenreList> genres = new ArrayList<>();
    private ArrayList<String> generesLabel = new ArrayList<>();
    private ArrayAdapter genresAdapter;
    private int genreId;
    private ArrayList<Serie> serieItems = new ArrayList<>();

    private boolean isSortedByGender = false;
    private int pageLimit;


    public SeriesFragment() {
        // Required empty public constructor
    }

   public SeriesFragment(SeriesRepository data) {
        // Required empty public constructor
       this.seriesRepository = data;
       this.genrerRepository = GenrerRepository.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_series, container, false);

        genresAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, generesLabel);

        toolbar = view.findViewById(R.id.toolbarFragmentSeries);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.filter:{
                        //Toast.makeText(getContext(), "Filtrado", Toast.LENGTH_SHORT).show();
                        alertDialog.show();
                        break;
                    }
                }
                return true;
            }
        });

        getGeneres();
        setUpFilterDialog(inflater);

        rvSeries = view.findViewById(R.id.rvSeries);
        GLM = new GridLayoutManager(getContext(), 3);

        rvSeries.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvSeries.setHasFixedSize(true);
        rvSeries.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = GLM.getItemCount();
                int visibleItemCount = GLM.findLastCompletelyVisibleItemPosition();
                int visibleThreshold = 1;

                if (!isFetchingSeries) {
                    if (!isSortedByGender) {
                        getSeries(currentPage + 1);
                    } else {
                        getGenreSerieList(currentPage + 1);
                    }
                }
            }
        });
        getSeries(currentPage);
        return view;
    }

    private void getSeries(int page) {
        isFetchingSeries = true;
        seriesRepository.getPaged(page, new onGetSeriePageCallback() {
            @Override
            public void onSuccess(int page, final List<Serie> series) {
                if (seriesAdapter == null) {
                    serieItems.addAll(series);
                    seriesAdapter = new MovieAdapter(null, serieItems);
                    seriesAdapter.setOnItemClickListener(new rvItemClickListenner() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Intent moviesDetail = new Intent(getContext(), SerieDetailsActivity.class);
                            moviesDetail.putExtra("serieSelected", serieItems.get(position));
                            startActivity(moviesDetail);
                        }

                        @Override
                        public void onItemLongClick(int position, View v) {

                        }
                    });
                    rvSeries.setAdapter(seriesAdapter);
                } else {
                    seriesAdapter.appendSeries(series);
                }
                currentPage = page;
                isFetchingSeries = false;
            }

            @Override
            public void onError() {

            }

        });
    }

    private void getGenreSerieList(final int page){
        //Toast.makeText(getContext(), "" + page, Toast.LENGTH_SHORT).show();
        isFetchingSeries = true;
        genrerRepository.getSerieList(genreId, page, new onGetGenreListSerie() {
            @Override
            public void onSuccess(GenreSerieList movieList) {
                Log.i("MOVIES -> ", movieList.toString());
                serieItems.addAll(movieList.getData());
                seriesAdapter.notifyDataSetChanged();
                currentPage = page;
                isFetchingSeries = false;
            }

            @Override
            public void onError() {

            }
        });
    }

    private void setUpFilterDialog(LayoutInflater inflater){
        builder = new AlertDialog.Builder(getContext());

        View filterLayout = inflater.inflate(R.layout.filters_dialog, null);

        spinnerGenrer = filterLayout.findViewById(R.id.spinnerGenre);
        spinnerGenrer.setAdapter(genresAdapter);
        spinnerGenrer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genreId = genres.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterLayout.findViewById(R.id.dialogFilterOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSortedByGender){
                    isSortedByGender = true;
                }
                serieItems.clear();
                currentPage = 1;
                getGenreSerieList(currentPage);
                alertDialog.dismiss();
            }
        });
        filterLayout.findViewById(R.id.dialogFilterCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder.setView(filterLayout);

        //builder.setCancelable(false);
        alertDialog = builder.create();
    }

    private void getGeneres(){
        genrerRepository.getGeneresList(new onGetGenreList() {
            @Override
            public void onSuccess(List<GenreList> genreList) {
                Log.i("MOVIES -> ", genreList.toString());
                genres.addAll(genreList);
                for (int i=0; i<genreList.size(); i++){
                    generesLabel.add(genreList.get(i).getName());
                }
                genresAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }


}
