package com.example.alexander.popularmoviesapp.jsondata;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.alexander.popularmoviesapp.moviedata.DownloadedMovie;
import com.example.alexander.popularmoviesapp.moviedata.Movie;

import java.util.ArrayList;

import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_ID;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_VOTE_AVERAGE;
import static com.example.alexander.popularmoviesapp.database.TMDBContract.FavoriteMovieEntry.CONTENT_URI;


/**
 * Created by awest on 8/13/2017.
 */

public class LoadFavoriteMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    private ArrayAdapter<Movie> mAdapter;
    private Context mContext;

    public LoadFavoriteMoviesTask(ArrayAdapter<Movie> adapter, Context context) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        try {
            ArrayList<Movie> movies = new ArrayList<>();
            String[] projection = new String[]{
                    COLUMN_MOVIE_ID,
                    COLUMN_MOVIE_TITLE,
                    COLUMN_MOVIE_OVERVIEW,
                    COLUMN_MOVIE_VOTE_AVERAGE,
                    COLUMN_MOVIE_RELEASE_DATE
            };
            Cursor cursor = mContext.getContentResolver().query(
                    CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex(COLUMN_MOVIE_ID);
                    int titleIndex = cursor.getColumnIndex(COLUMN_MOVIE_TITLE);
                    int overviewIndex = cursor.getColumnIndex(COLUMN_MOVIE_OVERVIEW);
                    int voteAvgIndex = cursor.getColumnIndex(COLUMN_MOVIE_VOTE_AVERAGE);
                    int releaseDateIndex = cursor.getColumnIndex(COLUMN_MOVIE_RELEASE_DATE);

                    String id = cursor.getString(idIndex);
                    String title = cursor.getString(titleIndex);
                    String overview = cursor.getString(overviewIndex);
                    double voteAvg = cursor.getDouble(voteAvgIndex);
                    String releaseDate = cursor.getString(releaseDateIndex);

                    movies.add(new DownloadedMovie(
                            id,
                            title,
                            overview,
                            voteAvg,
                            releaseDate
                    ));

                }
                return movies;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        mAdapter.clear();
        if (movies != null && movies.size() > 0) {
            for (Movie m : movies) {
                mAdapter.add(m);
            }
        } else {
            Toast.makeText(mContext, "No Favorite Movies!", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(movies);
    }
}
