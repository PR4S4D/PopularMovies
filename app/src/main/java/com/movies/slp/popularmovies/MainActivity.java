package com.movies.slp.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.movies.slp.popularmovies.adaptor.MovieAdapter;
import com.movies.slp.popularmovies.utilities.Movie;
import com.movies.slp.popularmovies.utilities.MovieContants;
import com.movies.slp.popularmovies.utilities.MovieUtils;
import com.movies.slp.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieContants, MovieAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<List<Movie>> {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    List<Movie> movies;
    public static String sortMode = FAVOURITE_MOVIES;
    LoaderManager loaderManager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getPreferences(MODE_PRIVATE);
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        progressBar = (ProgressBar) findViewById(R.id.loader);
        fetchMovies();


    }

    private void fetchMovies() {
        checkNetwork();
        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(MOVIE_LOADER, null, this);
    }

    private void checkNetwork() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            preferences.edit().putInt(SORT_MODE, R.id.favourite).apply();
            Toast.makeText(this, NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        checkNetwork();
        int currentSortMode = preferences.getInt(SORT_MODE, R.id.popular);
        if (currentSortMode != item.getItemId()) {
            preferences.edit().putInt(SORT_MODE, item.getItemId()).apply();
            loaderManager.restartLoader(MOVIE_LOADER, null, this);
        }
        return true;
    }


    private void initializeRecyclerView(List<Movie> movieList) {
        if (null != movieList) {
            rvMovies.setAdapter(new MovieAdapter(movieList, MainActivity.this));
                int gridSize = getResources().getInteger(R.integer.grid_size);
            rvMovies.setLayoutManager(new GridLayoutManager(this,gridSize));
            rvMovies.setHasFixedSize(true);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (null != movies) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MOVIE, movies.get(clickedItemIndex));
            startActivity(intent);
            overridePendingTransition(R.anim.in, R.anim.out);
        }
    }

    private void setMovies(List<Movie> movieList) {
        movies = movieList;
        initializeRecyclerView(movies);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<List<Movie>>(getApplicationContext()) {
            private List<Movie> movieList = null;

            @Override
            protected void onStartLoading() {

                if (null != movieList) {
                    deliverResult(movieList);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                List<Movie> movieResults = null;
                int sortMode = preferences.getInt(SORT_MODE, R.id.popular);
                if (sortMode == R.id.favourite) {
                    movieResults = MovieUtils.getFavouriteMovies(getApplicationContext());

                } else {
                    String urlPath = (sortMode == R.id.popular) ? POPULAR : TOP_RATED;
                    URL url = MovieUtils.getMoviesURL(urlPath);
                    String moviesDetail = null;
                    try {
                        moviesDetail = NetworkUtils.getResponseFromHttpUrl(url);
                        movieResults = MovieUtils.getMovieList(moviesDetail);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return movieResults;
            }

            @Override
            public void deliverResult(List<Movie> data) {
                movieList = data;
                super.deliverResult(data);

            }
        };


    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        progressBar.setVisibility(View.INVISIBLE);
        setMovies(data);

    }


    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        setMovies(null);
    }
}
