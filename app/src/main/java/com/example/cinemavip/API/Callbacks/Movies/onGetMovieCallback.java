package com.example.cinemavip.API.Callbacks.Movies;

import com.example.cinemavip.Models.Peliculas.Movie;

public interface onGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
