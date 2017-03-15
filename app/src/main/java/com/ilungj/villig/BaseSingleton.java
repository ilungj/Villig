package com.ilungj.villig;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Il Ung on 3/10/2017.
 */

public class BaseSingleton {

    private static BaseSingleton instance = null;

    private RequestQueue mRequestQueue;

    private BaseSingleton() {
        mRequestQueue = Volley.newRequestQueue(BaseApplication.getContext());
    }

    public static BaseSingleton getInstance() {
        if(instance == null)
            instance = new BaseSingleton();
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public SharedPreferences getSharedPreferences(Activity activity) {
        return activity.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
    }

    public void showSnackBar(Activity activity, String message) {
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}
