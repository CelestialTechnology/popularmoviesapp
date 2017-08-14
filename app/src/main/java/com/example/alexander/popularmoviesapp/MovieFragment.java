package com.example.alexander.popularmoviesapp;


import android.content.Intent;
import android.os.Bundle;
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

import com.example.alexander.popularmoviesapp.jsondata.DownloadMovieJSONTask;
import com.example.alexander.popularmoviesapp.moviedata.DownloadedMovie;
import com.example.alexander.popularmoviesapp.moviedata.Movie;
import com.example.alexander.popularmoviesapp.moviedata.OnlineMovie;
import com.example.alexander.popularmoviesapp.utils.DbMovieUtil;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private DownloadMovieJSONTask downloadMovieJSONTask;
    private MovieGridAdapter mMovieAdapter;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private String LOG_TAG = MovieFragment.class.getSimpleName();


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
                                downloadMovieData("popular");
                                break;
                            case "Top Rated":
                                downloadMovieData("top_rated");
                                break;
                            case "Favorites":
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

        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        mMovieAdapter = new MovieGridAdapter(getActivity(), new ArrayList<Movie>());
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        Toast.makeText(getActivity(), "Favorites Selected!", Toast.LENGTH_SHORT).show();
    }

}
