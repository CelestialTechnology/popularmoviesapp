package com.example.alexander.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie_poster, parent, false);
        }

        ImageView posterThumbnail = (ImageView) convertView.findViewById(R.id.movie_poster_image);
        Picasso.with(getContext()).load(movie.getImageUrl()).into(posterThumbnail);

        return convertView;
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }
}
