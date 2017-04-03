package com.movies.slp.popularmovies.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import com.movies.slp.popularmovies.VideoDetail;
import com.movies.slp.popularmovies.data.MovieReview;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {


    private int id;
    private String title;
    private String posterPath;
    private String synopsis;

    private double rating;

    private String releaseDate;
    private List<VideoDetail> videos;
    private List<MovieReview> reviews;

    public Movie() {

    }

    public Movie(int id, String title, String posterPath, String synopsis, double rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<VideoDetail> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDetail> videos) {
        this.videos = videos;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }



    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        synopsis = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        if (in.readByte() == 0x01) {
            videos = new ArrayList<VideoDetail>();
            in.readList(videos, VideoDetail.class.getClassLoader());
        } else {
            videos = null;
        }
        if (in.readByte() == 0x01) {
            reviews = new ArrayList<MovieReview>();
            in.readList(reviews, MovieReview.class.getClassLoader());
        } else {
            reviews = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(synopsis);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);
        if (videos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(videos);
        }
        if (reviews == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reviews);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

