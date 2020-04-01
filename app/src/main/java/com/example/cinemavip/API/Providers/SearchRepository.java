package com.example.cinemavip.API.Providers;

import android.util.Log;

import com.example.cinemavip.API.Callbacks.onGetSearchCallback;
import com.example.cinemavip.API.CineDarkAPI;
import com.example.cinemavip.Models.Search;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRepository {
    private static final String BASE_URL = "https://panel.tvmoviesapp.com/api/";

    private static SearchRepository repository;

    private CineDarkAPI api;

    private SearchRepository(CineDarkAPI api) {
        this.api = api;
    }

    public static SearchRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new SearchRepository(retrofit.create(CineDarkAPI.class));
        }

        return repository;
    }
    public void searchMedia(String query, final onGetSearchCallback callback){
        api.getSearch(query)
                .enqueue(new Callback<Search>() {
                    @Override
                    public void onResponse(Call<Search> call, Response<Search> response) {
                        Search searchResponse = response.body();
                        if (searchResponse != null) {
                            callback.onSuccess( searchResponse);
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Search> call, Throwable t) {
                        Log.e("ERROR -> ", t.getMessage());
                        callback.onError();
                    }
                });
    }
}
