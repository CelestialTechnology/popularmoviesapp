package com.example.alexander.popularmoviesapp.moviedata;

import android.graphics.Bitmap;

/**
 * Created by awest on 8/13/2017.
 */

public class DownloadedMovie extends Movie {
    private Bitmap poster;
    private Bitmap backdrop;

    public DownloadedMovie(String id, String title, String synopsis,
                           double userRating, String releaseDate,
                           Bitmap poster, Bitmap backdrop) {
        super(id, title, synopsis, userRating, releaseDate);

        this.poster = poster;
        this.backdrop = backdrop;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public Bitmap getBackdrop() {
        return backdrop;
    }
}
