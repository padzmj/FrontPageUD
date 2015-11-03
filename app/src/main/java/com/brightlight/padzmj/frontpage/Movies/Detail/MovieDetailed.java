package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightlight.padzmj.frontpage.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MovieDetailed extends AppCompatActivity {

    private String id, posterPath, movieTitle, releaseYear, synopsis, userRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title= (TextView) findViewById(R.id.detail_movie_title);
        TextView release = (TextView) findViewById(R.id.detail_release_year);
        TextView synop = (TextView) findViewById(R.id.detail_synopsis);
        TextView rating = (TextView) findViewById(R.id.detail_user_rating);
        ImageView poster = (ImageView) findViewById(R.id.detail_poster);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            id = bundle.getString("id");
            posterPath = bundle.getString("posterPath");
            movieTitle = bundle.getString("movieTitle");
            releaseYear = bundle.getString("releaseYear");
            synopsis = bundle.getString("synopsis");
            userRating = bundle.getString("userRating");
        }

        Glide.with(this).load(posterPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(poster);
        title.setText(movieTitle);
        release.setText(releaseYear);
        synop.setText(synopsis);
        rating.setText(userRating);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
