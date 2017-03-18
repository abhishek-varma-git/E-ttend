package com.amrit.e_ttend;

import android.content.Context;
import android.content.MutableContextWrapper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Amrit on 3/8/2017.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mctx;
    private MySingleton(Context context)
    {
        mctx=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;

    }
    public static synchronized MySingleton getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance=new MySingleton(context);
        }
        return mInstance;
    }
    public <T>void addToRequestque(Request<T> request)
    {
        requestQueue.add(request);

    }
}
