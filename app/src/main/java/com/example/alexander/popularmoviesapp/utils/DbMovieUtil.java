package com.example.alexander.popularmoviesapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.alexander.popularmoviesapp.moviedata.DownloadedMovie;
import com.example.alexander.popularmoviesapp.moviedata.Movie;
import com.example.alexander.popularmoviesapp.moviedata.OnlineMovie;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_ID;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_VOTE_AVERAGE;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.CONTENT_URI;


/**
 * Created by awest on 8/14/2017.
 */

public final class DbMovieUtil {
    private final String LOG_TAG = DbMovieUtil.class.getSimpleName();

    // Suppress default constructor for non-instantiability
    private DbMovieUtil() {
        throw new AssertionError("The " + LOG_TAG + " class will not be instantiated or extended.");
    }

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

    public static boolean changeFavoriteMovieState(Context context, Movie movie) {
        boolean stateChangeSuccessful = false;
        if (movie instanceof OnlineMovie) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(COLUMN_MOVIE_ID, movie.getId());
            movieValues.put(COLUMN_MOVIE_TITLE, movie.getTitle());
            movieValues.put(COLUMN_MOVIE_OVERVIEW, movie.getSynopsis());
            movieValues.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getUserRating());
            movieValues.put(COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());

            try {
                movieValues.put(COLUMN_MOVIE_POSTER,
                        DbBitmapUtility.getBytes(
                                Picasso.with(context).load(((OnlineMovie) movie).getImageUrl()).get()
                        ));
                movieValues.put(COLUMN_MOVIE_BACKDROP,
                        DbBitmapUtility.getBytes(
                                Picasso.with(context).load(((OnlineMovie) movie).getBackgroundImageUrl()).get()
                        ));
            } catch (IOException e) {
                e.printStackTrace();
            }


            Uri retUri = context.getContentResolver().insert(
                    CONTENT_URI,
                    movieValues
            );

            if (retUri != null) {
                stateChangeSuccessful = true;
            }
        } else if (movie instanceof DownloadedMovie) {
            int count = context.getContentResolver().delete(
                    CONTENT_URI.buildUpon().appendPath(movie.getId()).build(),
                    null,
                    null
            );
            if (count > 0) {
                stateChangeSuccessful = true;
            }
        } else {
            throw new UnsupportedClassVersionError("Not a supported Movie class type: " +
                    movie.getClass().getName());
        }

        return stateChangeSuccessful;
    }

    public static Bitmap retrieveBitmapFromDatabase(Context context, String columnName, String movieId) {
        Bitmap image;
        Cursor cursor;
        switch (columnName) {
            case COLUMN_MOVIE_POSTER:
                cursor = context.getContentResolver().query(
                        CONTENT_URI.buildUpon().appendPath(movieId).build(),
                        new String[]{COLUMN_MOVIE_POSTER},
                        null,
                        null,
                        null
                );
                break;
            case COLUMN_MOVIE_BACKDROP:
                cursor = context.getContentResolver().query(
                        CONTENT_URI.buildUpon().appendPath(movieId).build(),
                        new String[]{COLUMN_MOVIE_BACKDROP},
                        null,
                        null,
                        null
                );
                break;
            default:
                throw new UnsupportedOperationException("Unsupported column for bitmap operation: " + columnName);
        }

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int imageIndex = cursor.getColumnIndex(columnName);
            byte[] imageData = cursor.getBlob(imageIndex);
            image = DbBitmapUtility.getImage(imageData);
        } else {
            image = null;
        }

        return image;
    }
}
