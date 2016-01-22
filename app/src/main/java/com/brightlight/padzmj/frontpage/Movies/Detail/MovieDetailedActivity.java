package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.brightlight.padzmj.frontpage.R;


public class MovieDetailedActivity extends AppCompatActivity {

    String movieID, posterPath, backdropPath, movieTitle, releaseYear, movieSynopsis, userRating, runTime;
    boolean favourite;
    Context context;
    Movie movie;

    final static String LIFECYCLE = "LIFE_CYCLE";

    boolean twoPane;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detailed);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_detail);
//        setSupportActionBar(toolbar);



        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        MovieDetailedFragment movieDetailedFragment = MovieDetailedFragment.newInstance(this);
        movie = null;

        context = this;

        if(savedInstanceState==null){
            bundle = getIntent().getExtras();
            if (bundle != null) {
                movieID = bundle.getString("id");
                posterPath = bundle.getString("posterPath");
                backdropPath = bundle.getString("backdropPath");
                movieTitle = bundle.getString("movieTitle");
                releaseYear = bundle.getString("releaseYear");
                this.movieSynopsis = bundle.getString("synopsis");
                userRating = bundle.getString("userRating");
                runTime = bundle.getString("runtime");
                favourite = bundle.getBoolean("favourite");
                movieDetailedFragment.setArguments(bundle);
            }
        }else movieDetailedFragment.setArguments(savedInstanceState);

        fragmentTransaction.replace(R.id.fragment_master_detail, movieDetailedFragment);
        fragmentTransaction.commit();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", movieID);
        outState.putString("posterPath", posterPath);
        outState.putString("backdropPath", posterPath);
        outState.putString("movieTitle", movieTitle);
        outState.putString("releaseYear", releaseYear);
        outState.putString("synopsis", movieSynopsis);
        outState.putString("userRating", userRating);
        outState.putString("runtime", runTime);
        outState.putBoolean("favourite", favourite);

        Log.i("Saved", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieID = savedInstanceState.getString("id");
        posterPath = savedInstanceState.getString("posterPath");
        backdropPath = savedInstanceState.getString("backdropPath");
        movieTitle = savedInstanceState.getString("movieTitle");
        releaseYear = savedInstanceState.getString("releaseYear");
        this.movieSynopsis = savedInstanceState.getString("synopsis");
        userRating = savedInstanceState.getString("userRating");
        runTime = savedInstanceState.getString("runtime");
        favourite = savedInstanceState.getBoolean("favourite");
        Log.i("SavedIn", "RestoreInstance");
    }
}