package com.ilungj.villig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Il Ung on 3/10/2017.
 */

public class BaseDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected FrameLayout mContentLayout;
    protected DrawerLayout mDrawerLayout;

    private int mId;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_drawer, null);
        mContentLayout = (FrameLayout) mDrawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, mContentLayout, true);

        super.setContentView(mDrawerLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                doActionOnItemSelect();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        View headerView = navigationView.getHeaderView(0);
//        TextView nameText = (TextView) headerView.findViewById(R.id.nav_text_name);
        ((TextView) headerView.findViewById(R.id.drawer_header_name))
                .setText(sharedPreferences.getString("userName", "Null"));

        ((TextView) headerView.findViewById(R.id.drawer_header_email))
                .setText(sharedPreferences.getString("userEmail", "Null"));

        ImageView image = (ImageView) headerView.findViewById(R.id.drawer_header_image);
        switch (sharedPreferences.getString("userAvatar", "Null")) {
            case "1":
                image.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.avatar_one));
                break;
            case "2":
                image.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.avatar_two));
                break;
            case "3":
                image.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.avatar_three));
                break;
            case "4":
                image.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.avatar_four));
                break;
            case "5":
                image.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.avatar_five));
                break;
            case "6":
                image.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.avatar_six));
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences.Editor edit = getSharedPreferences("UserSession", MODE_PRIVATE).edit();
            edit.clear();
            edit.apply();

            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        mId = item.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void doActionOnItemSelect() {
        switch (mId) {
            case R.id.nav_home:
                Intent homeIntent = new Intent(this, ViewTaskActivity.class);
                startActivity(homeIntent);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_profile:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
                profileIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                profileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(0, 0);
                break;

            default:
                break;
        }
    }

}