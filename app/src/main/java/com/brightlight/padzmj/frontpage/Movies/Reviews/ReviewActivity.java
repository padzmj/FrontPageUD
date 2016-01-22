package com.brightlight.padzmj.frontpage.Movies.Reviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        authorTV = (TextView) findViewById(R.id.main_review_author);
        contentTV = (TextView) findViewById(R.id.main_review_content);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            author = bundle.getString("author");
            content = bundle.getString("content");
        }

        authorTV.setText(author);
        contentTV.setText(content);

    }



}
