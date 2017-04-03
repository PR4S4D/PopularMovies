package com.movies.slp.popularmovies;

import java.io.Serializable;

public class  VideoDetail implements Serializable{
    private String videoLink;
    private String videoDescription;

    public VideoDetail(String videoLink, String videoDescription) {
        this.videoLink = videoLink;
        this.videoDescription = videoDescription;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }
}
