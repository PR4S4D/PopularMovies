package com.movies.slp.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.movies.slp.popularmovies.VideoDetail;
import com.movies.slp.popularmovies.data.FavouriteMovieContract;
import com.movies.slp.popularmovies.data.MovieReview;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieUtils implements MovieContants {

    public static URL getMoviesURL(String sortMode) {
        Uri.Builder builder = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(sortMode).appendQueryParameter(API_KEY, MY_API_KEY);
        URL moviesUrl = null;
        try {
            moviesUrl = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return moviesUrl;
    }

    public static List<Movie> getMovieList(String moviesDetail) {
        List<Movie> movieList = new ArrayList<Movie>();
        try {
             JSONObject json = new JSONObject(moviesDetail);
            JSONArray movieArray = new JSONArray(json.getString(RESULTS));
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieJson = (JSONObject) movieArray.get(i);
                Movie movie = new Movie();
                movie.setId(movieJson.getInt(ID));
                movie.setTitle(movieJson.getString(ORIGINAL_TITLE));
                movie.setPosterPath(getPosterPath(movieJson.getString(POSTER_PATH)));
                movie.setSynopsis(movieJson.getString(OVERVIEW));
                movie.setRating(movieJson.getDouble(VOTE_AVERAGE));
                movie.setReleaseDate(movieJson.getString(RELEASE_DATE));
                movieList.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

/*
    public static List<Movie> getMovieList(URL url, Context context){
        List<Movie> movieList;
        NetworkUtils.getResponse(url.toString(), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                movieList =  getMovieList(result);
            }
        });

*/




    public static URL getUrl(int id, String pathLabel) {
        String movieId = String.valueOf(id);
        Uri.Builder builder = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(movieId).appendPath(pathLabel).appendQueryParameter(API_KEY, MY_API_KEY);
        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String getPosterPath(String imageUrl) {
        return POSTER_BASE_URL + W342 + imageUrl;
    }

    public static List<String> getVideoLinks(String response) throws JSONException {
        List<String> videoLinks = new ArrayList<String>();
        JSONObject videoJson = null;
        JSONObject json = new JSONObject(response);
        JSONArray videos = json.getJSONArray(RESULTS);
        for (int i = 0; i < videos.length(); i++) {
            videoJson = (JSONObject) videos.get(i);
            videoLinks.add(getYoutubeLink(videoJson.getString(KEY)));
        }

        return videoLinks;

    }
    public static List<VideoDetail> getVideos(String response) throws JSONException {
        List<VideoDetail> videos = new ArrayList<VideoDetail>();
        JSONObject videoJson = null;
        JSONObject json = new JSONObject(response);
        JSONArray videosArray = json.getJSONArray(RESULTS);
        for (int i = 0; i < videosArray.length(); i++) {
            videoJson = (JSONObject) videosArray.get(i);
            videos.add(new VideoDetail(getYoutubeLink(videoJson.getString(KEY)),videoJson.getString(MovieContants.NAME)));
        }

        return videos;
    }

    private static String getYoutubeLink(String string) {
        return MovieContants.YOUTUBE_BASE_URL + string;
    }

    public static List<MovieReview> getReviews(String reviewsResponse) throws JSONException {
        List<MovieReview> movieReviews = new ArrayList<MovieReview>();
        JSONObject reviewJson = null;
        String author = null;
        String content = null;
        JSONObject json = new JSONObject(reviewsResponse);
        JSONArray reviews = json.getJSONArray(RESULTS);
        for (int i = 0; i < reviews.length(); i++) {
            reviewJson = (JSONObject) reviews.get(i);
            author = reviewJson.getString(AUTHOR);
            content = reviewJson.getString(CONTENT);
            movieReviews.add(new MovieReview(author, content));
        }

        return movieReviews;
    }

    public static ContentValues getMovieContent(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(FavouriteMovieContract.MovieEntry._ID, movie.getId());
        cv.put(FavouriteMovieContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
        cv.put(FavouriteMovieContract.MovieEntry.RELEASE_DATE, movie.getReleaseDate());
        cv.put(FavouriteMovieContract.MovieEntry.RATING, movie.getRating());
        cv.put(FavouriteMovieContract.MovieEntry.SYNOPSIS, movie.getSynopsis());
        cv.put(FavouriteMovieContract.MovieEntry.POSTER,LOCAL_IMPAGE_BASE_URL+movie.getId()+JPG_EXTENSION);
        return cv;

    }

    public static boolean isFavourite(Context context,int movieId) {
        String[] selectionArgs = new String[]{String.valueOf(movieId)};
        Cursor cursor = context.getContentResolver().query(FavouriteMovieContract.MovieEntry.CONTENT_URI,null, FavouriteMovieContract.MovieEntry._ID+"=?",selectionArgs, FavouriteMovieContract.MovieEntry.MOVIE_TITLE);
        Log.i("cursor count",String.valueOf(cursor.getCount()));
        if(cursor.getCount() >= 1)
            return true;
        return false;
    }
    public static List<Movie> getFavouriteMovies(Context context) {
        List<Movie> movies = new ArrayList<Movie>();

        Cursor cursor = context.getContentResolver().query(FavouriteMovieContract.MovieEntry.CONTENT_URI, null, null, null, FavouriteMovieContract.MovieEntry.MOVIE_TITLE);
        int id = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry._ID);
        int title = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.MOVIE_TITLE);
        int rating = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.RATING);
        int releaseDate = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.RELEASE_DATE);
        int synopsis = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.SYNOPSIS);
        int poster = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.POSTER);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getInt(id), cursor.getString(title), cursor.getString(poster), cursor.getString(synopsis), cursor.getDouble(rating), cursor.getString(releaseDate));
                Log.i("Movie", movie.toString());
                movies.add(movie);
            } while (cursor.moveToNext());

        }
        return movies;
    }

    public static void saveImage(final Movie movie, final Context context) {
        Picasso.with(context)
                .load(movie.getPosterPath())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                File file = new File(
                                        Environment.getExternalStorageDirectory().getPath()+"/"+
                                                +movie.getId()+JPG_EXTENSION);
                                try {
                                    file.createNewFile();
                                    FileOutputStream ostream = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,ostream);
                                    ostream.close();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                }
                );
    }

}
