package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightlight.padzmj.frontpage.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PadzMJ on 23/10/2015.
 */
public class MovieFragment extends Fragment {
    private static List<Movie> movieList = new ArrayList<>();
    static boolean two_pane;
    private static Context context;

    public static MovieFragment newInstance(Context context, List<Movie> movieList, boolean pane){
        MovieFragment.context = context;
        MovieFragment.movieList = movieList;
        MovieFragment.two_pane = pane;
        return new MovieFragment();
    }

    public MovieFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.movie_fragment_layout, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.movieListRecyclerView);
        recyclerView.setAdapter(new MoviesAdapter(getActivity(), movieList, two_pane));

        if(!two_pane) recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else  recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        return rootView;
    }

}
