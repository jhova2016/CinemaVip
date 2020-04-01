package com.example.cinemavip.API.Callbacks.Genres;

import com.example.cinemavip.Models.Genre.GenreSerieList;

public interface onGetGenreListSerie {
    void onSuccess(GenreSerieList movieList);

    void onError();
}
