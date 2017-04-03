package com.movies.slp.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class FavouriteMovieProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    FavouriteMovieDbHelper dbHelper;
    UriMatcher matcher = buildUriMatcher();


    private UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouriteMovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FavouriteMovieContract.PATH_FAVOURITE_MOVIE, CODE_MOVIES);
        matcher.addURI(authority, FavouriteMovieContract.PATH_FAVOURITE_MOVIE + "/#", CODE_MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FavouriteMovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (matcher.match(uri)) {
            case CODE_MOVIES:
                cursor = dbHelper.getReadableDatabase().query(FavouriteMovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Uri:" + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri = null;
        switch (matcher.match(uri)) {
            case CODE_MOVIES:
                long id = dbHelper.getWritableDatabase().insert(FavouriteMovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(uri, id);
                } else {
                    throw new SQLException("DB insert failed" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Invalid Uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case CODE_MOVIES:
                return dbHelper.getWritableDatabase().delete(FavouriteMovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

            default:
                throw new UnsupportedOperationException("Invalid Uri:" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
