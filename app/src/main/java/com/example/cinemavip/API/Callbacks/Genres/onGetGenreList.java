package com.example.cinemavip.API.Callbacks.Genres;

import com.example.cinemavip.Models.Genre.GenreList;

import java.util.List;

public interface onGetGenreList {
    void onSuccess(List<GenreList> genreList);

    void onError();
}
