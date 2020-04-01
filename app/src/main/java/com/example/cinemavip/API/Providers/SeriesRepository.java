package com.example.cinemavip.API.Providers;

import android.util.Log;

import com.example.cinemavip.API.Callbacks.Series.onGetSerieCallback;
import com.example.cinemavip.API.Callbacks.Series.onGetSeriePageCallback;
import com.example.cinemavip.API.Callbacks.Series.onGetSeriesCallback;
import com.example.cinemavip.API.CineDarkAPI;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.Models.Series.SeriePage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeriesRepository {
    private static final String BASE_URL = "https://panel.tvmoviesapp.com/api/";

    private static SeriesRepository repository;

    private CineDarkAPI api;

    private SeriesRepository(CineDarkAPI api) {
        this.api = api;
    }

    public static SeriesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new SeriesRepository(retrofit.create(CineDarkAPI.class));
        }

        return repository;
    }

    public void getSeriesRecent(final onGetSeriesCallback callback) {
        api.getSeriesRecientes()
                .enqueue(new Callback<List<Serie>>() {
                    @Override
                    public void onResponse(Call<List<Serie>> call, Response<List<Serie>> response) {
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
                    public void onFailure(Call<List<Serie>> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }

                });
    }

    public void getSeriesPopular(final onGetSeriesCallback callback) {
        api.getSeriesPopular()
                .enqueue(new Callback<List<Serie>>() {
                    @Override
                    public void onResponse(Call<List<Serie>> call, Response<List<Serie>> response) {
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
                    public void onFailure(Call<List<Serie>> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }

                });
    }

    public void getPaged(final int page, final onGetSeriePageCallback callback) {
        api.getPageSerie(page)
                .enqueue(new Callback<SeriePage>() {
                    @Override
                    public void onResponse(Call<SeriePage> call, Response<SeriePage> response) {
                        if (response.isSuccessful()) {
                            SeriePage moviesResponse = response.body();
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
                    public void onFailure(Call<SeriePage> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });
    }

    public void getSerie(int id, final onGetSerieCallback callback){
        api.getSerie(id)
                .enqueue(new Callback<Serie>() {
                    @Override
                    public void onResponse(Call<Serie> call, Response<Serie> response) {
                        if (response.isSuccessful()) {
                            Serie serieResponse = response.body();
                            if (serieResponse != null) {
                                callback.onSuccess( serieResponse);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Serie> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });

    }

}
