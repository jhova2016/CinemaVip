package com.example.cinemavip.API.Callbacks.Series;

import com.example.cinemavip.Models.Series.Serie;

import java.util.List;

public interface onGetSeriesCallback {
    void onSuccess(List<Serie> series);

    void onError();
}
