package com.example.volleycrudexample;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleTon {

    public static MySingleTon singleTon;
    public RequestQueue requestQueue;
    public Context context;

    private MySingleTon(Context context) {
        this.context = context;
        getRequestQueue();
    }

    public static MySingleTon getInstance(Context context) {
        if (singleTon == null) {
            // create object
            singleTon = new MySingleTon(context);
        }
        return singleTon;
    }

    public RequestQueue getRequestQueue() {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        return requestQueue;
    }

    public void addRequestToQueue(Request request) {
        requestQueue.add(request);
    }


}
