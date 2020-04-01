package com.example.cinemavip.API.Callbacks.Series;

import com.example.cinemavip.Models.Series.Serie;

import java.util.List;

public interface onGetSeriePageCallback {
    void onSuccess(int page, List<Serie> movies);
    void onError();
}
