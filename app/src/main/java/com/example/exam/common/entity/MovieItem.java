package com.example.exam.common.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieItem {
    public  final  static String MOVIE_ID = "movieId";
    public  final  static String POSTER = "poster";
    public  final  static String NAME = "name";
    private String movieId;
    private String poster;
    private String name;

    public static String getMovieId() {
        return MOVIE_ID;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public static String getPOSTER() {
//        return POSTER;
//    }
//
//    public static String getNAME() {
//        return NAME;
//    }

    public MovieItem(JSONObject movieJson) throws JSONException {
        movieId = movieJson.getString(MOVIE_ID);
        poster  = movieJson.getString(POSTER);
        name    = movieJson.getString(NAME);
    }

}
