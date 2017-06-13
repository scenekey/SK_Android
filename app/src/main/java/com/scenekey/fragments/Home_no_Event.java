package com.scenekey.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.Permission;
import com.scenekey.activity.HomeActivity;
import com.scenekey.helper.SessionManager;
import com.scenekey.models.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mindiii on 11/4/17.
 */

public class Home_no_Event extends Fragment implements View.OnClickListener {

    public static final String TAG = Home_no_Event.class.toString();
    TextView txt_try_button_f1;
    HomeActivity activity;
    Context context;
    Permission permission;
    Demo_Event_Fragment event_fragment;
    /*Demo_Event_Fragment_ListView event_fragment_listView;*/
    Bitmap imageArrray[];
    private Font font;
    private TextView txt_sorry1;
    private TextView txt_sorry2;
    boolean clicked;

    public Bitmap[] getImageArrray() {
        return imageArrray;
    }

    public void setImageArrray(Bitmap[] imageArrray) {
        this.imageArrray = imageArrray;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f1_home_no_event, null);
        txt_try_button_f1 = (TextView) v.findViewById(R.id.txt_try_button_f1);


        txt_sorry2 = (TextView) v.findViewById(R.id.txt_sorry2);
        txt_sorry1 = (TextView) v.findViewById(R.id.txt_sorry1);
        setOnClick(txt_try_button_f1);
        activity = (HomeActivity) getActivity();
        try {
            createBitmap();
        } catch (Exception e) {

        }
        /*new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }
        }.execute();*/
        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_try_button_f1:
                if (!clicked) {
                    activity.showProgDilog(false,TAG);

                    activity.addFragment(event_fragment, 0);
//                    activity.addFragment(event_fragment_listView, 0);
                    event_fragment.setImageArrray(this);
//                    event_fragment_listView.setImageArrray(this);
                    clicked = true;
                }

                try {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clicked = false;
                        }
                    }, 2000);
                } catch (Exception e) {
                }
                //activity.getSupportFragmentManager().executePendingTransactions();
                break;


        }
    }

    void setOnClick(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        event_fragment = new Demo_Event_Fragment();
//        event_fragment_listView = new Demo_Event_Fragment_ListView();
        activity.setTitle(activity.getResources().getString(R.string.Enter));

    }

    @Override
    public void onStart() {
        activity.setBBvisiblity(View.VISIBLE,TAG);
        super.onStart();
        font = new Font(getContext());
        font.setFontFranklinRegular(txt_try_button_f1);
        font.setFontFrankDemiReg(txt_sorry1, txt_sorry2);
        // Working String date = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String date = new SimpleDateFormat("MMMM dd,yyyy").format(Calendar.getInstance().getTime());
        Log.e("Date", date + " 8:00 AM - 12:00 PM");
        permission = new Permission(activity, HomeActivity.instance);
        permission.checkLocationPermission();
        //activity.startService(new Intent(activity, TrackGPS.class));
        // activity.startService(new Intent(activity.getApplicationContext(), TrackGPS.class));
        //Log.e("Lat log",Gps.getLatitude()+" "+Gps.getLongitude());

    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setBBvisiblity(View.VISIBLE,TAG);
        activity.setTitleVisibality(View.VISIBLE);
    }

    void createBitmap() {
        imageArrray = new Bitmap[9];
        imageArrray[0] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_1)).getBitmap());
        imageArrray[1] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_2)).getBitmap());
        imageArrray[2] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_3)).getBitmap());
        imageArrray[3] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_4)).getBitmap());
        imageArrray[4] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_5)).getBitmap());
        imageArrray[5] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_6)).getBitmap());
        imageArrray[6] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_7)).getBitmap());
        imageArrray[7] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_8)).getBitmap());
        imageArrray[8] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_8)).getBitmap());

    }


}
