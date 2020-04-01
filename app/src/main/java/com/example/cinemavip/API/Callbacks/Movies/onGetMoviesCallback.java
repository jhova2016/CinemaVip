package com.example.cinemavip.API.Callbacks.Movies;

import com.example.cinemavip.Models.Peliculas.Movie;

import java.util.List;

public interface onGetMoviesCallback {
    void onSuccess(List<Movie> movies);

    void onError();
}
