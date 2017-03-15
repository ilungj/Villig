package com.ilungj.villig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText mEmail;
    private EditText mPassword;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        mEmail = (EditText) findViewById(R.id.login_email);
        mPassword = (EditText) findViewById(R.id.login_password);

        mButton = (Button) findViewById(R.id.login_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = BaseSingleton.getInstance().getRequestQueue();
                String url = "http://www.ilungj.com/app/login.php";

                StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(!response.contains("Error:")) {
                            try {
                                JSONArray userInformation = new JSONArray(response);
                                JSONObject user = userInformation.getJSONObject(0);
                                Log.d(TAG, user.get("user_id").toString());
                                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("userId", user.getString("user_id"));
                                edit.putString("userName", user.getString("user_name"));
                                edit.putString("userEmail", user.getString("user_email"));
                                edit.putString("userAvatar", user.getString("user_avatar"));
                                edit.apply();

                                Intent intent = new Intent(LoginActivity.this, ViewTaskActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);                                finish();
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        BaseSingleton.getInstance().showSnackBar(LoginActivity.this, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.getMessage());
                        BaseSingleton.getInstance().showSnackBar(LoginActivity.this, error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Log.d(TAG, "getParams called");
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email_post", mEmail.getText().toString());
                        params.put("password_post", mPassword.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });

    }
}
