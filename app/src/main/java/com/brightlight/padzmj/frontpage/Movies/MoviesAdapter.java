package com.brightlight.padzmj.frontpage.Movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightlight.padzmj.frontpage.Movies.Detail.MovieDetailed;
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
    private ArrayList<String> movieTitles = new ArrayList<>();

    public MoviesAdapter(Context context, List<Movie> moviesList){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.movieList = moviesList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {

        final String id = movieList.get(position).getID();
        final String posterPath = movieList.get(position).getPosterPath();
        final String title = movieList.get(position).getTitle();
        final String releaseYear = movieList.get(position).getReleaseYear();
        final String synopsis = movieList.get(position).getSynopsis();
        final String userRating = movieList.get(position).getUserRating();


        holder.title.setText(title);
        holder.releaseYear.setText(releaseYear);

        Glide.with(context).load(movieList.get(position).getPosterPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClicked(View v, int position) {
                Intent movieIntent = new Intent(context, MovieDetailed.class);
                movieIntent.putExtra("id", id);
                movieIntent.putExtra("posterPath", posterPath);
                movieIntent.putExtra("movieTitle", title);
                movieIntent.putExtra("releaseYear", releaseYear);
                movieIntent.putExtra("synopsis", synopsis);
                movieIntent.putExtra("userRating", userRating);
                context.startActivity(movieIntent);

                //Toast.makeText(v.getContext(), holder.title.getText(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface ItemClickListener{
        void onClicked(View v, int position);
    }
}
