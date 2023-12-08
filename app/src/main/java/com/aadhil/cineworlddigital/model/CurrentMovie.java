package com.aadhil.cineworlddigital.model;

import android.net.Uri;

public class CurrentMovie {
    private String documentId;
    private Uri imageId;
    private String videoId;
    private String movieName;
    private String duration;
    private String language;

    public CurrentMovie() {}

    public String getDocumentId() {
        return documentId;
    }

    public CurrentMovie setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public Uri getImageId() {
        return imageId;
    }

    public CurrentMovie setImageId(Uri imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getVideoId() {
        return videoId;
    }

    public CurrentMovie setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public String getMovieName() {
        return movieName;
    }

    public CurrentMovie setMovieName(String movieName) {
        this.movieName = movieName;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public CurrentMovie setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public CurrentMovie setLanguage(String language) {
        this.language = language;
        return this;
    }
}
