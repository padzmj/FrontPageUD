package com.brightlight.padzmj.frontpage.Movies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    ArrayList<String> movieTitles = new ArrayList<>();

    private static List<Movie> movieList = new ArrayList<>();

    public static MovieFragment newInstance(Context context, List<Movie> movieList){
        MovieFragment.movieList = movieList;
        return new MovieFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //movieTitles = getArguments().getStringArrayList("movieTitles");

        View rootView = inflater.inflate(R.layout.movie_fragment_layout, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        //Toast.makeText(getActivity(), movieList.size() + "", Toast.LENGTH_LONG).show();

        recyclerView.setAdapter(new MoviesAdapter(getActivity(),movieList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }
}
