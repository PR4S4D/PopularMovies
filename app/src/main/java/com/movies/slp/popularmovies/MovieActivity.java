package com.movies.slp.popularmovies;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.slp.popularmovies.data.FavouriteMovieContract;
import com.movies.slp.popularmovies.data.MovieReview;
import com.movies.slp.popularmovies.utilities.Movie;
import com.movies.slp.popularmovies.utilities.MovieContants;
import com.movies.slp.popularmovies.utilities.MovieUtils;
import com.movies.slp.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements MovieContants, LoaderManager.LoaderCallbacks {

    public static final String REMOVED_FROM_FAVOURITES = "Removed from favourites";
    public static final String INSERT_SUCCESSFUL  = "Insert Successful";
    public static final String PERMISSION_NOT_GRANTED = "Permission not Granted";
    private static int movieId;
    private static Movie movie;
    @Bind(R.id.movie_poster)
    ImageView moviePoster;
    @Bind(R.id.movie_title)
    TextView title;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.synopsis)
    TextView synopsis;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind((R.id.favourite))
    FloatingActionButton favouriteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        movie = (Movie) getIntent().getParcelableExtra(MOVIE);
        movieId = movie.getId();
        this.setTitle(movie.getTitle());
        showMovieDetails(movie);

        if (NetworkUtils.isNetworkAvailable(this))
            getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER, null, this);
    }

    private void checkPermissions() {
        if (!isWritePermissionGranted())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    3);
    }

    private boolean isWritePermissionGranted() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.share) {
            List<VideoDetail> videoLinks = movie.getVideos();

            if (null != videoLinks) {
                shareTrailer(videoLinks.get(0).getVideoLink());
            } else {
                Toast.makeText(this, "Cant Share at the moment!", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);

    }

    private void shareTrailer(String link) {
        String mimeType = "text/plain";
        String title = "Share Trailer";

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(link)
                .startChooser();
    }

    protected void showMovieDetails(Movie movie) {
        Picasso.with(MovieActivity.this).load(movie.getPosterPath()).placeholder(R.drawable.loading).error(R.drawable.loading).into(moviePoster);
        title.setText(movie.getTitle());
        rating.setText(movie.getRating() + "/10");
        releaseDate.setText(movie.getReleaseDate());
        synopsis.setText(movie.getSynopsis());
        setFavouriteImage();
    }

    private void setFavouriteImage() {
        if (MovieUtils.isFavourite(this, movieId)) {
            favouriteButton.setImageResource(R.drawable.favourite);
        } else {
            favouriteButton.setImageResource(R.drawable.make_favourite);
        }
    }

    public void showVideos(View view) {
        final List<VideoDetail> videos = movie.getVideos();
        if(null == videos || videos.isEmpty()){
            Toast.makeText(this,"No Videos!",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MovieActivity.this, VideoActivity.class);
        Log.i("Movie id", String.valueOf(movieId));
        intent.putExtra(VIDEOS, (ArrayList<VideoDetail>) videos);
        startActivity(intent);
        overridePendingTransition(R.anim.in, R.anim.out);
    }

    public void showReviews(View view) {
        if(movie.getReviews().isEmpty()){
            Toast.makeText(this,"No Reviews!",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra(REVIEWS, (ArrayList<MovieReview>) movie.getReviews());
        startActivity(intent);
        overridePendingTransition(R.anim.in, R.anim.out);
    }

    public void addToFavourites(View view) {
        checkPermissions();

        if (isWritePermissionGranted()) {
            if (MovieUtils.isFavourite(this, movieId)) {
                Uri uri = FavouriteMovieContract.MovieEntry.CONTENT_URI;
                String[] args = new String[]{String.valueOf(movieId)};
                int id = getContentResolver().delete(uri, FavouriteMovieContract.MovieEntry._ID + "=?", args);
                if (id > 0) {
                    Toast.makeText(this, REMOVED_FROM_FAVOURITES, Toast.LENGTH_SHORT).show();
                }
            } else {
                MovieUtils.saveImage(movie, this);
                ContentValues movieContent = MovieUtils.getMovieContent(movie);
                Uri uri = getContentResolver().insert(FavouriteMovieContract.MovieEntry.CONTENT_URI, movieContent);
                if (uri == null) {
                    Log.i("Insert failed", movieContent.toString());
                } else {
                    Toast.makeText(this, INSERT_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(this, PERMISSION_NOT_GRANTED, Toast.LENGTH_SHORT).show();
        }
        setFavouriteImage();

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Object loadInBackground() {
                try {
                    URL videosUrl = MovieUtils.getUrl(movieId, VIDEOS);
                    URL reviewsUrl = MovieUtils.getUrl(movieId, REVIEWS);
                    String videosResponse = NetworkUtils.getResponseFromHttpUrl(videosUrl);
                    String reviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                    movie.setVideos(MovieUtils.getVideos(videosResponse));
                    movie.setReviews(MovieUtils.getReviews(reviewsResponse));
                    Log.i("movie review", movie.getReviews().toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
