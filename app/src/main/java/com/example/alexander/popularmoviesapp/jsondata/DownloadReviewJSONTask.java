package com.example.alexander.popularmoviesapp.jsondata;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.alexander.popularmoviesapp.adapter.ReviewAdapter;
import com.example.alexander.popularmoviesapp.moviedata.Review;
import com.example.alexander.popularmoviesapp.utils.NetworkUtility;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by awest on 8/15/2017.
 */

public class DownloadReviewJSONTask extends AsyncTask<String, Void, ArrayList<Review>> {
    private final String LOG_TAG = DownloadReviewJSONTask.class.getSimpleName();
    private ReviewAdapter mAdapter;
    private RecyclerView recyclerView;

    public DownloadReviewJSONTask(RecyclerView recyclerView, ReviewAdapter reviewAdapter) {
        mAdapter = reviewAdapter;
        this.recyclerView = recyclerView;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {
        try {
            return loadJSONFromNetwork(params[0] + "/reviews");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        super.onPostExecute(reviews);
        if (reviews != null && reviews.size() > 0) {
            mAdapter.loadReviews(reviews);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private ArrayList<Review> loadJSONFromNetwork(String urlString) throws IOException {
        URL trailerUrl = NetworkUtility.createURL(urlString);
        String downloadedJSONData = NetworkUtility.readInputStream(NetworkUtility.getInputStreamFromURL(trailerUrl));

        try {
            return TMDBJsonParser.parseDownloadedReviewJSON(downloadedJSONData);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "@loadJSONFromNetwork! JsonParserException: Parse Failed!");
            return null;
        }
    }
}
