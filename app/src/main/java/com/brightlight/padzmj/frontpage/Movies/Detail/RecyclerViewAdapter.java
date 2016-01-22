package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightlight.padzmj.frontpage.Movies.Reviews.Review;
import com.brightlight.padzmj.frontpage.Movies.Reviews.ReviewActivity;
import com.brightlight.padzmj.frontpage.Movies.Reviews.ReviewViewHolder;
import com.brightlight.padzmj.frontpage.Movies.Trailers.Trailer;
import com.brightlight.padzmj.frontpage.Movies.Trailers.TrailerViewHolder;
import com.brightlight.padzmj.frontpage.Movies.Trailers.TrailersAdapter;
import com.brightlight.padzmj.frontpage.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by PadzMJ on 20/01/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TRAILER = 0, REVIEW = 1;

    private Context context;
    private ArrayList<Object> trailersAndReviews;

    public RecyclerViewAdapter (Context context, ArrayList<Object> trailersAndReviews){
        this.context = context;
        this.trailersAndReviews = trailersAndReviews;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case TRAILER:
                View trailerView = layoutInflater.inflate(R.layout.custom_trailers_list_item, parent, false);
                viewHolder = new TrailerViewHolder(trailerView);
                break;
            case REVIEW:
                View reviewView = layoutInflater.inflate(R.layout.custom_reviews_list_item, parent, false);
                viewHolder = new ReviewViewHolder(reviewView);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){

            case TRAILER:
            TrailerViewHolder trailerViewHolder = (TrailerViewHolder) holder;
            configureTrailers(trailerViewHolder, position);
            break;

            case REVIEW:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                configureReviews(reviewViewHolder, position);
                break;

        }
    }

    private void configureReviews(ReviewViewHolder holder, int position){

        Review review = (Review) trailersAndReviews.get(position);
        final String author, content;

        if(review!=null){
            author = review.getAuthor();
            content =  review.getContent();

            holder.author.setText(author);
            holder.content.setText(content);

            holder.setClickListener(new MoviesAdapter.ItemClickListener() {
                @Override
                public void onClicked(View v, int position) {
                    Intent reviewIntent = new Intent(context, ReviewActivity.class);
                    reviewIntent.putExtra("author", author);
                    reviewIntent.putExtra("content", content);
                    context.startActivity(reviewIntent);
                }
            });
        }

    }

    private void configureTrailers(TrailerViewHolder holder, int position){

        Trailer trailer = (Trailer) trailersAndReviews.get(position);

        if(trailer!=null){
            final String url = trailer.getTrailerURL();
            String thumnail = String.format("http://img.youtube.com/vi/%1$s/0.jpg", trailer.getTrailerKey());

            Glide.with(context).load(thumnail).centerCrop().into(holder.thumbnail);

            final int pos = position + 1;

            //holder.trailerThumbnailPos.setText("Trailer " + pos);

            holder.setClickListener(new TrailersAdapter.ItemClickListener() {
                @Override
                public void onClicked(View v, int position) {
                    Intent trailerVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(trailerVideoIntent);
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return trailersAndReviews.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(trailersAndReviews.get(position) instanceof Review) return REVIEW;
        else if(trailersAndReviews.get(position) instanceof Trailer) return TRAILER;
        return -1;
    }
}
