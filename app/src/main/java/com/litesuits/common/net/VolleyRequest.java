package com.litesuits.common.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Calvin on 15-10-20.
 */
public class VolleyRequest {

    public static Context sContext;

    private static RequestQueue sRequestQueue;

    public static void init(Context context) {
        if (sRequestQueue != null) return;
        sContext = context;
        //使用OkHttp作为Volley的引擎
        sRequestQueue = Volley.newRequestQueue(context, new OkHttpStack(OkHttpConfig.getmOkHttpClient()));
    }

    public static RequestQueue getRequestQueue() {
        return sRequestQueue;
    }

    public static Request addRequest(Request<String> request) {
        return sRequestQueue.add(request);
    }

    public static Request addRequest(Request<String> request, Object tag) {
        request.setTag(tag);
        return sRequestQueue.add(request);
    }

    public static void cancelRequest(Object tag) {
        sRequestQueue.cancelAll(tag);
    }

}
