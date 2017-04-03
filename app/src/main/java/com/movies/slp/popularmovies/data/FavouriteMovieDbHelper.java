package com.movies.slp.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouriteMovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favoouriteMovies.db";
    public static final int DATABASE_VERSION = 2;

    public FavouriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITE_MOVIES_TABLE =
                "CREATE TABLE " + FavouriteMovieContract.MovieEntry.TABLE_NAME + " (" +
                        FavouriteMovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                        FavouriteMovieContract.MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                        FavouriteMovieContract.MovieEntry.POSTER + " TEXT NULL, " +
                        FavouriteMovieContract.MovieEntry.RELEASE_DATE + " TEXT  NULL, " +
                        FavouriteMovieContract.MovieEntry.RATING + " REAL NULL, " +
                        FavouriteMovieContract.MovieEntry.SYNOPSIS + " TEXT NULL); ";

        db.execSQL(SQL_CREATE_FAVOURITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
