package com.example.cinemavip.API.Callbacks.Genres;

import com.example.cinemavip.Models.Genre.GenreMovieList;

public interface onGetGenreListMovie {
    void onSuccess(GenreMovieList movieList);

    void onError();
}
