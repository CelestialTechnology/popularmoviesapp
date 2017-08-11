package com.example.alexander.popularmoviesapp.jsondata;

import android.net.Uri;
import android.util.Log;

import com.example.alexander.popularmoviesapp.BuildConfig;
import com.example.alexander.popularmoviesapp.moviedata.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Alexander on 11/25/2016.
 */
public class TMDBJsonParser {

    private static final String LOG_TAG = TMDBJsonParser.class.getSimpleName();
    private static String API_KEY = BuildConfig.TMDB_API_KEY;
    private static String TMDB_BASE_URL = "https://api.themoviedb.org/3";
    private static String SORT_QUERY_PATH = "movie/";
    private static String API_KEY_PARAM = "api_key";


    /**
     * Default Constructor so nothing can be done with it.
     */
    TMDBJsonParser() {

    }

    public static URL createURL(String query) throws MalformedURLException {

        Uri builtUri;

        builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendEncodedPath(SORT_QUERY_PATH + query)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        return new URL(builtUri.toString());
    }


    public static ArrayList<Movie> parseDownloadedJSON(String rawData) throws JSONException {
        final String TMDB_RESULTS = "results";

        JSONObject rawDataJson = new JSONObject(rawData);
        JSONArray rawDataArray = rawDataJson.getJSONArray(TMDB_RESULTS);

        return extractDesiredFields(rawDataArray);
    }

    private static ArrayList<Movie> extractDesiredFields(JSONArray resultsArray) {

        final String TMDB_ID = "id";
        final String TMDB_TITLE = "title";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_RELEASE_DATE = "release_date";

        ArrayList<Movie> movies = new ArrayList<Movie>();

        try {
            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentMovie = resultsArray.getJSONObject(i);

                movies.add(new Movie(
                        currentMovie.getString(TMDB_ID),
                        currentMovie.getString(TMDB_TITLE),
                        currentMovie.getString(TMDB_POSTER_PATH),
                        currentMovie.getString(TMDB_BACKDROP_PATH),
                        currentMovie.getString(TMDB_OVERVIEW),
                        currentMovie.getDouble(TMDB_VOTE_AVERAGE),
                        currentMovie.getString(TMDB_RELEASE_DATE)
                ));
            }
        } catch (JSONException e) {
            Log.v(LOG_TAG, "Error Parsing Data! @extractDesiredFields()");
            e.printStackTrace();
            return null;
        }
        return movies;
    }
}
