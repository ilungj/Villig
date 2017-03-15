package com.ilungj.villig;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class CreateTaskActivity extends BaseDrawerActivity {

    private static final String TAG = "CreateTaskActivity";

    private EditText mTitle;
    private EditText mDescription;
    private EditText mIncentive;

    private DatePicker mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        init();
    }

    private void init() {
        mTitle = (EditText) findViewById(R.id.create_task_title);
        mDescription = (EditText) findViewById(R.id.create_task_description);
        mIncentive = (EditText) findViewById(R.id.create_task_incentive);
        mDate = (DatePicker) findViewById(R.id.create_task_date);

        Button button = (Button) findViewById(R.id.create_task_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = BaseSingleton.getInstance().getRequestQueue();
                String url = "http://www.ilungj.com/app/create-task.php";

                StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        BaseSingleton.getInstance().showSnackBar(CreateTaskActivity.this, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.getMessage());
                        BaseSingleton.getInstance().showSnackBar(CreateTaskActivity.this, error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String date = format.format(calendar.getTime());

                        Log.d(TAG, sharedPreferences.getString("userAvatar", "Null"));

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("task_name_post", mTitle.getText().toString());
                        params.put("creator_id_post", sharedPreferences.getString("userId", "999"));
                        params.put("creator_name_post", sharedPreferences.getString("userName", "Null"));
                        params.put("creator_email_post", sharedPreferences.getString("userEmail", "Null"));
                        params.put("creator_avatar_post", sharedPreferences.getString("userAvatar", "Null"));
                        params.put("date_due_post", date);
                        params.put("description_post", mDescription.getText().toString());
                        params.put("incentive_post", mIncentive.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });

    }

}
