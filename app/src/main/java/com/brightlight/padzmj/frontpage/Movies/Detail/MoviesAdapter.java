package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightlight.padzmj.frontpage.MainActivity;
import com.brightlight.padzmj.frontpage.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PadzMJ on 23/10/2015.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Movie> movieList = new ArrayList<>();
    boolean two_pane, startup = true;

    public MoviesAdapter(Context context, List<Movie> moviesList, boolean two_pane){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.movieList = moviesList;
        this.two_pane = two_pane;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(!two_pane) view = layoutInflater.inflate(R.layout.custom_movie_list_item, parent, false);
        else view = layoutInflater.inflate(R.layout.custom_movie_list_item_two_pane, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        final String id = movieList.get(position).getId();
        final String posterPath = movieList.get(position).getPosterPath();
        final String backdropPath = movieList.get(position).getBackdropPath();
        final String title = movieList.get(position).getTitle();
        final String releaseYear = movieList.get(position).getReleaseYear();
        final String synopsis = movieList.get(position).getSynopsis();
        final String userRating = movieList.get(position).getUserRating();
        final String runtime = movieList.get(position).getRuntime();
        final boolean favourite = movieList.get(position).isFavouriteMovie();

        Glide.with(context).load(movieList.get(position).getPosterPath()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);

        if(!two_pane){
            holder.title.setText(title);
            holder.releaseYear.setText(releaseYear);

            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClicked(View v, int position) {
                    Intent movieIntent = new Intent(context, MovieDetailedActivity.class);
                    movieIntent.putExtra("position", position);
                    movieIntent.putExtra("id", id);
                    movieIntent.putExtra("posterPath", posterPath);
                    movieIntent.putExtra("backdropPath", backdropPath);
                    movieIntent.putExtra("movieTitle", title);
                    movieIntent.putExtra("releaseYear", releaseYear);
                    movieIntent.putExtra("synopsis", synopsis);
                    movieIntent.putExtra("userRating", userRating);
                    movieIntent.putExtra("runtime", runtime);
                    movieIntent.putExtra("favourite", favourite);
                    context.startActivity(movieIntent);
                }
            });
        }else{
            if(startup) startFragment(0); startup = false;

            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClicked(View v, int position) {
                    startFragment(position);
                }
            });
        }

    }
    private void startFragment(int position){
        MovieDetailedFragment movieDetailedFragment = new MovieDetailedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", movieList.get(position).getId());
        bundle.putString("posterPath", movieList.get(position).getPosterPath());
        bundle.putString("backdropPath", movieList.get(position).getBackdropPath());
        bundle.putString("movieTitle", movieList.get(position).getTitle());
        bundle.putString("releaseYear", movieList.get(position).getReleaseYear());
        bundle.putString("synopsis", movieList.get(position).getSynopsis());
        bundle.putString("userRating", movieList.get(position).getUserRating());
        bundle.putString("runtime", movieList.get(position).getRuntime());
        bundle.putBoolean("favourite", movieList.get(position).isFavouriteMovie());
        movieDetailedFragment.setArguments(bundle);

        MainActivity activity = (MainActivity) context;
        activity.startDetailFragment(R.id.fragment_master_detail, movieDetailedFragment);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface ItemClickListener{
        void onClicked(View v, int position);
    }

}
