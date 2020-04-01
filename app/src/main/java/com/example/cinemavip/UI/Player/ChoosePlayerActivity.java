package com.example.cinemavip.UI.Player;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.Adapters.VideosAdapter;
import com.example.cinemavip.Models.Video;
import com.example.cinemavip.R;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ChoosePlayerActivity extends AppCompatActivity {
    RecyclerView rvVideos;
    ArrayList<Video> videos = new ArrayList<>();
    ArrayList<String> videosNameServer = new ArrayList<>();
    LinearLayout ll;
    private Toolbar toolbar;

    private LinearLayoutManager LLM;

    //private RewardedAd rewardedAd;
    private InterstitialAd mInterstitialAd0;
    private InterstitialAd mInterstitialAd1;

    int selected;
    private AlertDialog.Builder mdBuildr;
    private AlertDialog mAlert;
    private VideosAdapter videosAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        hideSystemUI();
        setContentView(R.layout.activity_choose_player);

        videos =  (ArrayList<Video>)getIntent().getSerializableExtra("selectedVideo");

        rvVideos = findViewById(R.id.listVideoServer);
        LLM = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rvVideos.setLayoutManager(LLM);
        videosAdapter = new VideosAdapter(videos);
        videosAdapter.setOnItemClickListener(
                new rvItemClickListenner() {
                    @Override
                    public void onItemClick(int position, View v) {
                        selected = position;
                        if (mInterstitialAd0.isLoaded()){
                            mInterstitialAd0.show();
                        }else{
                            startPlayerActivity();
                        }
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                },
                new rvItemClickListenner() {
                    @Override
                    public void onItemClick(int position, View v) {
                        selected = position;
                        if (mInterstitialAd0.isLoaded()){
                            mInterstitialAd0.show();
                        }else{
                            startSreamActivity();
                        }
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                },
                new rvItemClickListenner() {
                    @Override
                    public void onItemClick(int position, View v) {
                        TastyToast.makeText(getApplicationContext(), "Proximamente", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });
        rvVideos.setAdapter(videosAdapter);

        mInterstitialAd0 = new InterstitialAd(this);
        mInterstitialAd0.setAdUnitId(getResources().getString(R.string.intersticialAdsId));
        mInterstitialAd0.loadAd(new AdRequest.Builder().addTestDevice("19710009EFF315F6A8E81BC719DBA6E0").build());
        mInterstitialAd0.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                TastyToast.makeText(getApplicationContext(), "Enlaces listos!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                for(int i = 0; i<videos.size(); i++){
                    LLM.findViewByPosition(i).findViewById(R.id.playVideo).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                TastyToast.makeText(getApplicationContext(), "Enlaces listos!", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                for(int i = 0; i<videos.size(); i++){
                    LLM.findViewByPosition(i).findViewById(R.id.playVideo).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                //mContext.startActivity(activity2Start);
                TastyToast.makeText(getApplicationContext(), "Disfruta la pelicula", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                startPlayerActivity();
            }
        });

        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd1.setAdUnitId(getResources().getString(R.string.intersticialAdsId));
        mInterstitialAd1.loadAd(new AdRequest.Builder().addTestDevice("19710009EFF315F6A8E81BC719DBA6E0").build());
        mInterstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                TastyToast.makeText(getApplicationContext(), "Enlaces de transmisión, listos", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                for(int i = 0; i<videos.size(); i++){
                        LLM.findViewByPosition(i).findViewById(R.id.transmitVideo).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                TastyToast.makeText(getApplicationContext(), "Enlaces listos!", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                for(int i = 0; i<videos.size(); i++){
                        LLM.findViewByPosition(i).findViewById(R.id.transmitVideo).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                //mContext.startActivity(activity2Start);
                TastyToast.makeText(getApplicationContext(), "Disfruta la pelicula", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                startSreamActivity();
            }
        });

        TastyToast.makeText(getApplicationContext(), "Obteniendo enlaces", TastyToast.LENGTH_LONG, TastyToast.INFO);

        Button btnR = findViewById(R.id.btnReport);
        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse("https://m.me/CineDarkPlayOficial"));
                startActivity(facebookIntent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String bgImage = getIntent().getStringExtra("backgroundImage");

        ll = findViewById(R.id.llmain);
        Picasso.get().load(bgImage).placeholder(R.drawable.not_found_pholder).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ll.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        for (int i = 0; i<videos.size(); i++){
            videosNameServer.add(videos.get(i).getServer());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void appNotInstalledDownload(final String appName, final String packageName, final boolean isDownloadExternal) {

        mdBuildr = new AlertDialog.Builder(ChoosePlayerActivity.this);

        mdBuildr.setTitle("Importante");
        mdBuildr.setMessage(getApplicationContext().getString(R.string.download_msg, appName));
        mdBuildr.setPositiveButton("Si", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isDownloadExternal) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.xmtv_download_link))));
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }
            }
        });
        mdBuildr.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlert.dismiss();
            }
        });
        mdBuildr.setCancelable(false);
        mAlert = mdBuildr.create();
        mAlert.show();
    }

    private void startPlayerActivity(){
        if(videos.get(selected).getLink().contains(".m3u8")){
            Intent playMovie = new Intent(getApplicationContext(), ExoplayerActivity.class);
            playMovie.putExtra("streamUrl", videos.get(selected).getLink());
            playMovie.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(playMovie);
        }else{
            Intent playMovie = new Intent(getApplicationContext(), PlayerActivity.class);
            playMovie.putExtra("streamUrl", videos.get(selected).getLink());
            playMovie.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(playMovie);
        }
    }

    private void startSreamActivity(){
        if (isAppInstalled(getApplicationContext(), "com.instantbits.cast.webvideo")) {
            Uri uri = Uri.parse(videos.get(selected).getLink());
            Intent player = new Intent(Intent.ACTION_VIEW);
            player.setPackage("com.instantbits.cast.webvideo");
            player.setDataAndTypeAndNormalize(uri, "video/*");
            player.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(player);
        } else {
            appNotInstalledDownload(getResources().getString(R.string.xmtv_player), "com.instantbits.cast.webvideo", false);
        }
    }

}
