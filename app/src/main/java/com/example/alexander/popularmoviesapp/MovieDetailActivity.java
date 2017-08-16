package com.example.alexander.popularmoviesapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.popularmoviesapp.adapter.ReviewAdapter;
import com.example.alexander.popularmoviesapp.adapter.TrailerAdapter;
import com.example.alexander.popularmoviesapp.jsondata.DownloadReviewJSONTask;
import com.example.alexander.popularmoviesapp.jsondata.DownloadTrailerJSONTask;
import com.example.alexander.popularmoviesapp.moviedata.DownloadedMovie;
import com.example.alexander.popularmoviesapp.moviedata.Movie;
import com.example.alexander.popularmoviesapp.moviedata.OnlineMovie;
import com.example.alexander.popularmoviesapp.utils.DbMovieUtil;
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

    public static class MovieDetailFragment extends Fragment implements TrailerAdapter.TrailerListItemClickListener,
            ReviewAdapter.ReviewListItemClickListener {
        private String LOG_TAG = MovieDetailFragment.class.getSimpleName();
        private Movie movie;

        private TrailerAdapter mTrailerAdapter;
        private ReviewAdapter mReviewAdapter;
        private RecyclerView mReviewView;
        private RecyclerView mTrailerView;

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
                if (movie instanceof OnlineMovie) {
                    Log.i(LOG_TAG, "Online Movie!");
                } else if (movie instanceof DownloadedMovie) {
                    Log.i(LOG_TAG, "Downloaded Movie!");
                } else {
                    Log.i(LOG_TAG, "Correct Movie Class Not Found?");
                }
                Log.v(LOG_TAG, "MOVIE RECEIVED FOR DETAIL!!!");
                loadMovieDetails(movie, rootView);
            }

            return rootView;
        }

        // This helper method loads the movie data to the views in fragment_movie_detail
        public void loadMovieDetails(final Movie movie, View rootView) {

            // Load Title
            TextView title = (TextView) rootView.findViewById(R.id.detail_movie_title);
            title.setText(movie.getTitle());
            // Load Poster or backdrop
            ImageView poster = (ImageView) rootView.findViewById(R.id.detail_movie_thumbnail);
            final ImageView favoriteButton = (ImageView) rootView.findViewById(R.id.detail_favorite_selection);
            if (movie instanceof OnlineMovie) {
                Picasso.with(getContext()).load(((OnlineMovie) movie).getBackgroundImageUrl()).into(poster);
                favoriteButton.setImageResource(R.mipmap.ic_favorite_star_unselected);
                favoriteButton.setTag(R.mipmap.ic_favorite_star_unselected);
            } else if (movie instanceof DownloadedMovie) {
                poster.setImageBitmap(((DownloadedMovie) movie).getBackdrop(getActivity()));
                favoriteButton.setImageResource(R.mipmap.ic_favorite_star_selected);
                favoriteButton.setTag(R.mipmap.ic_favorite_star_selected);
            }

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AsyncTask<Void, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            return new Boolean(DbMovieUtil.changeFavoriteMovieState(getActivity(), movie));
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            if (aBoolean.booleanValue()) {
                                switch (((Integer) v.getTag()).intValue()) {
                                    case R.mipmap.ic_favorite_star_selected:
                                        favoriteButton.setImageResource(R.mipmap.ic_favorite_star_unselected);
                                        favoriteButton.setTag(R.mipmap.ic_favorite_star_unselected);
                                        break;
                                    case R.mipmap.ic_favorite_star_unselected:
                                        favoriteButton.setImageResource(R.mipmap.ic_favorite_star_selected);
                                        favoriteButton.setTag(R.mipmap.ic_favorite_star_selected);
                                        break;
                                }
                            }
                        }
                    }.execute();
                }
            });

            // Load overview
            TextView overView = (TextView) rootView.findViewById(R.id.detail_movie_synopsis);
            overView.setText(movie.getSynopsis());
            // Load vote_average
            TextView voteAverage = (TextView) rootView.findViewById(R.id.detail_movie_user_rating);
            voteAverage.setText("Ratings: " + movie.getUserRating() + "/10.0 ");
            // Load release date
            TextView releaseDate = (TextView) rootView.findViewById(R.id.detail_movie_release_date);
            releaseDate.setText("Release Date: " + movie.getReleaseDate());

            // This is only done if there is network connectivity
            if (isConnectedToWifi()) {
                loadTrailerDetails(rootView);
                loadReviewDetails(rootView);
            } else {
                Toast.makeText(getActivity(), "Not connected to Network. Trailers and Reviews not loaded.", Toast.LENGTH_LONG).show();
            }
        }

        private boolean isConnectedToWifi() {
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            boolean isConnectedToNetwork = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if (isConnectedToNetwork) {
                    return true;
            }
            return false;
        }

        private void loadReviewDetails(View rootView) {
            mReviewView = (RecyclerView) rootView.findViewById(R.id.rv_reviews);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mReviewView.setLayoutManager(layoutManager);
            mReviewAdapter = new ReviewAdapter(this);
            new DownloadReviewJSONTask(mReviewView, mReviewAdapter).execute(movie.getId());
        }

        private void loadTrailerDetails(View rootView) {
            mTrailerView = (RecyclerView) rootView.findViewById(R.id.rv_trailers);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mTrailerView.setLayoutManager(layoutManager);
            mTrailerAdapter = new TrailerAdapter(this);
            new DownloadTrailerJSONTask(mTrailerView, mTrailerAdapter).execute(movie.getId());
        }

        @Override
        public void onTrailerItemClick(int clickedTrailerIndex) {
            String videoKey = mTrailerAdapter.getTrailers().get(clickedTrailerIndex).getUrlKey();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + videoKey));
            startActivity(intent);
        }

        @Override
        public void onReviewItemClick(int clickedReviewIndex) {
            // TODO: Find an idea for the reviews
        }
    }


}
