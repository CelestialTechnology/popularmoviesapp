package com.example.alexander.popularmoviesapp.jsondata;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.alexander.popularmoviesapp.moviedata.Movie;
import com.example.alexander.popularmoviesapp.utils.NetworkUtility;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DownloadMovieJSONTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = DownloadMovieJSONTask.class.getSimpleName();
    private ArrayAdapter<Movie> adapter;
    private Context context;

    public DownloadMovieJSONTask(ArrayAdapter<Movie> adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        // params comes from the execute()call: params[0] is the url
        try {
            return loadJSONFromNetwork(params[0]);
        } catch (IOException e) {
            Log.d(LOG_TAG, "Unable to retrieve web Data. URL may be invalid");
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        if (result != null && adapter != null) {
            adapter.clear();
            for (Movie m : result) {

                adapter.add(m);
            }
        }
        super.onPostExecute(result);
    }

    private ArrayList<Movie> loadJSONFromNetwork(String urlString) throws IOException {

        URL url = TMDBJsonParser.createURL(urlString);
        String downloadedJSONData = NetworkUtility.readInputStream(NetworkUtility.getInputStreamFromURL(url));

        try {
            return TMDBJsonParser.parseDownloadedJSON(downloadedJSONData, context);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "@loadJSONFromNetwork! JsonParserException: Parse Failed!");
            return null;
        }
    }

}

