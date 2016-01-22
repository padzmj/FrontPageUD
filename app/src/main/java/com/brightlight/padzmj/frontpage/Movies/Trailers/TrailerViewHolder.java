package com.brightlight.padzmj.frontpage.Movies.Trailers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightlight.padzmj.frontpage.R;

/**
 * Created by PadzMJ on 10/01/2016.
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView trailerThumbnailPos;
    public ImageView thumbnail;
    public TrailersAdapter.ItemClickListener clickListener;

    public TrailerViewHolder(View itemView) {
        super(itemView);

        thumbnail = (ImageView) itemView.findViewById(R.id.trailerThumbnail);
        //trailerThumbnailPos = (TextView) itemView.findViewById(R.id.trailerThumbnailPosition);
        itemView.setOnClickListener(this);
    }

    public void setClickListener(TrailersAdapter.ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClicked(v, getLayoutPosition());
    }
}
