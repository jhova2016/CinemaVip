package com.example.cinemavip.Models;

import java.io.Serializable;

public class Video  implements Serializable {
    int id;
    int movieId;
    String server;
    String link;
    String lang;
    int hd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int isHd() {
        return hd;
    }

    public void setHd(int hd) {
        this.hd = hd;
    }
}
