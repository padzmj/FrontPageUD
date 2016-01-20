package com.brightlight.padzmj.frontpage.Movies.Trailers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightlight.padzmj.frontpage.Movies.Reviews.Review;
import com.brightlight.padzmj.frontpage.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PadzMJ on 10/01/2016.
 */
public class TrailersFragment extends Fragment {

    private static final String MOVIE_TRAILER = "movie_trailer";
    private static List<Trailer> trailerList = new ArrayList<>();
    private static ArrayList<Review> trailerParcelList = new ArrayList<>();

    private static Context context;

    public TrailersFragment() {
    }

    public static TrailersFragment newInstance(Context context, List<Trailer> trailers) {
        TrailersFragment trailersFragment = new TrailersFragment();

        TrailersFragment.context = context;
        TrailersFragment.trailerList = trailers;
        return trailersFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.movie_trailer_fragment_layout, container, false);

        RecyclerView trailersContainer = (RecyclerView) rootView.findViewById(R.id.trailersRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailersContainer.setLayoutManager(layoutManager);
        trailersContainer.setAdapter(new TrailersAdapter(getActivity(), trailerList));
        trailersContainer.setHasFixedSize(true);
        return trailersContainer;
    }
}