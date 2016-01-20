package com.brightlight.padzmj.frontpage.Movies.Reviews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.brightlight.padzmj.frontpage.R;

public class ReviewActivity extends AppCompatActivity {

    String author, content;
    Bundle bundle;

    TextView authorTV, contentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        authorTV = (TextView) findViewById(R.id.main_review_author);
        contentTV = (TextView) findViewById(R.id.main_review_content);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            author = bundle.getString("author");
            content = bundle.getString("content");
        }

        authorTV.setText(author);
        contentTV.setText(content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
