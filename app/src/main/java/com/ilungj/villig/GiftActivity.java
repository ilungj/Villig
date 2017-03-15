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

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class GiftActivity extends AppCompatActivity {

    private static final String TAG = "GiftActivity";

    private Button mAddPayment;

    private String mToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        init();
    }

    private void init() {
        mAddPayment = (Button) findViewById(R.id.gift_payment_button);
        mAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "TEST");
                Intent intent = new Intent(GiftActivity.this, PaymentInformationActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        final EditText temp = (EditText) findViewById(R.id.gift_amount);

        ((Button) findViewById(R.id.gift_button))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(mToken.isEmpty()) && temp.getText().length() != 0) {
                            final String token = mToken;
                            final int amount = Integer.valueOf(temp.getText().toString());
                            RequestQueue requestQueue = BaseSingleton.getInstance().getRequestQueue();
                            String url = "http://www.ilungj.com/app/stripetest.php";

                            StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    SharedPreferences temp = getSharedPreferences("UserSession", MODE_PRIVATE);
                                    Log.d(TAG, "TEST " + temp.getString("userId", "Null"));

                                    params.put("stripeToken", token);
                                    params.put("amount_post", String.valueOf(amount));
                                    params.put("user_id_post", temp.getString("userId", "Null"));
                                    return params;
                                }
                            };

                            requestQueue.add(stringRequest);
                            BaseSingleton.getInstance().showSnackBar(GiftActivity.this, "Success");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                mAddPayment.setVisibility(View.INVISIBLE);
                mToken = data.getStringExtra("token");
                final String token = mToken;
            }
            if(resultCode == RESULT_CANCELED) {

            }
        }
    }
}
