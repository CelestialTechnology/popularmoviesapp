package com.example.alexander.popularmoviesapp.jsondata;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.alexander.popularmoviesapp.TrailerAdapter;
import com.example.alexander.popularmoviesapp.moviedata.Trailer;
import com.example.alexander.popularmoviesapp.utils.NetworkUtility;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by awest on 8/14/2017.
 */

public class DownloadTrailerJSONTask extends AsyncTask<String, Void, ArrayList<Trailer>> {
    private final String LOG_TAG = DownloadTrailerJSONTask.class.getSimpleName();
    private TrailerAdapter mAdapter;
    private RecyclerView recyclerView;

    public DownloadTrailerJSONTask(RecyclerView recyclerView, TrailerAdapter adapter) {
        mAdapter = adapter;
        this.recyclerView = recyclerView;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        // The movie id string should be taken in
        try {
            return loadJSONFromNetwork(params[0] + "/videos");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);
        if (trailers != null && trailers.size() > 0) {
            mAdapter.loadTrailers(trailers);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private ArrayList<Trailer> loadJSONFromNetwork(String urlString) throws IOException {
        URL trailerUrl = NetworkUtility.createURL(urlString);
        String downloadedJSONData = NetworkUtility.readInputStream(NetworkUtility.getInputStreamFromURL(trailerUrl));

        try {
            return TMDBJsonParser.parseDownloadedTrailerJSON(downloadedJSONData);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "@loadJSONFromNetwork! JsonParserException: Parse Failed!");
            return null;
        }
    }
}
