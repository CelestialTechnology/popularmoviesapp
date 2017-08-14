package com.example.alexander.popularmoviesapp.jsondata;

import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.example.alexander.popularmoviesapp.moviedata.Movie;

/**
 * Created by awest on 8/13/2017.
 */

public class LoadFavoriteMoviesTask extends AsyncTask<Void, Void, Cursor> {

    private ArrayAdapter<Movie> mAdapter;

    public LoadFavoriteMoviesTask(ArrayAdapter<Movie> adapter) {
        mAdapter = adapter;
    }

    @Override
    protected Cursor doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
    }
}
