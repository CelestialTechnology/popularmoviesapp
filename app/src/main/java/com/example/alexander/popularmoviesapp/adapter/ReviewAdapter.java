package com.example.alexander.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.popularmoviesapp.R;
import com.example.alexander.popularmoviesapp.moviedata.Review;

import java.util.ArrayList;

/**
 * Created by awest on 8/15/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> reviews;
    final private ReviewListItemClickListener mReviewListItemClickListener;

    public ReviewAdapter(ReviewListItemClickListener clickListener) {
        mReviewListItemClickListener = clickListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItemId = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutItemId, parent, shouldAttachToParentImmediately);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    public void loadReviews(ArrayList<Review> newReviews) {
        this.reviews = newReviews;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public interface ReviewListItemClickListener {
        void onReviewItemClick(int clickedReviewIndex);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewAuthor;
        private TextView textViewContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = (TextView) itemView.findViewById(R.id.textViewAuthor);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewContent);
        }

        void bind(Review review) {
            textViewAuthor.setText(review.getAuthor());
            textViewContent.setText(review.getContent());
        }

        @Override
        public void onClick(View v) {
            int clickedReview = getAdapterPosition();
            mReviewListItemClickListener.onReviewItemClick(clickedReview);
        }
    }
}
