package com.example.alexander.popularmoviesapp.moviedata;

/**
 * Created by awest on 8/13/2017.
 */

public class OnlineMovie extends Movie {
    private String imageUrl = "http://image.tmdb.org/t/p/w342";
    private String backgroundImageUrl = "http://image.tmdb.org/t/p/w780";

    public OnlineMovie(String id, String title, String imageUrl,
                       String backgroundImageUrl, String synopsis,
                       double userRating, String releaseDate) {

        super(id, title, synopsis, userRating, releaseDate);

        this.imageUrl += imageUrl;
        this.backgroundImageUrl += backgroundImageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

}
