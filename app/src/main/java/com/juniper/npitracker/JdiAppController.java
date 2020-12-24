package com.juniper.npitracker;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.juniper.npitracker.util.LruJdiBitmapCache;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by koteswara on 04/11/16.
 */

public class JdiAppController extends Application{
    public static final String TAG = JdiAppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public ArrayList<String> getNpiid() {
        return npiid;
    }

    public void setNpiid(ArrayList<String> npiid) {
        this.npiid = npiid;
    }

    public HashMap getNpikeyandid() {
        return npikeyandid;
    }

    public void setNpikeyandid(HashMap npikeyandid) {
        this.npikeyandid = npikeyandid;
    }

    ArrayList<String> npiid;
    HashMap npikeyandid;
    private static JdiAppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized JdiAppController getInstance() {
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruJdiBitmapCache());
        }
        return this.mImageLoader;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
