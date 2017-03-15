package com.ilungj.villig;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class DetailedTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DetailedTaskActivity";

    private Bundle mBundle;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        init();
    }

    private void init() {
        mBundle = getIntent().getExtras();
        mSharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        ((TextView) findViewById(R.id.detailed_task_title))
                .setText(mBundle.getString("taskName"));
        ((TextView) findViewById(R.id.detailed_task_creator))
                .setText(mBundle.getString("creatorName"));
        ((TextView) findViewById(R.id.detailed_task_time))
                .setText(mBundle.getString("dateCreated"));
        ((TextView) findViewById(R.id.detailed_task_description))
                .setText(mBundle.getString("description"));

        Drawable drawable = null;
        String id = mBundle.getString("creatorAvatar");
        switch (id) {
            case "1":
                drawable = ContextCompat.getDrawable(DetailedTaskActivity.this, R.mipmap.avatar_one);
                break;
            case "2":
                drawable = ContextCompat.getDrawable(DetailedTaskActivity.this, R.mipmap.avatar_two);
                break;
            case "3":
                drawable = ContextCompat.getDrawable(DetailedTaskActivity.this, R.mipmap.avatar_three);
                break;
            case "4":
                drawable = ContextCompat.getDrawable(DetailedTaskActivity.this, R.mipmap.avatar_four);
                break;
            case "5":
                drawable = ContextCompat.getDrawable(DetailedTaskActivity.this, R.mipmap.avatar_five);
                break;
            case "6":
                drawable = ContextCompat.getDrawable(DetailedTaskActivity.this, R.mipmap.avatar_five);
                break;
        }

        ((ImageView) findViewById(R.id.detailed_task_avatar))
                .setImageDrawable(drawable);

        ((Button) findViewById(R.id.detailed_task_volunteer))
                .setOnClickListener(this);

        ((Button) findViewById(R.id.detailed_task_gift))
                .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.detailed_task_volunteer:
                Log.d(TAG, mBundle.getString("creatorEmail") + " " + mSharedPreferences.getString("userId", "Null") + " " + mBundle.getString("taskId"));

                RequestQueue requestQueue = BaseSingleton.getInstance().getRequestQueue();
                String url = "http://www.ilungj.com/app/volunteer-task.php";

                StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, mBundle.getString("creatorEmail") + " " + mSharedPreferences.getString("userId", "Null") + " " + mBundle.getString("taskId"));
                        Log.d(TAG, response);
                        BaseSingleton.getInstance().showSnackBar(DetailedTaskActivity.this, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, error.getMessage());
//                        BaseSingleton.getInstance().showSnackBar(DetailedTaskActivity.this, error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("creator_email_post", mBundle.getString("creatorEmail"));
                        params.put("user_id_post", mSharedPreferences.getString("userId", "Null"));
                        params.put("task_id_post", mBundle.getString("taskId"));
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                break;
            case R.id.detailed_task_gift:
                Intent intent = new Intent(DetailedTaskActivity.this, GiftActivity.class);
                startActivity(intent);
                break;
        }
    }

}
