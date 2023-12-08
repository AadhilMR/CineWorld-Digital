package com.aadhil.cineworlddigital.model;

import android.net.Uri;

public class UpcomingMovie {
    private Uri imageId;
    private String movieName;
    private String releaseDate;

    public UpcomingMovie() {}

    public Uri getImageId() {
        return imageId;
    }

    public UpcomingMovie setImageId(Uri imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getMovieName() {
        return movieName;
    }

    public UpcomingMovie setMovieName(String movieName) {
        this.movieName = movieName;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public UpcomingMovie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }
}
