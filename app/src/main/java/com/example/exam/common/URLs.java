package com.example.exam.common;

public class URLs {
    private final static String BASE = "http://cinema.areas.su/";
    public final static String  REGISTER = BASE + "auth/register";
    public final static String  LOGON = BASE + "auth/login";
    public final static String  COVER = BASE + "movies/cover";
    public final static String  IMAGE = BASE + "up/images/";
    public final static String  MOVIE_WITH_FILTER = BASE + "movies?filter=";
    public final static String  MOVIE_LAST = BASE + "usermovies?filter=lastView";

    public static String MOVIE_ID(int movieItemId) {
        return  BASE+"movies/"+movieItemId;
    }

    public static String MOVIE_ID_EPISODES(int movieItemId) {

        return  BASE+"movies/"+movieItemId+"/episodes";
    }
}
