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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Utility.Util;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.TreandingAdapter;
import com.scenekey.helper.SessionManager;
import com.scenekey.models.Events;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mindiii on 26/4/17.
 */

public class Trending_Fragment extends Fragment {

    public static final String TAG = Trending_Fragment.class.toString();
    VolleyGetPost volleyGetPost;
    HomeActivity activity;
    SessionManager sessionManager;
    boolean trendingEvents;
    ArrayList<Events> eventsArrayList;
    RecyclerView rclv_f3_trending;
    RecyclerView.LayoutManager layoutManager;
    TreandingAdapter treandingAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f1_trending_one, null);
        rclv_f3_trending = (RecyclerView) v.findViewById(R.id.rclv_f3_trending);
        activity = HomeActivity.instance;
        activity.setTitleVisibality(View.VISIBLE);
        activity.showProgDilog(false,TAG);
        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = activity.getSessionManager();
        activity.setTitle(activity.getResources().getString(R.string.Trending));
        getTrending();

    }

    void setRecyclerView() {
        if (treandingAdapter == null) {
            treandingAdapter = new TreandingAdapter(activity, eventsArrayList);
            layoutManager = new LinearLayoutManager(activity);
            rclv_f3_trending.setLayoutManager(layoutManager);
            rclv_f3_trending.setAdapter(treandingAdapter);
            treandingAdapter.notifyDataSetChanged();
            rclv_f3_trending.setHasFixedSize(true);
        } else {
            treandingAdapter.notifyDataSetChanged();
            rclv_f3_trending.setHasFixedSize(true);
        }
    }

    void getTrending() {
        volleyGetPost = new VolleyGetPost(HomeActivity.instance, getContext(), WebService.TRENDING, false) {
            @Override
            public void onVolleyResponse(String response) {
                Util.printBigLogcat(TAG, response);
                try {JSONObject jo = new JSONObject(response);
                    if (jo.has("status")) {
                        int status = jo.getInt("status");
                        if (status == 0){
                            trendingEvents = false;
                            activity.dismissProgDailog();
                            try {
                                Toast.makeText(activity,jo.getString("message"),Toast.LENGTH_SHORT).show();
                            }catch (Exception e){

                            }
                        }
                        //else if()
                    } else {
                        if (jo.has("userinfo")) {
                        }
                        if (jo.has("events")) {
                            if (eventsArrayList == null) eventsArrayList = new ArrayList<>();
                            else eventsArrayList.clear();
                            JSONArray eventAr = jo.getJSONArray("events");
                            for (int i = 0; i < eventAr.length(); i++) {
                                JSONObject object = eventAr.getJSONObject(i);
                                Events events = new Events();
                                if (object.has("venue"))
                                    events.setVenueJSON(object.getJSONObject("venue"));
                                if (object.has("artists"))
                                    events.setArtistsArray(object.getJSONArray("artists"));
                                if (object.has("events"))
                                    events.setEventJson(object.getJSONObject("events"));
                                try{
                                    events.setOngoing(events.checkWithTime(events.getEvent().getEvent_date() , events.getEvent().getInterval() ));
                                }catch (Exception e){

                                }
                                try {
                                    events.settimeFormat();
                                }catch (Exception e){

                                }
                                try {
                                    events.setRemainingTime();
                                }
                                catch (Exception e){

                                }
                                eventsArrayList.add(events);
                                //Log.e("Result",events.toString());
                            }
                            if (eventsArrayList.size() <= 0) {
                                Toast.makeText(activity, "No Event found near your location", Toast.LENGTH_LONG).show();
                            }
                            setRecyclerView();

                        }
                        activity.dismissProgDailog();
                    }
                } catch (Exception e) {
                    activity.dismissProgDailog();
                    Toast.makeText(activity,getString(R.string.somethingwentwrong),Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "" + e);
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                activity.dismissProgDailog();
                try {
                    activity.showToast(getString(R.string.somethingwentwrong));
                }catch (IllegalStateException e){
                }
                Log.e(TAG, error + "");
            }

            @Override
            public void onNetError() {
                activity.showToast( "Please check your internet connection");
                activity.dismissProgDailog();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("lat",activity.getlatlong()[0]);
                params.put("long",activity.getlatlong()[1]);
                params.put("user_id", sessionManager.getUserInfo().getUserID() + "");
                //Log.e(TAG,params.toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        volleyGetPost.execute();
    }


}
