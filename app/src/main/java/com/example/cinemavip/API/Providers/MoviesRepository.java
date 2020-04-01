package com.example.cinemavip.API.Providers;

import android.util.Log;

import com.example.cinemavip.API.Callbacks.Movies.onGetMovieCallback;
import com.example.cinemavip.API.Callbacks.Movies.onGetMoviePageCallback;
import com.example.cinemavip.API.Callbacks.Movies.onGetMoviesCallback;
import com.example.cinemavip.API.CineDarkAPI;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Peliculas.MoviePage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {
    private static final String BASE_URL = "https://panel.tvmoviesapp.com/api/";

    private static MoviesRepository repository;

    private CineDarkAPI api;

    private MoviesRepository(CineDarkAPI api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(CineDarkAPI.class));
        }

        return repository;
    }

    public void getMoviesPopular(final onGetMoviesCallback callback) {
        api.getMoviesPopular()
                .enqueue(new Callback<List<Movie>>() {
                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        if (response.isSuccessful()) {
                            //Movie moviesResponse = response.body();
                            if (response.body() != null) {
                                callback.onSuccess(response.body());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }

                });
    }

    public void getMoviesRecent(final onGetMoviesCallback callback) {
        api.getMoviesRecents()
                .enqueue(new Callback<List<Movie>>() {
                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        if (response.isSuccessful()) {
                            //Movie moviesResponse = response.body();
                            if (response.body() != null) {
                                callback.onSuccess(response.body());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }

                });
    }

    public void getMoviesRelated(final onGetMoviesCallback callback, int movieId){
        api.getRelatedsMovies(movieId)
                .enqueue(new Callback<List<Movie>>() {
                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        if (response.isSuccessful()) {
                            //Movie moviesResponse = response.body();
                            if (response.body() != null) {
                                callback.onSuccess(response.body());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }

                });
    }

    public void getSlider(final onGetMoviesCallback callback) {
        api.getSlider()
                .enqueue(new Callback<List<Movie>>() {
                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        if (response.isSuccessful()) {
                            //Movie moviesResponse = response.body();
                            if (response.body() != null) {
                                callback.onSuccess(response.body());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }

                });
    }

    public void getPaged(final int page, final onGetMoviePageCallback callback) {
        api.getPageMovie(page)
                .enqueue(new Callback<MoviePage>() {
                    @Override
                    public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                        if (response.isSuccessful()) {
                            MoviePage moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getData() != null) {
                                callback.onSuccess( moviesResponse.getCurrentPage(), moviesResponse.getData());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviePage> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });
    }

    public void getMovie(int id, final onGetMovieCallback callback){
        api.getMovie(id)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movieResponse = response.body();
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
                    public void onFailure(Call<Movie> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });
    }

}
