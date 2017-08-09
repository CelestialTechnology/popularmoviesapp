package com.example.alexander.popularmoviesapp.data;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.alexander.popularmoviesapp.Movie;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadMovieJSONTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = DownloadMovieJSONTask.class.getSimpleName();
    private ArrayAdapter<Movie> adapter;

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
            Log.d(LOG_TAG, "SOMETHING");
            adapter.clear();
            for (Movie m : result) {

                adapter.add(m);
            }
        }
        super.onPostExecute(result);
    }

    public void setDownloadAdapter(ArrayAdapter<Movie> adapter) {
        this.adapter = adapter;
    }


    private ArrayList<Movie> loadJSONFromNetwork(String urlString) throws IOException {

        URL url = TMDBJsonParser.createURL(urlString);
        String downloadedJSONData = readInputStream(getInputStreamFromURL(url));

        try {
            return TMDBJsonParser.parseDownloadedJSON(downloadedJSONData);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "@loadJSONFromNetwork! JsonParserException: Parse Failed!");
            return null;
        }
    }

    // Given a URL, establishes an HttpUrlCOnnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private InputStream getInputStreamFromURL(URL createdUrl) throws IOException {
        URL url = createdUrl;
        InputStream is;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10_000 /* milliseconds */);
        conn.setConnectTimeout(15_000 /* milliseconds */);
        conn.setRequestMethod("GET");
        if (conn.getDoInput() != true) {
            conn.setDoInput(true);
        }
        // Starts the query
        conn.connect();
        is = conn.getInputStream();
        if (conn != null) {
            conn.disconnect();
        }
        return is;
    }


    // Reads an InputStream and converts it to a String.
    private String readInputStream(InputStream stream) throws IOException {

        BufferedReader reader;
        StringBuffer buffer = new StringBuffer();
        if (stream == null) {
            Log.d(LOG_TAG, "Nothing to Download?");
        }
        reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            // Stream was empty, no point in parsing
            Log.v(LOG_TAG, "BUFFER WAS EMPTY!!");
            return null;
        }
        reader.close();
        return buffer.toString();
    }

}

