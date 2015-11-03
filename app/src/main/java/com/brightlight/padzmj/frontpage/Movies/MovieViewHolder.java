package com.brightlight.padzmj.frontpage.Movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightlight.padzmj.frontpage.R;

/**
 * Created by PadzMJ on 23/10/2015.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView thumbnail;
    TextView title, releaseYear, synopsis, userRating;
    MoviesAdapter.ItemClickListener clickListener;

    public MovieViewHolder(View itemView) {
        super(itemView);

        thumbnail = (ImageView) itemView.findViewById(R.id.poster);
        title = (TextView) itemView.findViewById(R.id.title);
        releaseYear = (TextView) itemView.findViewById(R.id.release_year);
        itemView.setOnClickListener(this);
    }

    public void setClickListener(MoviesAdapter.ItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClicked(v, getLayoutPosition());
    }
}
