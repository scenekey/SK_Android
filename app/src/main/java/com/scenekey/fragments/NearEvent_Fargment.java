package com.scenekey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.NearEventAdapter;
import com.scenekey.models.Events;

import java.util.ArrayList;

/**
 * Created by mindiii on 15/5/17.
 */

public class NearEvent_Fargment extends Fragment {


    static final String TAG = NearEvent_Fargment.class.toString();
    HomeActivity activity;
    RecyclerView rclv_f3_trending;
    ArrayList<Events> eventsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f1_trending_one, null);
        rclv_f3_trending = (RecyclerView) v.findViewById(R.id.rclv_f3_trending);
        activity = HomeActivity.instance;
        activity.setTitleVisibality(View.VISIBLE);

        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle(activity.getResources().getString(R.string.Enter));
        setRecyclerView();
        Log.e(TAG, " NearByFragment");

    }

    public void setEventsList(ArrayList<Events> eventsList) {
        this.eventsList = eventsList;
    }



    void setRecyclerView() {
        if (rclv_f3_trending.getAdapter() == null) {
            NearEventAdapter treandingAdapter = new NearEventAdapter(activity, eventsList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            rclv_f3_trending.setLayoutManager(layoutManager);
            rclv_f3_trending.setAdapter(treandingAdapter);
            treandingAdapter.notifyDataSetChanged();
            rclv_f3_trending.setHasFixedSize(true);
        } else {
            rclv_f3_trending.getAdapter().notifyDataSetChanged();
        }
    }
}
