package com.movies.slp.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavouriteMovieContract {

    public static final String CONTENT_AUTHORITY = "com.movies.slp.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_FAVOURITE_MOVIE = "favouriteMovie";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIE).build();
        public static final String TABLE_NAME = "favourite_movies";
        public static final String MOVIE_TITLE = "title";
        public static final String POSTER = "poster";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String SYNOPSIS = "synopsis";
        public static final String RATING = "rating";
    }

}
