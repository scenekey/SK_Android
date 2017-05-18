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
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.MapInfoAdapter;
import com.scenekey.models.Events;

import java.util.ArrayList;

/**
 * Created by mindiii on 27/4/17.
 */

public class Map_Fragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private static final String TAG = Map_Fragment.class.toString();
    HomeActivity activity;
    private MapView mMapView;
    private GoogleMap googleMap;
    private MapInfoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f1_map_event, null);
        mMapView = (MapView) v.findViewById(R.id.map_view);
        activity = HomeActivity.instance;
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
        activity.setTitleVisibality(View.VISIBLE);
        activity.setTitle(activity.getResources().getString(R.string.Scene));
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            mapAsyncer();
        } catch (Exception e) {
        }

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
                .title("HI this is tile")
                .snippet("A new Snippet")
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
                LatLng sydney = new LatLng(Double.parseDouble(activity.getlatlong()[0]), Double.parseDouble(activity.getlatlong()[1]));

                //TODO:reduce marker size and tilt the map
                //marker.icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.resizeBitmap( ImageUtil.getBitmapByUrl(markerUrl) , 2 ,2)));
                final ArrayList<Events> markersArray = activity.getEventsArrayList();
                try {


                    if (markersArray == null || markersArray.size() <= 0) {
                        Log.e(TAG, " Size 0");

                    } else {
                        adapter = new MapInfoAdapter(activity, markersArray);
                        googleMap.setInfoWindowAdapter(adapter);
                        for (int i = 0; i < markersArray.size(); i++) {
                            Log.e(TAG, markersArray.get(i).getEvent().getEvent_name() + " : " + markersArray.get(i).getEvent().getEvent_id() + " : " + markersArray.get(i).getVenue().getLatitude() + " : " + markersArray.get(i).getVenue().getLongitude());
                            if (i == (markersArray.size() - 1)) {
                                sydney = new LatLng(Double.parseDouble(markersArray.get(i).getVenue().getLatitude()), Double.parseDouble(markersArray.get(i).getVenue().getLongitude()));
                                m = createMarker(markersArray.get(i).getVenue().getLatitude(), markersArray.get(i).getVenue().getLongitude());
                            } else
                                createMarker(markersArray.get(i).getVenue().getLatitude(), markersArray.get(i).getVenue().getLongitude());
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
                        Events events = markersArray.get(Integer.parseInt(marker.getId().replace("m", "")));
                        Event_Fragment frg = new Event_Fragment();
                        frg.setEventId(events.getEvent().getEvent_id());
                        activity.addFragment(frg, 0);
                        activity.setBBvisiblity(View.GONE);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //TODO:DELETE BEFORE LIVE
                       /* Events events = markersArray.get(Integer.parseInt(marker.getId().replace("m","")));
                        Log.e(TAG,"Info window clicked"+marker.getId()+":"+events.getVenue().getLatitude()+"="+marker.getPosition()+" :"+events.getVenue().getLongitude());*/
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
            adapter.setEventArrayList(activity.getEventsArrayList());
        }
    }
}
