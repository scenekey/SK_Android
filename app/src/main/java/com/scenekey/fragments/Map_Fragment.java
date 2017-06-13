package com.scenekey.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.scenekey.R;
import com.scenekey.Utility.CusDialogProg;
import com.scenekey.Utility.Util;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.MapInfoAdapter;
import com.scenekey.helper.Constants;
import com.scenekey.models.Events;
import com.scenekey.models.UserInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mindiii on 27/4/17.
 */

public class Map_Fragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private static final String TAG = Map_Fragment.class.toString();
    private MapView mMapView;
    private GoogleMap googleMap;
    private MapInfoAdapter adapter;
    CusDialogProg cusDialogProg;
    ArrayList<Events> eventArrayMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f1_map_event, null);
        mMapView = (MapView) v.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity().setTitleVisibality(View.VISIBLE);
        activity().setTitle(activity().getResources().getString(R.string.Scene));
        if(cusDialogProg == null)cusDialogProg = new CusDialogProg(activity(),R.layout.custom_progress_dialog_layout);
        checkEventAvailablity();
    }

    HomeActivity activity(){
        return HomeActivity.instance;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {


        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }

    protected Marker createMarker(String latitude, String longitude) {
        double lat = Double.parseDouble(latitude);
        double longe = Double.parseDouble(longitude);
        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, longe))
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
        //;
    }

    void mapAsyncer() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                Marker m = null;
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(HomeActivity.instance, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.instance, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (HomeActivity.instance.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //callPermission(Constants.TYPE_PERMISSION_FINE_LOCATION);
                        } else if (HomeActivity.instance.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //callPermission(Constants.TYPE_PERMISSION_CORAS_LOCATION);
                        }
                    }
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(Double.parseDouble(activity().getlatlong()[0]), Double.parseDouble(activity().getlatlong()[1]));


                //marker.icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.resizeBitmap( ImageUtil.getBitmapByUrl(markerUrl) , 2 ,2)));
               // eventArrayMarker = activity.getEventsArrayList();
                try {


                    if (eventArrayMarker == null || eventArrayMarker.size() <= 0) {
                        Log.e(TAG, " Size 0");

                    } else {
                        adapter = new MapInfoAdapter(activity(), eventArrayMarker);
                        googleMap.setInfoWindowAdapter(adapter);
                        for (int i = 0; i < eventArrayMarker.size(); i++) {
                            Log.e(TAG, eventArrayMarker.get(i).getEvent().getEvent_name() + " : " + eventArrayMarker.get(i).getEvent().getEvent_id() + " : " + eventArrayMarker.get(i).getVenue().getLatitude() + " : " + eventArrayMarker.get(i).getVenue().getLongitude());
                            if (i == (eventArrayMarker.size() - 1)) {
                                sydney = new LatLng(Double.parseDouble(eventArrayMarker.get(i).getVenue().getLatitude()), Double.parseDouble(eventArrayMarker.get(i).getVenue().getLongitude()));
                                m = createMarker(eventArrayMarker.get(i).getVenue().getLatitude(), eventArrayMarker.get(i).getVenue().getLongitude());
                            } else
                                createMarker(eventArrayMarker.get(i).getVenue().getLatitude(), eventArrayMarker.get(i).getVenue().getLongitude());
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, " " + e);
                    e.printStackTrace();
                }

                //sydney = new LatLng(38.222046D, -122.144755D);
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        //Log.e(TAG,"Info window clicked"+marker.getId()+":"+marker.getZIndex());
                        Events events = eventArrayMarker.get(Integer.parseInt(marker.getId().replace("m", "")));
                        Event_Fragment frg = new Event_Fragment();
                        frg.setData(events.getEvent().getEvent_id(),events.getVenue().getVenue_name());
                        activity().addFragment(frg, 0);
                        activity().setBBvisiblity(View.GONE,TAG);
                        LatLng sydney = new LatLng(Double.parseDouble(events.getVenue().getLatitude()), Double.parseDouble(events.getVenue().getLongitude()));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        return true;
                    }
                });

                Handler handler = new Handler();
                final Marker finalM = m;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (finalM != null) finalM.showInfoWindow();
                    }
                }, 2000);


            }
        });


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e(TAG, "Info window clicked" + marker.getId() + ":" + marker.getZIndex());
        marker.showInfoWindow();
        return false;
    }

    public void notifyAdapter() {
        if (adapter != null) {
            adapter.setEventArrayList(activity().getEventsArrayList());
        }
    }



    void checkEventAvailablity() {
        cusDialogProg.setCancelable(false);
        cusDialogProg.setCanceledOnTouchOutside(false);
        cusDialogProg.setColor(R.color.transparent);
        cusDialogProg.show();

        VolleyGetPost volleyGetPost = new VolleyGetPost(activity(), activity(), WebService.EVENT_BY_LOCAL, false) {
            @Override
            public void onVolleyResponse(String response) {
                Util.printBigLogcat(TAG, " " + response);
                try {
                    JSONObject jo = new JSONObject(response);
                    if (jo.has("status")) {
                        int status = jo.getInt("status");
                        if (status == 0){
                            activity().dismissProgDailog();
                            try {
                               activity().showToast(jo.getString("message"));
                            }catch (Exception e){

                            }
                        }
                        if (jo.has("userInfo")) {
                            UserInfo  userInfo = activity().userInfo();
                            JSONObject user = jo.getJSONObject("userInfo");
                            if(user.has("makeAdmin"))   userInfo.setMakeAdmin(user.getString("makeAdmin"));
                            if(user.has("lat"))         userInfo.setLatitude(user.getString("lat"));
                            if(user.has("longi"))       userInfo.setLongitude(user.getString("longi"));
                            if(user.has("adminLat"))    userInfo.setLatitude(user.getString("adminLat"));
                            if(user.has("adminLong"))   userInfo.setLongitude(user.getString("adminLong"));
                            if(user.has("address"))     userInfo.setAddress(user.getString("address"));
                            activity().updateSession(userInfo);
                        }
                        //else if()
                    } else {
                        if (jo.has("userinfo")) {
                        }
                        if (jo.has("events")) {
                            if (eventArrayMarker == null) eventArrayMarker = new ArrayList<>();
                            else eventArrayMarker.clear();
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
                                eventArrayMarker.add(events);
                                //Log.e("Size",eventsArrayList.size()+"");
                            }

                            try {
                                mapAsyncer();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    if (cusDialogProg != null) cusDialogProg.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (cusDialogProg != null) cusDialogProg.dismiss();
                    try {
                        activity().showToast( getResources().getString(R.string.somethingwentwrong));
                    }catch (Exception ee){

                    }
                }

            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (cusDialogProg != null) cusDialogProg.dismiss();
                try {
                    //TODO Activity activity();
                    activity().showToast( getResources().getString(R.string.somethingwentwrong));
                }catch (Exception e){

                }
                Log.e(TAG, "" + error);
            }

            @Override
            public void onNetError() {
                if (cusDialogProg != null) cusDialogProg.dismiss();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("lat",activity().getlatlong()[0]);
                params.put("long",activity().getlatlong()[1]);
                params.put("user_id",activity().userInfo().getUserID());
                params.put("updateLocation", Constants.ADMIN_NO);
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
