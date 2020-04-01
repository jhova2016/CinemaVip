package com.example.cinemavip.API.Providers;

import android.util.Log;

import com.example.cinemavip.API.Callbacks.Genres.onGetGenreList;
import com.example.cinemavip.API.Callbacks.Genres.onGetGenreListMovie;
import com.example.cinemavip.API.Callbacks.Genres.onGetGenreListSerie;
import com.example.cinemavip.API.CineDarkAPI;
import com.example.cinemavip.Models.Genre.GenreList;
import com.example.cinemavip.Models.Genre.GenreMovieList;
import com.example.cinemavip.Models.Genre.GenreSerieList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenrerRepository {
    private static final String BASE_URL = "https://panel.tvmoviesapp.com/api/";

    private static GenrerRepository repository;

    private CineDarkAPI api;

    private GenrerRepository(CineDarkAPI api) {
        this.api = api;
    }

    public static GenrerRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new GenrerRepository(retrofit.create(CineDarkAPI.class));
        }

        return repository;
    }

    public void getGeneresList(final onGetGenreList callback){
        api.getGenreList()
                .enqueue(new Callback<List<GenreList>>() {
                    @Override
                    public void onResponse(Call<List<GenreList>> call, Response<List<GenreList>> response) {
                        if (response.isSuccessful()) {
                            List<GenreList> genreResponse = response.body();
                            if (genreResponse != null) {
                                callback.onSuccess( genreResponse);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GenreList>> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });
    }

    public void getMoviesList(int id, int page, final onGetGenreListMovie callback){
        api.getGenreMovieList(id, page)
                .enqueue(new Callback<GenreMovieList>() {

                    @Override
                    public void onResponse(Call<GenreMovieList> call, Response<GenreMovieList> response) {
                        if (response.isSuccessful()) {
                            GenreMovieList movieResponse = response.body();
                            if (movieResponse != null) {
                                callback.onSuccess( movieResponse);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreMovieList> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });
    }

    public void getSerieList(int id, int page, final onGetGenreListSerie callback){
        api.getGenreSerieList(id, page)
                .enqueue(new Callback<GenreSerieList>() {

                    @Override
                    public void onResponse(Call<GenreSerieList> call, Response<GenreSerieList> response) {
                        if (response.isSuccessful()) {
                            GenreSerieList movieResponse = response.body();
                            if (movieResponse != null) {
                                callback.onSuccess( movieResponse);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreSerieList> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });
    }
}
