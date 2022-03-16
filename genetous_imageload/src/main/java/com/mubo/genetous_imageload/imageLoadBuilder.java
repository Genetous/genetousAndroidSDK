package com.mubo.genetous_imageload;

import android.app.Activity;
import android.util.Size;
import android.widget.ImageView;

public class imageLoadBuilder {
    private Activity activity;
    private int placeholder;
    private Size requiredSize;
    private ImageView imageView;
    private String imageUrl;

    public imageLoadBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public imageLoadBuilder setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public imageLoadBuilder setRequiredSize(Size requiredSize) {
        this.requiredSize = requiredSize;
        return this;
    }

    public imageLoadBuilder setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public imageLoadBuilder setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public imageLoad load() {
        return new imageLoad(activity, placeholder, requiredSize, imageView, imageUrl);
    }
}