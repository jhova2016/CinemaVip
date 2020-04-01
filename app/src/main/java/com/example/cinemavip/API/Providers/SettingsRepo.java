package com.example.cinemavip.API.Providers;

import android.util.Log;

import com.example.cinemavip.API.Callbacks.onGetSettingsCallback;
import com.example.cinemavip.API.CineDarkAPI;
import com.example.cinemavip.Models.Settings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsRepo {
    private static final String BASE_URL = "https://panel.tvmoviesapp.com/api/";

    private static SettingsRepo repository;

    private CineDarkAPI api;

    private SettingsRepo(CineDarkAPI api) {
        this.api = api;
    }

    public static SettingsRepo getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new SettingsRepo(retrofit.create(CineDarkAPI.class));
        }

        return repository;
    }

    public void getSettings(final onGetSettingsCallback callback) {
        api.getSettings()
                .enqueue(new Callback<Settings>() {
                    @Override
                    public void onResponse(Call<Settings> call, Response<Settings> response) {
                        if (response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Settings> call, Throwable t) {
                        callback.onError(); Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });
    }
}
