package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightlight.padzmj.frontpage.Model.MySQL.MovieContract.MovieEntry;
import com.brightlight.padzmj.frontpage.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PadzMJ on 23/10/2015.
 */
public class MovieFragment extends Fragment {
    private static List<Movie> movieList = new ArrayList<>();
    ArrayList<String> movieTitles = new ArrayList<>();
    static boolean two_pane;

    public static MovieFragment newInstance(Context context, List<Movie> movieList, boolean pane){
        MovieFragment.movieList = movieList;
        MovieFragment.two_pane = pane;
        return new MovieFragment();
    }

    public MovieFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getDataFromDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //movieTitles = getArguments().getStringArrayList("movieTitles");

        View rootView = inflater.inflate(R.layout.movie_fragment_layout, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.movieListRecyclerView);

        recyclerView.setAdapter(new MoviesAdapter(getActivity(),movieList, two_pane));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callback) getActivity()).onSelectedListener();
            }
        });
        return rootView;
    }

    private void getDataFromDB() {
        Uri movieUri = MovieEntry.buildMovie();
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(movieUri, null, null, null, null);

        //Cursor update = getActivity().getApplicationContext().getContentResolver().update(movieUri, )

        if (cursor != null) {
            //if (cursor.moveToFirst()){
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    Movie movie = new Movie();
                    movie.setTitle(cursor.getString(4));
                    movieList.add(movie);
                }
            }


            //}
        }
    }

    interface Callback {
        void onSelectedListener();
    }

}
