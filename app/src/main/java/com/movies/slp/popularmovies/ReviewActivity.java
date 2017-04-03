package com.movies.slp.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.movies.slp.popularmovies.adaptor.ReviewAdaptor;
import com.movies.slp.popularmovies.data.MovieReview;
import com.movies.slp.popularmovies.utilities.MovieContants;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity implements MovieContants {

    private static final String REVIEW_TITLE = "Reviews";
    private List<MovieReview> reviews;
    private RecyclerView rvReviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        rvReviews = (RecyclerView) findViewById(R.id.rv_reviews);
        reviews = (ArrayList<MovieReview>) getIntent().getSerializableExtra(REVIEWS);
        this.setTitle(REVIEW_TITLE);
        Log.i("REviews",String.valueOf(reviews));

        if (null != reviews) {
            rvReviews.setAdapter(new ReviewAdaptor(reviews));
            rvReviews.setLayoutManager(new LinearLayoutManager(this));
            rvReviews.setHasFixedSize(true);
        }
    }
}
