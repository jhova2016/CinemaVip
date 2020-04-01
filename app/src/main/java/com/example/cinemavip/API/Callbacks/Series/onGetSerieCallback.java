package com.example.cinemavip.API.Callbacks.Series;

import com.example.cinemavip.Models.Series.Serie;

public interface onGetSerieCallback {
    void onSuccess(Serie serie);

    void onError();
}
