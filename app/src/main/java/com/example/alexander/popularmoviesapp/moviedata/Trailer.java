package com.example.alexander.popularmoviesapp.moviedata;

/**
 * Created by awest on 8/11/2017.
 */

public class Trailer implements JsonDataType {
    private String name;
    private String urlKey;
    private String type;

    public Trailer(String name, String urlKey, String type) {
        this.name = name;
        this.urlKey = urlKey;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getJsonDataType() {
        return "Trailer";
    }

    @Override
    public String toString() {
        return "Trailer - " +
                "name: " + name + " " +
                "key: " + urlKey + " " +
                "type: " + type;
    }
}
