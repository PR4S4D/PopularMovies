package com.movies.slp.popularmovies.data;

import android.os.Parcel;

import java.io.Serializable;


public class MovieReview implements Serializable {
    private String author;
    private String content;

    public MovieReview(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    protected MovieReview(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

}
