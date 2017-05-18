package com.scenekey.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Services.TrackGPS;
import com.scenekey.Utility.CusDialogProg;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.Permission;
import com.scenekey.Utility.Util;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.fragments.Add_Event_Fragmet;
import com.scenekey.fragments.Event_Fragment;
import com.scenekey.fragments.Home_no_Event;
import com.scenekey.fragments.Map_Fragment;
import com.scenekey.fragments.NearEvent_Fargment;
import com.scenekey.fragments.Trending_Fragment;
import com.scenekey.helper.Constants;
import com.scenekey.helper.SessionManager;
import com.scenekey.lib_sources.arc_menu.ArcMenu;
import com.scenekey.lib_sources.arc_menu.widget.FloatingActionButton;
import com.scenekey.models.Events;
import com.scenekey.models.UserInfo;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mindiii on 10/4/17.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int CALLBACK_ACTIVITY = 0;
    static final String TAG = HomeActivity.class.toString();
    public static HomeActivity instance;
    public static int ActivityWidth;
    public static int ActivityHeight;
    public Permission permission;
    RelativeLayout rtlv_one, rtlv_two, rtlv_three, rtlv_four, rtlv_five;
    /*TextView        txt_one, txt_two ,txt_three,txt_four,txt_five;*/
    RelativeLayout lastclicked;
    FrameLayout frame_fragments_a3, frm_bottmbar;
    boolean doublebackpress;
    SessionManager sessionManager;
    UserInfo userInfo;
    Mytask mytask;
    ArrayList<Events> eventsArrayList;
    ArrayList<Events> eventsNearbyList;
    boolean eventAvailable;
    CircleImageView img_f1_profile;
    Map_Fragment map_fragment;
    private Font font;
    private CusDialogProg cusDialogProg;
    private double latitude, longiude;
    private TextView txt_f1_title;
    private TrackGPS gps;
    private RelativeLayout title_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, R.anim.fade_out);
        setContentView(R.layout.a3_home_activity);
        Util.setStatusBarColor(this, R.color.colorPrimary);
        txt_f1_title = (TextView) findViewById(R.id.txt_f1_title);
        title_view = (RelativeLayout) findViewById(R.id.title_view);
        img_f1_profile = (CircleImageView) findViewById(R.id.img_f1_profile);
        instance = HomeActivity.this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        ActivityWidth = width;
        ActivityHeight = height;
        cusDialogProg = new CusDialogProg(this, R.layout.custom_progress_dialog_layout);

        /*************************** Arc Menu *****************************************/
        ArcMenu arcMenu = (ArcMenu) findViewById(R.id.arcMenuX);/*
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) arcMenu.getLayoutParams();
        layoutParams.setMargins(0,0,-(width/2),-(height/2));
        arcMenu.setLayoutParams(layoutParams);*/
        arcMenu.setToolTipTextSize(14);
        arcMenu.setMinRadius(((width - ((int) getResources().getDimension(R.dimen.fab_size_normal2))) / 4));
        arcMenu.setArc(177, 273);
        arcMenu.setToolTipSide(ArcMenu.TOOLTIP_LEFT);
        arcMenu.setToolTipTextColor(Color.BLACK);
        arcMenu.setToolTipBackColor(Color.parseColor("#88000000"));
        arcMenu.setToolTipCorner(2);
        arcMenu.setToolTipPadding(10);
        arcMenu.setColorNormal(getResources().getColor(R.color.colorPrimary));
        arcMenu.showTooltip(true);
        arcMenu.setDuration(ArcMenu.ArcMenuDuration.LENGTH_LONG);
        arcMenu.setAnim(500, 500, ArcMenu.ANIM_MIDDLE_TO_DOWN, ArcMenu.ANIM_MIDDLE_TO_RIGHT,
                ArcMenu.ANIM_INTERPOLATOR_ANTICIPATE, ArcMenu.ANIM_INTERPOLATOR_ANTICIPATE);

        final int[] ITEM_DRAWABLES = {R.drawable.image_defult_profile, R.drawable.image_defult_profile, R.drawable.image_defult_profile};

        String[] str = {"Existing Event", "Add Venue", "Add Event"};


        initArcMenu(arcMenu, str, ITEM_DRAWABLES, ITEM_DRAWABLES.length);

    }

    /************************************
     * Arc Menu
     *********************************/
    private void initArcMenu(final ArcMenu menu, final String[] str, int[] itemDrawables, int count) {
        for (int i = 0; i < count; i++) {
            FloatingActionButton item = new FloatingActionButton(this);
            item.setSize(FloatingActionButton.SIZE_MINI);
            item.setIcon(itemDrawables[i]);
            item.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            menu.setChildSize(item.getIntrinsicHeight());

            final int position = i;
            menu.addItem(item, str[i], new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this, str[position],
                            Toast.LENGTH_SHORT).show();
                    if (position == 1) {
                        menu.menuOut();
                    }
                }
            });
        }
    }

    /********************************************************************************/

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        sessionManager = new SessionManager(instance);
        userInfo = sessionManager.getUserInfo();
        setViews();
        dimmedEffet();
        permission = new Permission(this, instance);
        permission.askForGps();
        gps = new TrackGPS(instance) {
            @Override
            public String[] onLocationUpdate(double latitude, double longitude) {
                instance.latitude = latitude;
                instance.longiude = longitude;
                Log.e(TAG, "Lat " + latitude + " Long" + longitude);
                return new String[0];
            }
        };
        Log.e(TAG, " : " + gps.getLongitude() + " : " + gps.getLatitude());

        mytask = new Mytask();
        mytask.execute("");
        //TODO setting the page according events
        //TODO progress bar for trending and till getting the list.
        rtlv_three.callOnClick();
        checkEventAvailablity(true);

    }

    void setViews() {
        rtlv_one = (RelativeLayout) findViewById(R.id.rtlv_one);
        rtlv_two = (RelativeLayout) findViewById(R.id.rtlv_two);
        rtlv_three = (RelativeLayout) findViewById(R.id.rtlv_three);
        rtlv_four = (RelativeLayout) findViewById(R.id.rtlv_four);
        rtlv_five = (RelativeLayout) findViewById(R.id.rtlv_five);
        frame_fragments_a3 = (FrameLayout) findViewById(R.id.frame_fragments_a3);
        frm_bottmbar = (FrameLayout) findViewById(R.id.frm_bottmbar);
        dimmedEffet();
        setOnClick(rtlv_one, rtlv_two, rtlv_three, rtlv_four, rtlv_five);

        dimmedEffet();
        font = new Font(this);
        font.setFontFranklinRegular(getAllTextView(rtlv_one, rtlv_two, rtlv_three, rtlv_four, rtlv_five));
        font.setFontFranklinRegular(txt_f1_title);
        try {
            Picasso.with(this).load(userInfo.getUserImage()).into(img_f1_profile);
        } catch (Exception e) {
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rtlv_one:
                setBottombar((RelativeLayout) v, lastclicked);
                replaceFragment(new Trending_Fragment());
                break;
            case R.id.rtlv_two:
                setBottombar((RelativeLayout) v, lastclicked);
                if (map_fragment == null) map_fragment = new Map_Fragment();
                replaceFragment(map_fragment);
                break;
            case R.id.rtlv_three:
                setBottombar((RelativeLayout) v, lastclicked);
                try {
                    if (eventsNearbyList.size() <= 0) replaceFragment(new Home_no_Event());
                    else onNearByEventFound();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                ;
                break;
            case R.id.rtlv_four:
                setBottombar((RelativeLayout) v, lastclicked);

                break;
            case R.id.rtlv_five:
                setBottombar((RelativeLayout) v, lastclicked);
                replaceFragment(new Add_Event_Fragmet());
                break;
        }

    }

    void setOnClick(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    void setBottombar(RelativeLayout v, final RelativeLayout lastclicked) {
        setRtlvText(v, true);
        if (lastclicked != null) {

            setRtlvText(lastclicked, false);

            switch (lastclicked.getId()) {
                case R.id.rtlv_one:
                    getImgV(lastclicked).setImageResource(R.drawable.trending_icon);
                    break;
                case R.id.rtlv_two:
                    getImgV(lastclicked).setImageResource(R.drawable.map_icon);
                    break;
                case R.id.rtlv_three:
                    getImgV(lastclicked).setImageResource(R.drawable.key_icon);
                    break;
                case R.id.rtlv_four:
                    getImgV(lastclicked).setImageResource(R.drawable.search_icon);
                    break;
                case R.id.rtlv_five:
                    getImgV(lastclicked).setImageResource(R.drawable.add_icon);
                    break;

            }

        }
        switch (v.getId()) {
            case R.id.rtlv_one:
                getImgV(v).setImageResource(R.drawable.selected_trending_icon);
                break;
            case R.id.rtlv_two:
                getImgV(v).setImageResource(R.drawable.selected_map_icon);
                break;
            case R.id.rtlv_three:
                getImgV(v).setImageResource(R.drawable.selected_key_icon);
                break;
            case R.id.rtlv_four:
                getImgV(v).setImageResource(R.drawable.selected_search_icon);
                break;
            case R.id.rtlv_five:
                getImgV(v).setImageResource(R.drawable.selected_add_icon);
                break;
        }


        this.lastclicked = v;
    }

    void setRtlvText(RelativeLayout rtlv, boolean isclicked) {
        if (isclicked) {
            ((TextView) rtlv.getChildAt(1)).setTextColor(getResources().getColor(R.color.bottom_selected));
        } else {
            ((TextView) rtlv.getChildAt(1)).setTextColor(getResources().getColor(R.color.bottom_unselected));
        }

    }

    ImageView getImgV(RelativeLayout rtlv) {
        return ((ImageView) rtlv.getChildAt(0));
    }

    TextView[] getAllTextView(RelativeLayout... rtlv) {
        TextView[] result = new TextView[rtlv.length];
        int i = 0;
        for (RelativeLayout rt : rtlv) {
            result[i] = ((TextView) rt.getChildAt(1));
            i++;
        }
        return result;
    }


    void replaceFragment(Fragment fragmentHolder) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        String fragmentName = fragmentHolder.getClass().getName();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_fragments_a3, fragmentHolder).addToBackStack(fragmentName);
        fragmentTransaction.commit();
        hideKeyBoard();
    }

    public Fragment addFragment(Fragment fragmentHolder, int animationValue) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentName = fragmentHolder.getClass().getName();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (animationValue == 0) {

            fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_out_down, R.anim.slide_in_down);
        }
        if (animationValue == 1)
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }
        fragmentTransaction.add(R.id.frame_fragments_a3, fragmentHolder).addToBackStack(fragmentName);
        fragmentTransaction.commit();
        hideKeyBoard();
        return fragmentHolder;
    }

    public Fragment addFragmentExplde(Fragment fragmentHolder, String fragmentName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fab_scale_up, R.anim.fab_scale_down);
        fragmentTransaction.add(R.id.frame_fragments_a3, fragmentHolder).addToBackStack(fragmentName);
        fragmentTransaction.commit();
        hideKeyBoard();
        return fragmentHolder;
    }

    @Override
    public void onBackPressed() {
        Handler handler = new Handler();
        Runnable runnable;
        Log.e(TAG, "" + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    doublebackpress = false;
                }
            }, 1000);
            if (doublebackpress) {
                handler.removeCallbacks(runnable);
                finish();
            } else {
                doublebackpress = true;
                super.onBackPressed();

            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.alertDoubletap), Toast.LENGTH_SHORT).show();
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    doublebackpress = false;
                }
            }, 1000);
            if (doublebackpress) {
                handler.removeCallbacks(runnable);
                finish();
            } else {
                doublebackpress = true;
            }
        }


    }

    public void hideKeyBoard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

    void dimmedEffet() {
        Animation animat1 = AnimationUtils.loadAnimation(this, R.anim.one_fade);
        Animation animat2 = AnimationUtils.loadAnimation(this, R.anim.two_fade);
        Animation animat3 = AnimationUtils.loadAnimation(this, R.anim.three_fade);
        Animation animat4 = AnimationUtils.loadAnimation(this, R.anim.four_fade);

        getImgV(rtlv_one).setAnimation(animat1);
        getImgV(rtlv_two).setAnimation(animat2);
        getImgV(rtlv_four).setAnimation(animat3);
        getImgV(rtlv_five).setAnimation(animat4);

    }

    /***
     * @param ViewDot One of {  #VISIBLE}, { #INVISIBLE}, or { #GONE}.
     */
    public void setBBvisiblity(int ViewDot) {
        frm_bottmbar.setVisibility(ViewDot);
        if (ViewDot == View.GONE) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frame_fragments_a3.getLayoutParams();
            layoutParams.bottomMargin = 0;
            title_view.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frame_fragments_a3.getLayoutParams();
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.bottomBar_margin);
            title_view.setVisibility(View.VISIBLE);
        }
    }

    public void setBBvisiblity(final int ViewDot, final int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ViewDot == View.GONE) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frame_fragments_a3.getLayoutParams();
                    layoutParams.bottomMargin = 0;
                    title_view.setVisibility(View.GONE);
                    frm_bottmbar.setVisibility(View.GONE);

                } else {
                    Animation animation = AnimationUtils.loadAnimation(instance, R.anim.slide_up);
                    Animation animation1 = AnimationUtils.loadAnimation(instance, R.anim.slide_in_up);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frame_fragments_a3.getLayoutParams();
                    layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.bottomBar_margin);
                    title_view.setVisibility(View.VISIBLE);
                    title_view.startAnimation(animation1);
                    frm_bottmbar.setVisibility(View.VISIBLE);
                    frm_bottmbar.startAnimation(animation);
                }
            }
        }, delay);

    }

    public void showProgDilog(boolean b) {
        cusDialogProg.setCanceledOnTouchOutside(b);
        cusDialogProg.setCancelable(b);
        cusDialogProg.show();

    }

    public void dismissProgDailog() {
        if (cusDialogProg != null) cusDialogProg.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CALLBACK_ACTIVITY == Constants.CALL_BACK_GPS) {
            permission.askForGps();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_CAMERA) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                try {
                    ((Event_Fragment) fragment).onRequestPermissionsResult(requestCode, permissions, grantResults);
                    ;
                } catch (Exception e) {

                }
            }
        }
    }

    void checkEventAvailablity(final boolean showProgress) {
        cusDialogProg.setCancelable(false);
        cusDialogProg.setCanceledOnTouchOutside(false);
        cusDialogProg.setColor(R.color.transparent);
        if (showProgress) cusDialogProg.show();

        VolleyGetPost volleyGetPost = new VolleyGetPost(instance, instance, WebService.EVENT_BY_LOCAL, false) {
            @Override
            public void onVolleyResponse(String response) {
                Util.printBigLogcat(TAG, " " + response);
                try {
                    JSONObject jo = new JSONObject(response);
                    if (jo.has("status")) {
                        int status = jo.getInt("status");
                        if (status == 0) eventAvailable = false;
                        //else if()
                    } else {
                        if (jo.has("userinfo")) {
                        }
                        if (jo.has("events")) {
                            if (eventsArrayList == null) eventsArrayList = new ArrayList<>();
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
                                eventsArrayList.add(events);
                                //Log.e("Size",eventsArrayList.size()+"");
                            }
                            try {
                                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                                    if (fragment instanceof Map_Fragment)
                                        if (map_fragment != null) map_fragment.notifyAdapter();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            checkedEventtoJoint();
                            if (showProgress) rtlv_three.callOnClick();
                        }
                    }
                    if (cusDialogProg != null) cusDialogProg.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (cusDialogProg != null) cusDialogProg.dismiss();
                }

            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (cusDialogProg != null) cusDialogProg.dismiss();
                Log.e(TAG, "" + error);
            }

            @Override
            public void onNetError() {
                if (cusDialogProg != null) cusDialogProg.dismiss();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("lat", "38.222046");
                params.put("long", "-122.144755");
                params.put("user_id", userInfo.getUserID());
               /* params.put("lat",latitude+"");
                params.put("long",longiude+"");
                params.put("user_id",userInfo.getUserID());*/
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

    public ArrayList<Events> getEventsArrayList() {
        return eventsArrayList;
    }

    public void setEventsArrayList(ArrayList<Events> eventsArrayList) {
        this.eventsArrayList = eventsArrayList;
    }

    public String[] getlatlong() {
        return new String[]{latitude + "", longiude + ""};
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setTitle(String title) {
        txt_f1_title.setText(title);
    }

    public void setTitleVisibality(int visibality) {
        title_view.setVisibility(visibality);
    }

    /***
     * This method is to check nearby events accouding to given conditions i.e, user should be within 200 meter radius from the event
     * && the event should be started i.e, user current time should be less then end time and should be greater then Start time.
     * if both the conditions are fullfiled then te event will show insted of the try demo screen.
     */
    public void checkedEventtoJoint() {
        if (eventsNearbyList == null) eventsNearbyList = new ArrayList<>();
        else eventsNearbyList.clear();
        for (Events events : eventsArrayList) {
            Location location = new Location("Location end");
            location.setLatitude(Double.parseDouble(events.getVenue().getLatitude()));
            location.setLongitude(Double.parseDouble(events.getVenue().getLongitude()));
            Location myLocation = new Location("Location My");
            myLocation.setLatitude(38.222046D);
            myLocation.setLongitude(-122.144755D);
            double distance = location.distanceTo(myLocation);
            try {
                if (distance <= Constants.MAXIMUM_DISTANCE && checkWithTime(events.getEvent().getEvent_date())) {
                    eventsNearbyList.add(events);

                }
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(this, getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_LONG).show();
            }
        }

    }


    /*********************************
     * Chekcing the Events
     ******************************************************/

    public boolean checkWithTime(final String date) throws ParseException {
        Log.e(TAG, date);
        String[] dateSplit;
        if (date.contains("T")) dateSplit = (date.replace("TO", " ")).replace("T", " ").split(" ");
        else {
            String date2 = date.replace("TO", " ");
            Log.e(TAG, date2);
            dateSplit = date2.split("\\s?");
        }

        Date startTime = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(dateSplit[0] + " " + dateSplit[1]);

        Date endTime = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(dateSplit[0] + " " + dateSplit[2]);
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();

        //if(dateSplit[0].equals(new SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime()))  ){
        //  Log.e(TAG," Time current"+ new SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime())+" : "+timeStart+" : "+endTime);
            /*checkTime(timeStart,endTime);*/
        //}
        //return checkTime(timeStart,endTime,EventIdandName);

        if (currentTime < endTime.getTime() && currentTime > startTime.getTime()) {
            return true;
        }
        return false;
    }

    boolean onNearByEventFound() {
        Log.e(TAG, eventsNearbyList.size() + " : ");
        if (eventsNearbyList.size() >= 1) {
            NearEvent_Fargment nearEvent_fargment = new NearEvent_Fargment();
            nearEvent_fargment.setEventsList(eventsNearbyList);
            replaceFragment(nearEvent_fargment);
            return false;
        } else return true;
    }

    //Not using this right now
    /*public boolean checkTime(final Date startTime,final Date endTime ,String name){
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
        Log.e(TAG," Sta "+startTime.getTime() +"\n end "+endTime.getTime()+"\n Cur "+ currentTime + "Event ID "+name);
        if(currentTime<endTime.getTime() && currentTime>startTime.getTime()){
            return  true;
        }


        return false;
    }*/

    /******************************************************************************************************************************/

    public double phpDistance(Double[] LL) {
        return 6371000 * (Math.acos(Math.cos(Math.toRadians(LL[0])) * Math.cos(Math.toRadians(LL[2])) * Math.cos(Math.toRadians(LL[3]) - Math.toRadians(LL[1])) + Math.sin(Math.toRadians(LL[0])) * Math.sin(Math.toRadians(LL[2]))));
    }

    class Mytask extends AsyncTask<String, Void, double[]> {
        @Override
        protected double[] doInBackground(String... params) {
            while (true) {

                if (latitude == 0.0d && longiude == 0.0d) {
                    latitude = gps.getLatitude();
                    longiude = gps.getLongitude();
                } else break;
            }
            return new double[]{latitude, longiude};
        }

        @Override
        protected void onPostExecute(double[] s) {
            //checkEventAvailablity(false);
            super.onPostExecute(s);
        }
    }


}
