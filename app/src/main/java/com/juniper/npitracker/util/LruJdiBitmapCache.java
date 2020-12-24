package com.juniper.npitracker.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by koteswara on 04/11/16.
 */

public class LruJdiBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{
    public LruJdiBitmapCache(int maxSize) {
        super(maxSize);
    }
    public LruJdiBitmapCache() {
        this(getDefaultLruCacheSize());
    }
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
