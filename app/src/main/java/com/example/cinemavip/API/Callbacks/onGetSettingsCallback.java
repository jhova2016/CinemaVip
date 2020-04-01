package com.example.cinemavip.API.Callbacks;

import com.example.cinemavip.Models.Settings;

public interface onGetSettingsCallback {
    void onSuccess(Settings settings);

    void onError();
}
