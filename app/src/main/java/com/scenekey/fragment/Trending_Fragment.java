package com.scenekey.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.Trending_Adapter;
import com.scenekey.helper.WebServices;
import com.scenekey.model.Events;
import com.scenekey.model.UserInfo;
import com.scenekey.util.SceneKey;
import com.scenekey.util.Utility;
import com.scenekey.volleymultipart.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Trending_Fragment extends Fragment {

    private Context context;
    private HomeActivity activity;


    private  Utility utility;
    private RecyclerView rcViewTrending;
    private String lat="",lng="";
    private Trending_Adapter trendingAdapter;
    private ArrayList<Events> eventsArrayList;
    public boolean canCallWebservice;
    private static Timer timerHttp;
    private final String TAG = Trending_Fragment.class.toString();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trending, container,false);
        rcViewTrending = v.findViewById(R.id.rcViewTrending);
        activity.setTitleVisibility(View.VISIBLE);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle(context.getResources().getString(R.string.trending));
        trendingData();
    }

    @Override
    public void onStart() {
        super.onStart();
        canCallWebservice=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        canCallWebservice = true;
        if (timerHttp == null) setDataTimer();
    }

    private void trendingData() {
        String[] latLng=  activity.getLatLng();
        lat=latLng[0];
        lng=latLng[1];
        if (lat.equals("0.0")&&lng.equals("0.0")){
            latLng=  activity.getLatLng();
            lat= latLng[0];
            lng= latLng[1];
            retryLocation();
        }
        else{
            activity.showProgDialog(false,TAG);
            getTrendingData();
            if (timerHttp == null) setDataTimer();
        }
    }

    private void retryLocation() {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_popup_with_btn);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //      deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id

        TextView tvCancel, tvTryAgain,tvTitle,tvMessages;

        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvMessages = dialog.findViewById(R.id.tvMessages);
        tvCancel = dialog.findViewById(R.id.tvPopupCancel);
        tvTryAgain = dialog.findViewById(R.id.tvPopupOk);

        tvTitle.setText(R.string.gps_new);
        tvMessages.setText(R.string.couldntGetLocation);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                activity.showProgDialog(false,TAG);
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run() {

                        if (utility.checkNetworkProvider()|utility.checkGPSProvider()){
                            trendingData();
                        }else{
                            utility.checkGpsStatus();
                            trendingData();
                        }
                    }
                }, 3 * 1000); // wait for 3 seconds
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.dismissProgDialog();
                dialog.cancel();

            }
        });
        dialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        activity= (HomeActivity) getActivity();
        utility=new Utility(context);
    }

    private void setRecyclerView() {
        if (trendingAdapter == null) {
            trendingAdapter = new Trending_Adapter(activity, eventsArrayList,new String[]{lat,lng});
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            rcViewTrending.setLayoutManager(layoutManager);
            rcViewTrending.setAdapter(trendingAdapter);
            trendingAdapter.notifyDataSetChanged();
            rcViewTrending.setHasFixedSize(true);
        } else {
            trendingAdapter.notifyDataSetChanged();
            rcViewTrending.setHasFixedSize(true);
        }
    }

    public void getTrendingData() {

        if (utility.checkInternetConnection()) {
            StringRequest request = new StringRequest(Request.Method.POST, WebServices.TRENDING, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    activity.dismissProgDialog();
                    // get response
                    try {
                        JSONObject jo = new JSONObject(response);

                        if (jo.has("status")) {
                            int status = jo.getInt("status");
                            if (status == 0){
                                activity.dismissProgDialog();
                                try {
                                    Toast.makeText(activity,jo.getString("message"),Toast.LENGTH_SHORT).show();
                                    if (eventsArrayList == null) eventsArrayList = new ArrayList<>();
                                    else eventsArrayList.clear();
                                    setRecyclerView();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            try {
                                if (jo.has("myInfo")) {
                                    UserInfo userInfo = activity.userInfo();
                                    JSONObject user = jo.getJSONObject("myInfo");
                                    if(user.has("makeAdmin"))   userInfo.makeAdmin=(user.getString("makeAdmin"));
                                    if(user.has("lat"))         userInfo.latitude=(user.getString("lat"));
                                    if(user.has("longi"))       userInfo.longitude=(user.getString("longi"));
                                    if(user.has("address"))       userInfo.address=(user.getString("address"));
                                    if(user.has("fullname"))       userInfo.fullName=(user.getString("fullname"));
                                    if(user.has("key_points"))userInfo.keyPoints=(user.getString("key_points"));
                                    activity.updateSession(userInfo);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            //else if()
                        } else {
                            try {
                                if (jo.has("myInfo")) {
                                    UserInfo userInfo = activity.userInfo();
                                    JSONObject user = jo.getJSONObject("myInfo");
                                    if(user.has("makeAdmin"))   userInfo.makeAdmin=(user.getString("makeAdmin"));
                                    if(user.has("lat"))         userInfo.latitude=(user.getString("lat"));
                                    if(user.has("longi"))       userInfo.longitude=(user.getString("longi"));
                                    if(user.has("address"))       userInfo.address=(user.getString("address"));
                                    if(user.has("fullname"))       userInfo.fullName=(user.getString("fullname"));
                                    if(user.has("key_points"))userInfo.keyPoints=(user.getString("key_points"));
                                    activity.updateSession(userInfo);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
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
                                        events.setOngoing(events.checkWithTime(events.getEvent().event_date , events.getEvent().interval));
                                    }catch (Exception e){
                                        Utility.e("Date exception",e.toString());
                                    }
                                    try {
                                        events.settimeFormat();
                                    }catch (Exception e){
                                        Utility.e("Exception time",e.toString());
                                    }
                                    try {
                                        events.setRemainingTime();
                                    }
                                    catch (Exception e){
                                        Utility.e("Exception Remaining",e.toString());
                                    }
                                    eventsArrayList.add(events);
                                    // Util.printLog("Result",events.toString());
                                }
                                if (eventsArrayList.size() <= 0) {
                                    Toast.makeText(activity, "No Event found near your location", Toast.LENGTH_LONG).show();
                                }
                                setRecyclerView();
                            }
                            activity.dismissProgDialog();
                        }
                    } catch (Exception e) {
                        activity.dismissProgDialog();
                        Utility.showToast(context,getString(R.string.somethingwentwrong),0);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    utility.volleyErrorListner(e);
                    activity.dismissProgDialog();
                }
            }) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("lat",lat);
                    params.put("long",lng);
                    params.put("user_id", SceneKey.sessionManager.getUserInfo().userID + "");

                    Utility.e(TAG," params "+params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(request);
            request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1));
        }else{
            utility.snackBar(rcViewTrending,getString(R.string.internetConnectivityError),0);
            activity.dismissProgDialog();
        }
    }

   private void setDataTimer() {
        if(timerHttp == null )timerHttp = new Timer();

        //Set the schedule function and rate
        //TODO timer changed as required
        timerHttp.scheduleAtFixedRate(new TimerTask() {

                                          @Override
                                          public void run() {
                                              activity.runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      Utility.e(TAG,"TimerVolley Trending");
                                                      try{

                                                          if (canCallWebservice) {
                                                              getTrendingData();}
                                                      }catch (Exception e){
                                                          e.printStackTrace();
                                                      }


                                                  }
                                              });
                                          }

                                      },
                //Set how long before to start calling the TimerTask (in milliseconds)
                60000,
                //Set the amount of time between each execution (in milliseconds)
                60000);
    }

    @Override
    public void onPause() {
        if (timerHttp != null) timerHttp.cancel();
        timerHttp=null;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (timerHttp != null) timerHttp.cancel();
        timerHttp=null;
        super.onDestroy();
    }
}
