package com.brightlight.padzmj.frontpage.Movies.Trailers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightlight.padzmj.frontpage.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by PadzMJ on 10/01/2016.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    LayoutInflater layoutInflater;
    private Context context;
    private List<Trailer> trailerList;


    public TrailersAdapter(Context context, List<Trailer> trailerList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_trailers_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        final String url = trailerList.get(position).getTrailerURL();
        String thumnail = String.format("http://img.youtube.com/vi/%1$s/0.jpg", trailerList.get(position).getTrailerKey());

        Glide.with(context).load(thumnail).centerCrop().into(holder.thumbnail);

        final int pos = position + 1;

        holder.trailerThumbnailPos.setText("Trailer " + pos);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClicked(View v, int position) {
                Intent trailerVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(trailerVideoIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public interface ItemClickListener {
        void onClicked(View v, int position);
    }
}
