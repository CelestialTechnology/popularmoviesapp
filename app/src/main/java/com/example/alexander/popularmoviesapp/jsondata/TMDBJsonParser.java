package com.example.alexander.popularmoviesapp.jsondata;

import android.content.Context;
import android.util.Log;

import com.example.alexander.popularmoviesapp.moviedata.DownloadedMovie;
import com.example.alexander.popularmoviesapp.moviedata.Movie;
import com.example.alexander.popularmoviesapp.moviedata.OnlineMovie;
import com.example.alexander.popularmoviesapp.moviedata.Review;
import com.example.alexander.popularmoviesapp.moviedata.Trailer;
import com.example.alexander.popularmoviesapp.utils.DbMovieUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alexander on 11/25/2016.
 */
public class TMDBJsonParser {

    private static final String LOG_TAG = TMDBJsonParser.class.getSimpleName();


    /**
     * Default Constructor so nothing can be done with it.
     */
    TMDBJsonParser() {

    }

    public static ArrayList<Movie> parseDownloadedMovieJSON(String rawData, Context context) throws JSONException {
        final String TMDB_RESULTS = "results";

        JSONObject rawDataJson = new JSONObject(rawData);
        JSONArray rawDataArray = rawDataJson.getJSONArray(TMDB_RESULTS);

        return extractDesiredMovieFields(rawDataArray, context);
    }

    public static ArrayList<Review> parseDownloadedReviewJSON(String rawData) throws JSONException {
        final String TMDB_RESULTS = "results";

        JSONObject rawDataJson = new JSONObject(rawData);
        JSONArray rawDataArray = rawDataJson.getJSONArray(TMDB_RESULTS);

        return extractDesiredReviewFields(rawDataArray);
    }

    private static ArrayList<Review> extractDesiredReviewFields(JSONArray resultArray) {
        final String TMDB_REVIEW_AUTHOR = "author";
        final String TMDB_REVIEW_CONTENT = "content";

        ArrayList<Review> reviews = new ArrayList<>();
        try {
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentReview = resultArray.getJSONObject(i);

                reviews.add(new Review(
                        currentReview.getString(TMDB_REVIEW_AUTHOR),
                        currentReview.getString(TMDB_REVIEW_CONTENT)
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return reviews;
    }

    public static ArrayList<Trailer> parseDownloadedTrailerJSON(String rawData) throws JSONException {
        final String TMDB_RESULTS = "results";

        JSONObject rawDataJson = new JSONObject(rawData);
        JSONArray rawDataArray = rawDataJson.getJSONArray(TMDB_RESULTS);

        return extractDesiredTrailerFields(rawDataArray);
    }

    private static ArrayList<Trailer> extractDesiredTrailerFields(JSONArray resultArray) {
        final String TMDB_VIDEO_KEY = "key";
        final String TMDB_VIDEO_NAME = "name";
        final String TMDB_VIDEO_TYPE = "type";

        ArrayList<Trailer> trailers = new ArrayList<>();
        try {
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentTrailer = resultArray.getJSONObject(i);

                trailers.add(
                        new Trailer(
                                currentTrailer.getString(TMDB_VIDEO_NAME),
                                currentTrailer.getString(TMDB_VIDEO_KEY),
                                currentTrailer.getString(TMDB_VIDEO_TYPE))
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trailers;
    }


    private static ArrayList<Movie> extractDesiredMovieFields(JSONArray resultsArray, Context context) {

        final String TMDB_ID = "id";
        final String TMDB_TITLE = "title";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_RELEASE_DATE = "release_date";

        ArrayList<Movie> movies = new ArrayList<Movie>();
        boolean isAlreadyDownloadedMovie;
        Movie movie;
        try {
            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentMovie = resultsArray.getJSONObject(i);

                isAlreadyDownloadedMovie = DbMovieUtil.isFavoriteMovie(context, currentMovie.getString(TMDB_ID));

                if (isAlreadyDownloadedMovie) {
                    movie = new DownloadedMovie(
                            currentMovie.getString(TMDB_ID),
                            currentMovie.getString(TMDB_TITLE),
                            currentMovie.getString(TMDB_OVERVIEW),
                            currentMovie.getDouble(TMDB_VOTE_AVERAGE),
                            currentMovie.getString(TMDB_RELEASE_DATE)
                    );
                } else {
                    movie = new OnlineMovie(
                            currentMovie.getString(TMDB_ID),
                            currentMovie.getString(TMDB_TITLE),
                            currentMovie.getString(TMDB_POSTER_PATH),
                            currentMovie.getString(TMDB_BACKDROP_PATH),
                            currentMovie.getString(TMDB_OVERVIEW),
                            currentMovie.getDouble(TMDB_VOTE_AVERAGE),
                            currentMovie.getString(TMDB_RELEASE_DATE)
                    );
                }
                movies.add(movie);
            }
        } catch (JSONException e) {
            Log.v(LOG_TAG, "Error Parsing Data! @extractDesiredMovieFields()");
            e.printStackTrace();
            return null;
        }
        return movies;
    }


}
