package com.example.cinemavip.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemavip.Adapters.Holders.ItemMediaHolder;
import com.example.cinemavip.Adapters.Holders.UnifiedNativeAdViewHolder;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.R;
import com.example.cinemavip.Utils.rvItemClickListenner;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UnifiedMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_MOVIE_VIEW_TYPE = 0;
    private static final int MENU_ITEM_SERIE_VIEW_TYPE = 1;
    // The unified native ad view type.
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 2;

    private ArrayList<Object> mRecyclerViewItems = new ArrayList<>();
    private ItemMediaHolder menuItemHolder;

    private rvItemClickListenner clickListener;

    public UnifiedMediaAdapter(ArrayList<Object> movies, ArrayList<Object> series){
        if (movies != null){
            mRecyclerViewItems = movies; // se está llenando de vacio, en lugar de hacer una addAll se necesita hacer una asignación
        } else {
            mRecyclerViewItems = series;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == UNIFIED_NATIVE_AD_VIEW_TYPE) {
            View unifiedNativeLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.ad_unified,
                    parent, false);
            return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_movie, parent, false);
            return new ItemMediaHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        Log.e("View Type -> ", String.valueOf(viewType));
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) mRecyclerViewItems.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
                break;
            case MENU_ITEM_SERIE_VIEW_TYPE:
                menuItemHolder = (ItemMediaHolder) holder;
                Serie menuItemS = (Serie) mRecyclerViewItems.get(position);

                Picasso.get().load(menuItemS.getPosterPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(menuItemHolder.itemImage);
                menuItemHolder.itemText.setText(menuItemS.getName());
                menuItemHolder.itemImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onItemClick(position, v);
                    }
                });
                break;
            default:
            menuItemHolder = (ItemMediaHolder) holder;
            Movie menuItemM = (Movie) mRecyclerViewItems.get(position);
            Picasso.get().load(menuItemM.getPosterPath()).placeholder(R.drawable.not_found_pholder).fit().centerCrop().into(menuItemHolder.itemImage);
            menuItemHolder.itemText.setText(menuItemM.getTitle());
            menuItemHolder.itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(position, v);
                }
            });
            break;
        }
    }

    public void setOnItemClickListener(rvItemClickListenner clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        if (mRecyclerViewItems != null){
            Log.e("Returned -> ", String.valueOf(mRecyclerViewItems.size()));
            return mRecyclerViewItems.size();
        }else {
            Log.e("Returned -> ", "0");
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = mRecyclerViewItems.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        } else {
            if (recyclerViewItem instanceof Movie){
                return MENU_ITEM_MOVIE_VIEW_TYPE;
            } else {
                return MENU_ITEM_SERIE_VIEW_TYPE;
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        mRecyclerViewItems.addAll(moviesToAppend);
        notifyDataSetChanged();
    }
    public void appendSeries(List<Serie> seriesToAppend) {
        mRecyclerViewItems.addAll(seriesToAppend);
        notifyDataSetChanged();
    }
    public ArrayList<Object> getMovies(){
        return mRecyclerViewItems;
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
}
