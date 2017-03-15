package com.ilungj.villig;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;

import static android.widget.LinearLayout.VERTICAL;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class ViewTaskActivity extends BaseDrawerActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ViewTaskActivity";

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FloatingActionButton mFab;

    private List<Task> mTaskList;
    private Task mMostRecentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        init();
    }

    private void init() {

        mFab = (FloatingActionButton) findViewById(R.id.view_task_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTaskActivity.this, CreateTaskActivity.class);
                startActivity(intent);
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.view_task_recyclerview);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new FadeInDownAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.view_task_swipe);

        RequestQueue requestQueue = BaseSingleton.getInstance().getRequestQueue();
        String url = "http://www.ilungj.com/app/load-task.php";

        StringRequest stringRequest = new StringRequest(GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                mTaskList = Arrays.asList(gson.fromJson(response, Task[].class));

                mAdapter = new Adapter(mTaskList);

                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                mSwipeRefreshLayout.setOnRefreshListener(ViewTaskActivity.this);

                if(mTaskList.size() > 0) {
                    mMostRecentTask = mTaskList.get(mTaskList.size() - 1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
                BaseSingleton.getInstance().showSnackBar(ViewTaskActivity.this, error.getMessage());
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        RequestQueue requestQueue = BaseSingleton.getInstance().getRequestQueue();
        String url = "http://www.ilungj.com/app/refresh-task.php";

        StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                if(!response.isEmpty()) {
                    Gson gson = new Gson();
                    List<Task> newList = Arrays.asList(gson.fromJson(response, Task[].class));
                    if(!newList.isEmpty())
                        mAdapter.addItem(newList.get(0));
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
                BaseSingleton.getInstance().showSnackBar(ViewTaskActivity.this, error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("task_id_post", mMostRecentTask.taskId);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private ArrayList<Task> mArrayList;

        private Adapter(List<Task> list) {
            mArrayList = new ArrayList<Task>(list);
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_view_task, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Task currentTask = mArrayList.get(position);
            holder.mHolderTitle.setText(currentTask.taskName);
            holder.mHolderCreator.setText(currentTask.creatorName);
            holder.mHolderTime.setText(currentTask.dateCreated);

            Drawable drawable = null;
            switch (currentTask.creatorAvatar) {
                case "1":
                    drawable = ContextCompat.getDrawable(ViewTaskActivity.this, R.mipmap.avatar_one);
                    break;
                case "2":
                    drawable = ContextCompat.getDrawable(ViewTaskActivity.this, R.mipmap.avatar_two);
                    break;
                case "3":
                    drawable = ContextCompat.getDrawable(ViewTaskActivity.this, R.mipmap.avatar_three);
                    break;
                case "4":
                    drawable = ContextCompat.getDrawable(ViewTaskActivity.this, R.mipmap.avatar_four);
                    break;
                case "5":
                    drawable = ContextCompat.getDrawable(ViewTaskActivity.this, R.mipmap.avatar_five);
                    break;
                case "6":
                    drawable = ContextCompat.getDrawable(ViewTaskActivity.this, R.mipmap.avatar_six);
                    break;
            }

            holder.mHolderAvatar.setImageDrawable(drawable);

            final View view = holder.mHolder;
            final Task task = currentTask;
            holder.mHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewTaskActivity.this, DetailedTaskActivity.class);
                    intent.putExtra("taskId", task.taskId);
                    intent.putExtra("taskName", task.taskName);
                    intent.putExtra("creatorId", task.creatorId);
                    intent.putExtra("creatorName", task.creatorName);
                    intent.putExtra("creatorEmail", task.creatorEmail);
                    intent.putExtra("creatorAvatar", task.creatorAvatar);
                    intent.putExtra("dateCreated", task.dateCreated);
                    intent.putExtra("dateDue", task.dateDue);
                    intent.putExtra("description", task.description);
                    intent.putExtra("incentive", task.incentive);

                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(ViewTaskActivity.this, view, "holder");
                    ActivityCompat.startActivity(ViewTaskActivity.this, intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        private void addItem(Task task) {
            mArrayList.add(mArrayList.size(), task);
            notifyItemInserted(mArrayList.size());
            mRecyclerView.smoothScrollToPosition(mArrayList.size());
            mMostRecentTask = task;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private RelativeLayout mHolder;

            private TextView mHolderTitle;
            private TextView mHolderCreator;
            private TextView mHolderTime;
            private ImageView mHolderAvatar;

            public ViewHolder(View itemView) {
                super(itemView);
                mHolder = (RelativeLayout) itemView.findViewById(R.id.holder);

                mHolderTitle = (TextView) itemView.findViewById(R.id.holder_title);
                mHolderCreator = (TextView) itemView.findViewById(R.id.holder_creator);
                mHolderTime = (TextView) itemView.findViewById(R.id.holder_time);
                mHolderAvatar = (ImageView) itemView.findViewById(R.id.holder_avatar);
            }
        }
    }

    private class Task {

        @SerializedName("task_id")
        private String taskId;
        @SerializedName("task_name")
        private String taskName;
        @SerializedName("creator_id")
        private String creatorId;
        @SerializedName("creator_name")
        private String creatorName;
        @SerializedName("date_created")
        private String dateCreated;
        @SerializedName("date_due")
        private String dateDue;
        @SerializedName("description")
        private String description;
        @SerializedName("incentive")
        private String incentive;
        @SerializedName("creator_email")
        private String creatorEmail;
        @SerializedName("creator_avatar")
        private String creatorAvatar;

        private Task() {

        }
    }
}
