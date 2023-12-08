package com.aadhil.cineworlddigital.model;

import android.net.Uri;

public class SliderImage {
    private final Uri uri;

    public SliderImage(Uri uri) {
        this.uri = uri;
    }

    public Uri getImageUri() {
        return uri;
    }
}
