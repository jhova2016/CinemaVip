package com.example.cinemavip.Models;

import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Series.Serie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {
    @SerializedName("movies")
    @Expose
    private List<Movie> movies = null;
    @SerializedName("series")
    @Expose
    private List<Serie> series = null;
    @SerializedName("livetvs")
    @Expose
    private List<Object> livetvs = null;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    public List<Object> getLivetvs() {
        return livetvs;
    }

    public void setLivetvs(List<Object> livetvs) {
        this.livetvs = livetvs;
    }

}
