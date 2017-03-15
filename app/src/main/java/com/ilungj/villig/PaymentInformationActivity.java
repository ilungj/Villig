package com.ilungj.villig;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class PaymentInformationActivity extends AppCompatActivity {

    private static final String TAG = "PIActivity";

    private RelativeLayout mRootContainer;

    private Scene mSceneCard;
    private Scene mSceneExp;
    private Scene mSceneCvv;

    private Transition mTransition;

    private EditText mCardEdit;
    private EditText mExpEdit;
    private EditText mCvvEdit;

    private String mCardString;
    private String mExpString;
    private String mCvvString;

    private Button mAddPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_information);
        init();
    }

    private void init() {
        mTransition = new ChangeBounds();
        mRootContainer = (RelativeLayout) findViewById(R.id.activity_payment_information);

        initCardScene();
        initExpScene();
        initCvvScene();

        mSceneCard.enter();

    }

    private void initCardScene() {
        mSceneCard = Scene.getSceneForLayout(mRootContainer, R.layout.payment_card_scene, this);
        mSceneCard.setEnterAction(new Runnable() {
            @Override
            public void run() {
                mCardEdit = (EditText) mRootContainer.findViewById(R.id.payment_card_edit);
                mCardEdit.requestFocus();
                mCardEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.length() == 16) {
                            BaseSingleton.getInstance().showSnackBar(PaymentInformationActivity.this, "HELLO");
                            mCardString = mCardEdit.getText().toString();
                            TransitionManager.go(mSceneExp, mTransition);
                        }
                    }
                });
            }
        });
    }

    private void initExpScene() {
        mSceneExp = Scene.getSceneForLayout(mRootContainer, R.layout.payment_exp_scene, this);
        mSceneExp.setEnterAction(new Runnable() {
            @Override
            public void run() {
                mExpEdit = (EditText) mRootContainer.findViewById(R.id.payment_exp_edit);
                mExpEdit.requestFocus();
//                mExpEdit.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//                        if(mExpEdit.length() == 0 && keyCode == KeyEvent.KEYCODE_DEL) {
//                            TransitionManager.go(mSceneCard, mTransition);
//                        }
//
//                        if(mExpEdit.length()  >= 6) {
//                            mExpString = mExpEdit.getText().toString();
//                            TransitionManager.go(mSceneCvv, mTransition);
//                        }
//                        return false;
//                    }
//                });
                mExpEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(count == 0) {
                            TransitionManager.go(mSceneCard, mTransition);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.length() == 6) {
                            mExpString = mExpEdit.getText().toString();
                            TransitionManager.go(mSceneCvv, mTransition);
                        }
                    }
                });
            }
        });
    }

    private void initCvvScene() {
        mSceneCvv = Scene.getSceneForLayout(mRootContainer, R.layout.payment_cvv_scene, this);
        mSceneCvv.setEnterAction(new Runnable() {
            @Override
            public void run() {
                mAddPayment = (Button) mRootContainer.findViewById(R.id.payment_add_confirmation);
                mCvvEdit = (EditText) mRootContainer.findViewById(R.id.payment_cvv_edit);
                mCvvEdit.requestFocus();
                mCvvEdit.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(mCvvEdit.length() == 0 && keyCode == KeyEvent.KEYCODE_DEL) {
                            TransitionManager.go(mSceneExp, mTransition);
                        }

                        if(mCvvEdit.length() == 3) {
                            mCvvString = mCvvEdit.getText().toString();

                            if(mAddPayment.getVisibility() == View.INVISIBLE) {
                                mAddPayment.setVisibility(View.VISIBLE);
                                mAddPayment.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int month = Integer.parseInt(mExpString.charAt(0) + "" + mExpString.charAt(1));
                                        int year = Integer.parseInt(mExpString.charAt(2) + "" + mExpString.charAt(3) + "" + mExpString.charAt(4) + "" + mExpString.charAt(5));
                                        Card card = new Card(
                                                mCardString,
                                                month,
                                                year,
                                                mCvvString);

                                        if(card.validateCard()) {

                                            new Stripe(BaseApplication.getContext()).createToken(
                                                    card,
                                                    "pk_test_DIlPU1EFFiC7vnfl62N9VlWr",
                                                    new TokenCallback() {
                                                        public void onError(Exception error) {
                                                            // Show localized error message
                                                            BaseSingleton.getInstance().showSnackBar(PaymentInformationActivity.this, error.getMessage());
                                                            Log.d(TAG, error.getMessage());
                                                            Log.d(TAG, "hello");
                                                        }

                                                        @Override
                                                        public void onSuccess(Token token) {
                                                            Intent intent = new Intent();
                                                            intent.putExtra("token", token.getId());
                                                            setResult(RESULT_OK, intent);
                                                            finish();
                                                        }
                                                    }
                                            );
                                        } else {
                                            BaseSingleton.getInstance().showSnackBar(PaymentInformationActivity.this, "This is an invalid credit card.");
                                        }
                                    }
                                });
                            }

                            return !(keyCode == KeyEvent.KEYCODE_DEL);
                        } else {
                            mAddPayment.setVisibility(View.INVISIBLE);
                        }
                        return false;
                    }
                });
            }
        });
    }

}
