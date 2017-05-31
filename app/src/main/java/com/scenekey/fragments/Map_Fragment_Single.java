package com.scenekey.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.scenekey.Utility.CustomAlertDialog;
import com.scenekey.Utility.Font;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.MapInfoAdapter;
import com.scenekey.models.Events;

import java.util.ArrayList;

/**
 * Created by mindiii on 22/5/17.
 */

public class Map_Fragment_Single  extends Fragment implements View.OnClickListener {
    private static final String TAG = Map_Fragment_Single.class.toString();
    HomeActivity activity;
    private MapView mMapView;
    private GoogleMap googleMap;
    Double lat,longe;
    TextView txt_getDirection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f1_map_event2, null);
        mMapView = (MapView) v.findViewById(R.id.map_view);
        txt_getDirection = (TextView) v.findViewById(R.id.txt_getDirection);
        activity = HomeActivity.instance;
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        TextView txt_f1_title = (TextView) v.findViewById(R.id.txt_f1_title);
        txt_f1_title.setTypeface(new Font(activity).FrankBookRegular());
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView img_back = (ImageView) v.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        RelativeLayout mainlayout = (RelativeLayout) v.findViewById(R.id.mainlayout);
        mainlayout.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_getDirection.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            mapAsyncer();
        } catch (Exception e) {
        }

    }

    public Map_Fragment_Single setData(String lat, String longe){
        this.lat = Double.parseDouble(lat);
        this.longe = Double.parseDouble(longe);
        return this;
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
                LatLng sydney = new LatLng(lat,longe);

                //TODO:reduce marker size and tilt the map
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, longe))
                        .anchor(0.5f, 0.5f)
                        .title("HI this is tile")
                        .snippet("A new Snippet")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin)));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity) {
                            @Override
                            public void leftButtonClick() {
                                googleMapCall();
                                this.dismiss();
                            }

                            @Override
                            public void rightButtonClick() {
                                this.dismiss();
                            }
                        };
                        customAlertDialog.setMessage(getString(R.string.closeformap));
                        customAlertDialog.show();

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



    void googleMapCall(){
        Uri routeUri = Uri.parse("http://maps.google.com/maps?saddr="+activity.getlatlong()[0]+","+activity.getlatlong()[1]+"&daddr="+lat+","+longe);
        Log.e(TAG , " : "+routeUri);
        Intent i = new Intent(Intent.ACTION_VIEW, routeUri);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainlayout:
                break;
            case R.id.img_back:
                activity.onBackPressed();
                break;
            case R.id.txt_getDirection:
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity) {
                    @Override
                    public void leftButtonClick() {
                        googleMapCall();
                        this.dismiss();
                    }

                    @Override
                    public void rightButtonClick() {
                        this.dismiss();
                    }
                };
                customAlertDialog.setMessage(getString(R.string.closeformap));
                customAlertDialog.show();
                break;
        }
    }
}

