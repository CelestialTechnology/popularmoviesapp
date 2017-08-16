package com.example.alexander.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.popularmoviesapp.R;
import com.example.alexander.popularmoviesapp.moviedata.Trailer;

import java.util.ArrayList;

/**
 * Created by awest on 8/14/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private ArrayList<Trailer> trailers;
    final private TrailerListItemClickListener mTrailerListItemClickListener;

    public TrailerAdapter(TrailerListItemClickListener clickListener) {
        mTrailerListItemClickListener = clickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutItemId = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutItemId, parent, shouldAttachToParentImmediately);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);

        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(trailers.get(position));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public interface TrailerListItemClickListener {
        void onTrailerItemClick(int clickedTrailerIndex);
    }

    public void loadTrailers(ArrayList<Trailer> newTrailers) {
        trailers = newTrailers;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView trailerName;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView) itemView.findViewById(R.id.textViewName);
            itemView.setOnClickListener(this);
        }

        void bind(Trailer trailer) {
            trailerName.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            int clickedTrailer = getAdapterPosition();
            mTrailerListItemClickListener.onTrailerItemClick(clickedTrailer);
        }
    }
}
