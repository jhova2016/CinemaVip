package com.example.cinemavip.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("app_url_android")
    @Expose
    private Object appUrlAndroid;
    @SerializedName("app_url_ios")
    @Expose
    private Object appUrlIos;
    @SerializedName("title_in_poster")
    @Expose
    private Integer titleInPoster;
    @SerializedName("app_bar_animation")
    @Expose
    private Integer appBarAnimation;
    @SerializedName("livetv")
    @Expose
    private Integer livetv;
    @SerializedName("kids")
    @Expose
    private Integer kids;
    @SerializedName("ad_app_id")
    @Expose
    private String adAppId;
    @SerializedName("ad_banner")
    @Expose
    private Integer adBanner;
    @SerializedName("ad_unit_id_banner")
    @Expose
    private String adUnitIdBanner;
    @SerializedName("ad_interstitial")
    @Expose
    private Integer adInterstitial;
    @SerializedName("ad_unit_id_interstitial")
    @Expose
    private String adUnitIdInterstitial;
    @SerializedName("ad_ios_app_id")
    @Expose
    private String adIosAppId;
    @SerializedName("ad_ios_banner")
    @Expose
    private Integer adIosBanner;
    @SerializedName("ad_ios_unit_id_banner")
    @Expose
    private String adIosUnitIdBanner;
    @SerializedName("ad_ios_interstitial")
    @Expose
    private Integer adIosInterstitial;
    @SerializedName("ad_ios_unit_id_interstitial")
    @Expose
    private String adIosUnitIdInterstitial;
    @SerializedName("app_color_dark")
    @Expose
    private Integer appColorDark;
    @SerializedName("app_background_color")
    @Expose
    private String appBackgroundColor;
    @SerializedName("app_header_recent_task_color")
    @Expose
    private String appHeaderRecentTaskColor;
    @SerializedName("app_primary_color")
    @Expose
    private String appPrimaryColor;
    @SerializedName("app_splash_color")
    @Expose
    private String appSplashColor;
    @SerializedName("app_buttons_color")
    @Expose
    private String appButtonsColor;
    @SerializedName("app_bar_color")
    @Expose
    private String appBarColor;
    @SerializedName("app_bar_opacity")
    @Expose
    private Double appBarOpacity;
    @SerializedName("app_bar_icons_color")
    @Expose
    private String appBarIconsColor;
    @SerializedName("bottom_navigation_bar_color")
    @Expose
    private String bottomNavigationBarColor;
    @SerializedName("icons_color")
    @Expose
    private String iconsColor;
    @SerializedName("text_color")
    @Expose
    private String textColor;
    @SerializedName("app_bar_title_color")
    @Expose
    private String appBarTitleColor;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Object getAppUrlAndroid() {
        return appUrlAndroid;
    }

    public void setAppUrlAndroid(Object appUrlAndroid) {
        this.appUrlAndroid = appUrlAndroid;
    }

    public Object getAppUrlIos() {
        return appUrlIos;
    }

    public void setAppUrlIos(Object appUrlIos) {
        this.appUrlIos = appUrlIos;
    }

    public Integer getTitleInPoster() {
        return titleInPoster;
    }

    public void setTitleInPoster(Integer titleInPoster) {
        this.titleInPoster = titleInPoster;
    }

    public Integer getAppBarAnimation() {
        return appBarAnimation;
    }

    public void setAppBarAnimation(Integer appBarAnimation) {
        this.appBarAnimation = appBarAnimation;
    }

    public Integer getLivetv() {
        return livetv;
    }

    public void setLivetv(Integer livetv) {
        this.livetv = livetv;
    }

    public Integer getKids() {
        return kids;
    }

    public void setKids(Integer kids) {
        this.kids = kids;
    }

    public String getAdAppId() {
        return adAppId;
    }

    public void setAdAppId(String adAppId) {
        this.adAppId = adAppId;
    }

    public Integer getAdBanner() {
        return adBanner;
    }

    public void setAdBanner(Integer adBanner) {
        this.adBanner = adBanner;
    }

    public String getAdUnitIdBanner() {
        return adUnitIdBanner;
    }

    public void setAdUnitIdBanner(String adUnitIdBanner) {
        this.adUnitIdBanner = adUnitIdBanner;
    }

    public Integer getAdInterstitial() {
        return adInterstitial;
    }

    public void setAdInterstitial(Integer adInterstitial) {
        this.adInterstitial = adInterstitial;
    }

    public String getAdUnitIdInterstitial() {
        return adUnitIdInterstitial;
    }

    public void setAdUnitIdInterstitial(String adUnitIdInterstitial) {
        this.adUnitIdInterstitial = adUnitIdInterstitial;
    }

    public String getAdIosAppId() {
        return adIosAppId;
    }

    public void setAdIosAppId(String adIosAppId) {
        this.adIosAppId = adIosAppId;
    }

    public Integer getAdIosBanner() {
        return adIosBanner;
    }

    public void setAdIosBanner(Integer adIosBanner) {
        this.adIosBanner = adIosBanner;
    }

    public String getAdIosUnitIdBanner() {
        return adIosUnitIdBanner;
    }

    public void setAdIosUnitIdBanner(String adIosUnitIdBanner) {
        this.adIosUnitIdBanner = adIosUnitIdBanner;
    }

    public Integer getAdIosInterstitial() {
        return adIosInterstitial;
    }

    public void setAdIosInterstitial(Integer adIosInterstitial) {
        this.adIosInterstitial = adIosInterstitial;
    }

    public String getAdIosUnitIdInterstitial() {
        return adIosUnitIdInterstitial;
    }

    public void setAdIosUnitIdInterstitial(String adIosUnitIdInterstitial) {
        this.adIosUnitIdInterstitial = adIosUnitIdInterstitial;
    }

    public Integer getAppColorDark() {
        return appColorDark;
    }

    public void setAppColorDark(Integer appColorDark) {
        this.appColorDark = appColorDark;
    }

    public String getAppBackgroundColor() {
        return appBackgroundColor;
    }

    public void setAppBackgroundColor(String appBackgroundColor) {
        this.appBackgroundColor = appBackgroundColor;
    }

    public String getAppHeaderRecentTaskColor() {
        return appHeaderRecentTaskColor;
    }

    public void setAppHeaderRecentTaskColor(String appHeaderRecentTaskColor) {
        this.appHeaderRecentTaskColor = appHeaderRecentTaskColor;
    }

    public String getAppPrimaryColor() {
        return appPrimaryColor;
    }

    public void setAppPrimaryColor(String appPrimaryColor) {
        this.appPrimaryColor = appPrimaryColor;
    }

    public String getAppSplashColor() {
        return appSplashColor;
    }

    public void setAppSplashColor(String appSplashColor) {
        this.appSplashColor = appSplashColor;
    }

    public String getAppButtonsColor() {
        return appButtonsColor;
    }

    public void setAppButtonsColor(String appButtonsColor) {
        this.appButtonsColor = appButtonsColor;
    }

    public String getAppBarColor() {
        return appBarColor;
    }

    public void setAppBarColor(String appBarColor) {
        this.appBarColor = appBarColor;
    }

    public Double getAppBarOpacity() {
        return appBarOpacity;
    }

    public void setAppBarOpacity(Double appBarOpacity) {
        this.appBarOpacity = appBarOpacity;
    }

    public String getAppBarIconsColor() {
        return appBarIconsColor;
    }

    public void setAppBarIconsColor(String appBarIconsColor) {
        this.appBarIconsColor = appBarIconsColor;
    }

    public String getBottomNavigationBarColor() {
        return bottomNavigationBarColor;
    }

    public void setBottomNavigationBarColor(String bottomNavigationBarColor) {
        this.bottomNavigationBarColor = bottomNavigationBarColor;
    }

    public String getIconsColor() {
        return iconsColor;
    }

    public void setIconsColor(String iconsColor) {
        this.iconsColor = iconsColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getAppBarTitleColor() {
        return appBarTitleColor;
    }

    public void setAppBarTitleColor(String appBarTitleColor) {
        this.appBarTitleColor = appBarTitleColor;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
