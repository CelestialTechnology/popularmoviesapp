package com.example.alexander.popularmoviesapp.utils;

import android.content.Context;
import android.database.Cursor;

import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.CONTENT_URI;

/**
 * Created by awest on 8/14/2017.
 */

public class DbMovieUtil {

    public static boolean isFavoriteMovie(Context context, String movieId) {
        boolean isFavMovie = false;
        Cursor retCur = context.getContentResolver().query(
                CONTENT_URI.buildUpon().appendPath(movieId).build(),
                null,
                null,
                null,
                null
        );

        if (retCur != null && retCur.getCount() > 0) isFavMovie = true;

        retCur.close();
        return isFavMovie;
    }
}
