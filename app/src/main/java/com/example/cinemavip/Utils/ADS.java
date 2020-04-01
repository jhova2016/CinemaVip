package com.example.cinemavip.Utils;

import android.content.Context;
import android.content.Intent;

import com.example.cinemavip.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ADS {
    public static void showInterstitialAds(final Context context, final Intent activity) {
        final InterstitialAd mInterstitial = new InterstitialAd(context);
        mInterstitial.setAdUnitId(context.getString(R.string.intersticialAdsId));
        AdRequest.Builder builder = new AdRequest.Builder();
        mInterstitial.loadAd(builder.build());
        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitial.show();
            }

            @Override
            public void onAdClosed() {
                context.startActivity(activity);
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                context.startActivity(activity);
                super.onAdFailedToLoad(i);
            }
        });
    }
}
