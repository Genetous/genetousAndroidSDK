package com.mubo.genetous_imageload;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LazyLoad {
    public static LruCache<String, Bitmap> getMemoryCache() {
        return memoryCache;
    }
    public static List<Map<String,Integer>> images;
    private static LruCache<String, Bitmap> memoryCache;
    public LazyLoad(){

    }
    public static void create(){
        images=new ArrayList<>();
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }
}
