package com.calak.jemmy.movie.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by JEMMY CALAK on 1/25/2018.
 */

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleySingleton(Context context1){
        context = context1;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());

        }
        return requestQueue;
    }

    public static synchronized VolleySingleton getmInstance(Context context1){
        if(mInstance==null){
            mInstance = new VolleySingleton(context1);
        }
        return mInstance;
    }

    public<T> void addToRequestque(Request<T> request){
        requestQueue.add(request);
    }
}
