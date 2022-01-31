package com.app.swagliv.model.profile.pojo;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PersonalImages {

    private Uri imagesURI;

    private String url;

    public Uri getImagesURI() {
        return imagesURI;
    }

    public void setImagesURI(Uri imagesURI) {
        this.imagesURI = imagesURI;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
