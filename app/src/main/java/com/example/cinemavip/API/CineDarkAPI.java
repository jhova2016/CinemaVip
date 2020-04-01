package com.example.cinemavip.API;

import com.example.cinemavip.Models.Genre.GenreList;
import com.example.cinemavip.Models.Genre.GenreMovieList;
import com.example.cinemavip.Models.Genre.GenreSerieList;
import com.example.cinemavip.Models.Peliculas.Movie;
import com.example.cinemavip.Models.Peliculas.MoviePage;
import com.example.cinemavip.Models.Search;
import com.example.cinemavip.Models.Series.Serie;
import com.example.cinemavip.Models.Series.SeriePage;
import com.example.cinemavip.Models.Settings;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CineDarkAPI {

    @GET("movies")
    Call <MoviePage> getPageMovie(
            @Query("page") int page
    );

    @GET("movies/relateds/{id}")
    Call<List<Movie>> getRelatedsMovies(
            @Path("id") int id
    );

    @GET("movies/show/{id}")
    Call<Movie> getMovie(
            @Path("id") int id
    );

    @GET("movies/popular")
    Call<List<Movie>> getMoviesPopular();

    @GET("movies/recents")
    Call<List<Movie>> getMoviesRecents();

    @GET("movies/latest")
    Call<List<Movie>> getSlider();

    @GET("series/show/{id}")
    Call<Serie> getSerie(
            @Path("id") int id
    );

    @GET("series/recents")
    Call<List<Serie>> getSeriesRecientes();

    @GET("series/popular")
    Call<List<Serie>> getSeriesPopular();

    @GET("series")
    Call<SeriePage> getPageSerie(
            @Query("page") int page
    );

    @GET("settings")
    Call<Settings> getSettings();

    @GET("search/{query}")
    Call<Search> getSearch(
            @Path("query") String query
    );

    @GET("genres/movies/show/{id}")
    Call<GenreMovieList> getGenreMovieList(
            @Path("id") int id,
            @Query("page") int page
    );

    @GET("genres/series/show/{id}")
    Call<GenreSerieList> getGenreSerieList(
            @Path("id") int id,
            @Query("page") int page
    );

    @GET("genres/list")
    Call<List<GenreList>> getGenreList();

}

