package com.example.alexander.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Alexander on 6/16/2016.
 */
public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_movie_fragment, new MovieDetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. THe action bar will
        // automatically handle clicks on the HOme/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MovieDetailFragment extends Fragment {
        private String LOG_TAG = MovieDetailFragment.class.getSimpleName();
        private Movie movie;

        public MovieDetailFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent movieDetailIntent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            if (movieDetailIntent != null && movieDetailIntent.hasExtra("Movie")) {
                movie = movieDetailIntent.getParcelableExtra("Movie");
                Log.v(LOG_TAG, "MOVIE RECEIVED FOR DETAIL!!!");
                loadMovieDetails(movie, rootView);
            }

            return rootView;
        }

        // This helper method loads the movie data to the views in fragment_movie_detail
        public void loadMovieDetails(Movie movie, View rootView) {
            // Load Title
            TextView title = (TextView) rootView.findViewById(R.id.detail_movie_title);
            title.setText(movie.getTitle());
            // Load Poster or backdrop
            ImageView poster = (ImageView) rootView.findViewById(R.id.detail_movie_thumbnail);
            Picasso.with(getContext()).load(movie.getBackgroundImageUrl()).into(poster);
            // Load overview
            TextView overView = (TextView) rootView.findViewById(R.id.detail_movie_synopsis);
            overView.setText(movie.getSynopsis());
            // Load vote_average
            TextView voteAverage = (TextView) rootView.findViewById(R.id.detail_movie_user_rating);
            voteAverage.setText("Ratings: " + movie.getUserRating() + "/10.0 ");
            // Load release date
            TextView releaseDate = (TextView) rootView.findViewById(R.id.detail_movie_release_date);
            releaseDate.setText("Release Date: " + movie.getReleaseDate());
        }

    }


}
