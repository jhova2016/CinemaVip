package com.example.cinemavip.Models.Series;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Temporada implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tmdb_id")
    @Expose
    private Integer tmdbId;
    @SerializedName("serie_id")
    @Expose
    private Integer serieId;
    @SerializedName("season_number")
    @Expose
    private Integer seasonNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("overview")
    @Expose
    private Object overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("air_date")
    @Expose
    private String airDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("episodes")
    @Expose
    private List<Episodio> episodes = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public Integer getSerieId() {
        return serieId;
    }

    public void setSerieId(Integer serieId) {
        this.serieId = serieId;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getOverview() {
        return overview;
    }

    public void setOverview(Object overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
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

    public List<Episodio> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episodio> episodes) {
        this.episodes = episodes;
    }

}
