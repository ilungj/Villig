package com.ilungj.villig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mButton;

    private ImageView mAvatarOne;
    private ImageView mAvatarTwo;
    private ImageView mAvatarThree;
    private ImageView mAvatarFour;
    private ImageView mAvatarFive;
    private ImageView mAvatarSix;

    private int mAvatar = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = (EditText) findViewById(R.id.register_name);
        mEmail = (EditText) findViewById(R.id.register_email);
        mPassword = (EditText) findViewById(R.id.register_password);

        mAvatarOne = (ImageView) findViewById(R.id.register_avatar_one);
        mAvatarTwo = (ImageView) findViewById(R.id.register_avatar_two);
        mAvatarThree = (ImageView) findViewById(R.id.register_avatar_three);
        mAvatarFour = (ImageView) findViewById(R.id.register_avatar_four);
        mAvatarFive = (ImageView) findViewById(R.id.register_avatar_five);
        mAvatarSix = (ImageView) findViewById(R.id.register_avatar_six);

        mAvatarOne.setOnClickListener(this);
        mAvatarTwo.setOnClickListener(this);
        mAvatarThree.setOnClickListener(this);
        mAvatarFour.setOnClickListener(this);
        mAvatarFive.setOnClickListener(this);
        mAvatarSix.setOnClickListener(this);

        mButton = (Button) findViewById(R.id.activity_register_register_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = BaseSingleton.getInstance().getRequestQueue();
                String url = "http://www.ilungj.com/app/register.php";

                StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        BaseSingleton.getInstance().showSnackBar(RegisterActivity.this, response);
                        try {
                            JSONArray userInformation = new JSONArray(response);
                            JSONObject user = userInformation.getJSONObject(0);

                            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("userId", user.getString("user_id"));
                            edit.putString("userName", user.getString("user_name"));
                            edit.putString("userEmail", user.getString("user_email"));
                            edit.putString("userAvatar", user.getString("user_avatar"));
                            edit.apply();

                            Log.d("TEST", user.getString("user_avatar"));

                            Intent intent = new Intent(RegisterActivity.this, ViewTaskActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);                                finish();
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        BaseSingleton.getInstance().showSnackBar(RegisterActivity.this, error.getMessage());
                        Log.d(TAG, error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name_post", mName.getText().toString());
                        params.put("email_post", mEmail.getText().toString());
                        params.put("password_post", mPassword.getText().toString());
                        params.put("avatar_post", String.valueOf(mAvatar));
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_avatar_one:
                reset();
                v.setAlpha(0.5f);
                mAvatar = 1;
                break;
            case R.id.register_avatar_two:
                reset();
                v.setAlpha(0.5f);
                mAvatar = 2;
                break;
            case R.id.register_avatar_three:
                reset();
                v.setAlpha(0.5f);
                mAvatar = 3;
                break;
            case R.id.register_avatar_four:
                reset();
                v.setAlpha(0.5f);
                mAvatar = 4;
                break;
            case R.id.register_avatar_five:
                reset();
                v.setAlpha(0.5f);
                mAvatar = 5;
                break;
            case R.id.register_avatar_six:
                reset();
                v.setAlpha(0.5f);
                mAvatar = 6;
                break;
        }
    }

    private void reset() {
        mAvatarOne.setAlpha(1f);
        mAvatarTwo.setAlpha(1f);
        mAvatarThree.setAlpha(1f);
        mAvatarFour.setAlpha(1f);
        mAvatarFive.setAlpha(1f);
        mAvatarSix.setAlpha(1f);
    }
}
