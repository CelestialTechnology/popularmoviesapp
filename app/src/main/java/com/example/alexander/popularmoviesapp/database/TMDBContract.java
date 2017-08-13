package com.example.alexander.popularmoviesapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by awest on 8/11/2017.
 */

public class TMDBContract {
    // This class shall hold the proper values for the database
    private TMDBContract() {
    }

    // Define the Base content URI
    public static final String CONTENT_AUTHORITY = "com.example.alexander.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "FavoriteMovieEntry";

    public static class FavoriteMovieEntry implements BaseColumns {

        // Uri to obtain all the entries in the table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "FavoriteMovieEntry";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";

        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
    }
}
