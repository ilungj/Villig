package com.ilungj.villig;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import im.dacer.androidcharts.BarView;

public class ProfileActivity extends BaseDrawerActivity {

    private TextView mName;
    private TextView mAssist;
    private ImageView mProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        mName = (TextView) findViewById(R.id.profile_name);
        mAssist = (TextView) findViewById(R.id.profile_volunteer_number);
        mProfileImage = (ImageView) findViewById(R.id.profile_avatar);

        String avatarId = BaseSingleton.getInstance()
                .getSharedPreferences(this)
                .getString("userAvatar", "Null");

        Drawable avatarDrawable = null;

        switch (avatarId) {
            case "1":
                avatarDrawable = ContextCompat.getDrawable(ProfileActivity.this, R.mipmap.avatar_one);
                break;
            case "2":
                avatarDrawable = ContextCompat.getDrawable(ProfileActivity.this, R.mipmap.avatar_two);
                break;
            case "3":
                avatarDrawable = ContextCompat.getDrawable(ProfileActivity.this, R.mipmap.avatar_three);
                break;
            case "4":
                avatarDrawable = ContextCompat.getDrawable(ProfileActivity.this, R.mipmap.avatar_four);
                break;
            case "5":
                avatarDrawable = ContextCompat.getDrawable(ProfileActivity.this, R.mipmap.avatar_five);
                break;
            case "6":
                avatarDrawable = ContextCompat.getDrawable(ProfileActivity.this, R.mipmap.avatar_six);
                break;
            default:
                break;
        }

        mProfileImage.setImageDrawable(avatarDrawable);

        String name = BaseSingleton.getInstance()
                .getSharedPreferences(this)
                .getString("userName", "Null");

        mName.setText(name);

        ArrayList<String> strList = new ArrayList<String>();
        strList.add("Jan");
        strList.add("Feb");
        strList.add("Mar");
        strList.add("Apr");
        strList.add("May");
        strList.add("Jun");
        strList.add("Jul");
        strList.add("Aug");
        strList.add("Sep");
        strList.add("Oct");
        strList.add("Nov");
        strList.add("Dec");

        ArrayList<Integer> dataList = new ArrayList<Integer>();
        dataList.add(0);
        dataList.add(0);
        dataList.add(1);
        dataList.add(0);
        dataList.add(0);
        dataList.add(0);
        dataList.add(0);
        dataList.add(0);
        dataList.add(0);
        dataList.add(0);

        BarView barView = (BarView)findViewById(R.id.bar_view);
        barView.setBottomTextList(strList);
        barView.setDataList(dataList, 5);
    }
}
