package com.mubo.genetous_imageload;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.util.Size;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;

public class imageLoad {
    private Activity activity;
    private int placeholder;
    private Size requiredSize;
    private ImageView imageView;
    private String imageUrl;
    static imageLoader loader;
    private Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    public imageLoad(Activity activity, int placeholder, Size requiredSize, ImageView imageView,String imageUrl) {
        this.activity = activity;
        this.placeholder = placeholder;
        this.requiredSize = requiredSize;
        this.imageView = imageView;
        this.imageUrl = imageUrl;
        if(loader==null)
            loader=new imageLoader(activity,placeholder,requiredSize);
        loader.DisplayImage(imageUrl, imageView);

    }
}
