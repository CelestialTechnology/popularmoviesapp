package com.example.alexander.popularmoviesapp;

/**
 * Created by awest on 8/11/2017.
 */

public class Trailer {
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
}
