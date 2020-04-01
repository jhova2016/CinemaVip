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
import com.example.cinemavip.API.Callbacks.Genres.onGetGenreListMovie;
import com.example.cinemavip.API.Callbacks.Movies.onGetMoviePageCallback;
import com.example.cinemavip.API.Providers.GenrerRepository;
import com.example.cinemavip.API.Providers.MoviesRepository;
import com.example.cinemavip.Adapters.UnifiedMediaAdapter;
import com.example.cinemavip.Models.Genre.GenreList;
import com.example.cinemavip.Models.Genre.GenreMovieList;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.R;
import com.example.cinemavip.UI.Details.MovieDetailsActivity;
import com.example.cinemavip.Utils.rvItemClickListenner;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    private RecyclerView rvMovies;
    private UnifiedMediaAdapter mediaAdapter;
    private MoviesRepository moviesRepository;
    private GridLayoutManager GLM;
    private boolean isFetchingMovies;
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
    private ArrayList<Object> movieItems = new ArrayList<>();

    private boolean isSortedByGender = false;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public MoviesFragment(MoviesRepository mRepo, GenrerRepository gRepo) {
        // Required empty public constructor
        this.moviesRepository = mRepo;
        this.genrerRepository = gRepo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        genresAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, generesLabel);

        toolbar = view.findViewById(R.id.toolbarFragmentMovies);
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

        rvMovies = view.findViewById(R.id.rvMovies);
        GLM = new GridLayoutManager(getContext(), 3);
        rvMovies.setLayoutManager(GLM);
        rvMovies.setHasFixedSize(true);
        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = GLM.getItemCount();
                int visibleItemCount = GLM.findLastCompletelyVisibleItemPosition();
                int visibleThreshold = 1;
                if (visibleItemCount + visibleThreshold >= totalItemCount) {
                    if (!isFetchingMovies) {
                        if (!isSortedByGender) {
                            getMovies(currentPage + 1);
                        } else {
                            getGenreMovieList(currentPage + 1);
                        }
                    }
                }
            }
        });

        getMovies(currentPage);
        return view;
    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        moviesRepository.getPaged(page, new onGetMoviePageCallback() {
            @Override
            public void onSuccess(int page, final List<Movie> movies) {
                if (mediaAdapter == null) {
                    movieItems.addAll(movies);
                    mediaAdapter = new UnifiedMediaAdapter(movieItems, null);
                    mediaAdapter.setOnItemClickListener(new rvItemClickListenner() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Intent moviesDetail = new Intent(getContext(), MovieDetailsActivity.class);
                            moviesDetail.putExtra("movieSelected", (Movie)movieItems.get(position));
                            startActivity(moviesDetail);
                        }

                        @Override
                        public void onItemLongClick(int position, View v) {

                        }
                    });
                    rvMovies.setAdapter(mediaAdapter);
                } else {
                    mediaAdapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {

            }

        });
    }

    private void getGenreMovieList(final int page){
        //Toast.makeText(getContext(), "" + page, Toast.LENGTH_SHORT).show();
        isFetchingMovies = true;
        genrerRepository.getMoviesList(genreId, page, new onGetGenreListMovie() {
            @Override
            public void onSuccess(GenreMovieList movieList) {
                Log.i("MOVIES -> ", movieList.toString());
                movieItems.addAll(movieList.getData());
                mediaAdapter.notifyDataSetChanged();
                currentPage = page;
                isFetchingMovies = false;
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
                movieItems.clear();
                currentPage = 1;
                getGenreMovieList(currentPage);
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
        if(genrerRepository != null){
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
        } else {
            genrerRepository = GenrerRepository.getInstance();
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

}
