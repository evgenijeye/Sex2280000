package com.example.exam.common.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Episode {
    public  final  static String EPISODE_ID = "episodeId";
    public  final  static String IMAGES = "images";
    public  final  static String NAME = "name";
    public  final  static String DESCRIPTION = "description";
    public  final  static String YEAR = "year";

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String episodeId;
    private String description;
    private String poster;
    private String name;
    private String year;

    public Episode(JSONObject movieJson) throws JSONException {
        episodeId = movieJson.getString(EPISODE_ID);
        name    = movieJson.getString(NAME);
        description    = movieJson.getString(DESCRIPTION);
        year    = movieJson.getString(YEAR);
        poster    = (String) movieJson.getJSONArray(IMAGES).get(0);
    }
}
