/*package com.dcrworkforce.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

*//**
 *
 * Add Request using this singleton class.
 *
 * Get instance by calling static method getInstance(context).
 *
 * Created by Aswin Vodapally on 2/24/2015.
 *//*
public class NetworkRequestQueue {

    private static NetworkRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;


    private NetworkRequestQueue(){
    }

    public static synchronized NetworkRequestQueue getInstance(Context context){
        mContext = context;
        if(mInstance == null){
            mInstance = new NetworkRequestQueue();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
*/