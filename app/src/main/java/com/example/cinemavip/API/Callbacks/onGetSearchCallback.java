package com.example.cinemavip.API.Callbacks;

import com.example.cinemavip.Models.Search;

public interface onGetSearchCallback {
    void onSuccess(Search dataRetrieved);

    void onError();

}
