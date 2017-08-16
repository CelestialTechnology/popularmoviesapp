package com.example.alexander.popularmoviesapp;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alexander.popularmoviesapp.adapter.MovieGridAdapter;
import com.example.alexander.popularmoviesapp.jsondata.DownloadMovieJSONTask;
import com.example.alexander.popularmoviesapp.jsondata.LoadFavoriteMoviesTask;
import com.example.alexander.popularmoviesapp.moviedata.DownloadedMovie;
import com.example.alexander.popularmoviesapp.moviedata.Movie;
import com.example.alexander.popularmoviesapp.moviedata.OnlineMovie;
import com.example.alexander.popularmoviesapp.utils.DbMovieUtil;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private DownloadMovieJSONTask downloadMovieJSONTask;
    private LoadFavoriteMoviesTask loadFavoriteMoviesTask;
    private MovieGridAdapter mMovieAdapter;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String LOG_TAG = MovieFragment.class.getSimpleName();
    private GridView mMovieGrid;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.movie_sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);

        if (spinner == null) {
            Log.v(LOG_TAG, "Spinner not found!");
        } else {
            Log.v(LOG_TAG, "Spinner Found!");

            if (adapter != null) {
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (parent.getItemAtPosition(position).toString()) {
                            case "Most Popular":
                                if (isConnectedToWifi()) {
                                    downloadMovieData("popular");
                                } else {
                                    spinner.setSelection(2);
                                }
                                break;
                            case "Top Rated":
                                if (isConnectedToWifi()) {
                                    downloadMovieData("top_rated");
                                } else {
                                    spinner.setSelection(2);
                                }
                                break;
                            case "Favorites":
                                if (!isConnectedToWifi()) {
                                    Toast.makeText(getActivity(), "No connection to network, Showing Favorites Only", Toast.LENGTH_LONG).show();
                                }

                                loadFavoriteMovieData();
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
        mMovieGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        mMovieAdapter = new MovieGridAdapter(getActivity(), new ArrayList<Movie>());
        mMovieGrid.setAdapter(mMovieAdapter);

        mMovieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent displayMovieDetail = new Intent(getActivity(), MovieDetailActivity.class);

                if (DbMovieUtil.isFavoriteMovie(getActivity(), mMovieAdapter.getItem(position).getId())) {
                    DownloadedMovie downloadedMovie = (DownloadedMovie) mMovieAdapter.getItem(position);
                    displayMovieDetail.putExtra("Movie", downloadedMovie);
                } else {
                    OnlineMovie onlineMovie = (OnlineMovie) mMovieAdapter.getItem(position);
                    displayMovieDetail.putExtra("Movie", onlineMovie);
                }

                startActivity(displayMovieDetail);
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("GridPosition", mMovieGrid.getLastVisiblePosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            int previousGridPosition = savedInstanceState.getInt("GridPosition");
            mMovieGrid.setSelection(previousGridPosition);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void downloadMovieData(String sortCriteria) {
        downloadMovieJSONTask = new DownloadMovieJSONTask(mMovieAdapter, getActivity());
        downloadMovieJSONTask.execute(sortCriteria);
    }

    public void loadFavoriteMovieData() {
        loadFavoriteMoviesTask = new LoadFavoriteMoviesTask(mMovieAdapter, getActivity());
        loadFavoriteMoviesTask.execute();
    }

}
