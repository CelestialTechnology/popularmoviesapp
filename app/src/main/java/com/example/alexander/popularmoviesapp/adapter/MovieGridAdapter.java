package com.example.alexander.popularmoviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.alexander.popularmoviesapp.R;
import com.example.alexander.popularmoviesapp.moviedata.DownloadedMovie;
import com.example.alexander.popularmoviesapp.moviedata.Movie;
import com.example.alexander.popularmoviesapp.moviedata.OnlineMovie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Alexander on 6/17/2016.
 */
public class MovieGridAdapter extends ArrayAdapter<Movie> {

    private ArrayList<Movie> mMovies;

    public MovieGridAdapter(Context context, ArrayList<Movie> objects) {
        super(context, 0, objects);
        this.mMovies = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        MovieViewHolder movieViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie_poster, parent, false);
            movieViewHolder = new MovieViewHolder();
            movieViewHolder.posterThumbnail = (ImageView) convertView.findViewById(R.id.movie_poster_image);
            convertView.setTag(movieViewHolder);
        } else {
            movieViewHolder = (MovieViewHolder) convertView.getTag();
        }

        if (getItem(position) instanceof OnlineMovie) {
            Picasso.with(getContext()).load(((OnlineMovie) movie).getImageUrl()).into(movieViewHolder.posterThumbnail);
        } else if (getItem(position) instanceof DownloadedMovie) {
            movieViewHolder.posterThumbnail.setImageBitmap(((DownloadedMovie) movie).getPoster(getContext()));
        } else {
            throw new UnsupportedClassVersionError(
                    "Incorrect Version of Movie Class: " + getItem(position).getClass().toString());
        }

        return convertView;
    }

    private class MovieViewHolder {
        ImageView posterThumbnail;
    }
}
