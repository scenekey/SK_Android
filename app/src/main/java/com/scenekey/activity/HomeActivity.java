package com.scenekey.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.scenekey.R;
import com.scenekey.Utility.CusDialogProg;
import com.scenekey.Utility.CustomToastDialog;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.Permission;
import com.scenekey.Utility.Util;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.fragments.Add_Event_Fragmet;
import com.scenekey.fragments.Event_Fragment;
import com.scenekey.fragments.Home_no_Event;
import com.scenekey.fragments.Key_In_Event_Fragment;
import com.scenekey.fragments.Map_Fragment;
import com.scenekey.fragments.NearEvent_Fargment;
import com.scenekey.fragments.Profile_Fragment;
import com.scenekey.fragments.Search_Fragment;
import com.scenekey.fragments.Trending_Fragment;
import com.scenekey.fragments.UpdateFragment;
import com.scenekey.helper.Constants;
import com.scenekey.helper.SessionManager;
import com.scenekey.lib_sources.arc_menu.ArcMenu;
import com.scenekey.lib_sources.arc_menu.widget.FloatingActionButton;
import com.scenekey.models.EventAttendy;
import com.scenekey.models.Events;
import com.scenekey.models.UserInfo;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mindiii on 10/4/17.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final int CALLBACK_ACTIVITY = 0;
    static final String TAG = HomeActivity.class.toString();
    public static HomeActivity instance;
    public static int ActivityWidth;
    public static int ActivityHeight;
    public Permission permission;
    RelativeLayout rtlv_one, rtlv_two, rtlv_three, rtlv_four, rtlv_five;
    RelativeLayout lastclicked;
    FrameLayout frame_fragments_a3;
    private static FrameLayout frm_bottmbar;
    boolean doublebackpress;
    SessionManager sessionManager;
    static UserInfo userInfo;
    ArrayList<Events> eventsArrayList;
    ArrayList<Events> eventsNearbyList;
    boolean eventAvailable;
    CircleImageView img_f1_profile;
    Map_Fragment map_fragment;
    private Font font;
    private static CusDialogProg cusDialogProg;
    private double latitude, longiude;
    private TextView txt_f1_title;

    private RelativeLayout title_view;
    View view;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, R.anim.fade_out);
        setContentView(R.layout.a3_home_activity);
        view = this.getWindow().getDecorView();
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

        //TODO TEMP
        Log.e(TAG, " FIREBASE " + FirebaseInstanceId.getInstance().getToken());

        /*************************** Arc Menu *****************************************/
        ArcMenu arcMenu = (ArcMenu) findViewById(R.id.arcMenuX);

        /*
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) arcMenu.getLayoutParams();
        layoutParams.setMargins(0,0,-(width/2),-(height/2));
        arcMenu.setLayoutParams(layoutParams);
        */
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
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000);
        locationRequest.setFastestInterval(15 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        googleApiClient.connect();
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            latitude = location.getLatitude();
            longiude = location.getLongitude();
            Intent in = getIntent();
            latitude = Double.parseDouble(in.getStringExtra(Constants.LATITUDE));
            longiude = Double.parseDouble(in.getStringExtra(Constants.LONGITUDE));
        }
        catch (NullPointerException e){

        }


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


    public UserInfo userInfo(){
        if(userInfo == null) {
            if(sessionManager == null) sessionManager = new SessionManager(getApplicationContext());
                userInfo = sessionManager.getUserInfo();
        }
        return userInfo;
    }
    /********************************************************************************/

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        sessionManager = new SessionManager(instance);
        userInfo = sessionManager.getUserInfo();
        setViews();
        googleApiClient.connect();
        dimmedEffet();
        permission = new Permission(this, instance);
        permission.askForGps();
        permission.checkLocationPermission();
        permission.checkForCamera();
        replaceFragment(new Home_no_Event());
        if(userInfo.getMakeAdmin().equals(Constants.ADMIN_YES)){
            try {
                latitude = Double.parseDouble(userInfo.getLatitude());
                longiude = Double.parseDouble(userInfo.getLongitude());
            }catch (Exception e){

            }

        }
        rtlv_three.callOnClick();

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }


    void setViews() {
        rtlv_one = (RelativeLayout) findViewById(R.id.rtlv_one);
        rtlv_two = (RelativeLayout) findViewById(R.id.rtlv_two);
        rtlv_three = (RelativeLayout) findViewById(R.id.rtlv_three);
        rtlv_four = (RelativeLayout) findViewById(R.id.rtlv_four);
        rtlv_five = (RelativeLayout) findViewById(R.id.rtlv_five);
        frame_fragments_a3 = (FrameLayout) findViewById(R.id.frame_fragments_a3);
        frm_bottmbar = (FrameLayout) findViewById(R.id.frm_bottmbar);
        setOnClick(rtlv_one,
                rtlv_two,
                rtlv_three,
                rtlv_four,
                rtlv_five,
                img_f1_profile);
        font = new Font(this);
        font.setFontFranklinRegular(getAllTextView(rtlv_one,
                rtlv_two,
                rtlv_three,
                rtlv_four,
                rtlv_five));
        font.setFontFranklinRegular(txt_f1_title);
        try {
            Picasso.with(this).load(userInfo.getUserImage()).placeholder(R.drawable.image_defult_profile).into(img_f1_profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rtlv_one:
                position =1;
                setBottombar((RelativeLayout) v, lastclicked);
                replaceFragment(new Trending_Fragment());
                break;
            case R.id.rtlv_two:
                position =2;
                setBottombar((RelativeLayout) v, lastclicked);
                if (map_fragment == null) map_fragment = new Map_Fragment();
                replaceFragment(map_fragment);
                break;
            case R.id.rtlv_three:
                position =3;
                replaceFragment(new Home_no_Event());
                setBottombar((RelativeLayout) v, lastclicked);
                if(eventsArrayList != null)eventsArrayList.clear();
                if(eventsNearbyList != null)eventsNearbyList.clear();
                if(latitude == 0.0D && longiude == 0.0D){
                    if(userInfo.getMakeAdmin().equals(Constants.ADMIN_YES)){
                        try{
                            checkEventAvailablity(true);
                        }catch (IllegalStateException e){

                        }catch (IllegalArgumentException e){

                        }
                    }else try{
                        CustomToastDialog customToastDialog = new CustomToastDialog(this);
                        customToastDialog.setMessage(getString(R.string.elocatoion));
                        customToastDialog.show();
                        requestLocationUpdates();
                    }catch(IllegalStateException e){

                    }

                }
                else {
                    try{
                        checkEventAvailablity(true);
                    }
                    catch (IllegalArgumentException e){

                    }
                    catch (IllegalStateException e){

                    }
                }
                break;
            case R.id.rtlv_four:
                position =4;
                setBottombar((RelativeLayout) v, lastclicked);
                replaceFragment(new Search_Fragment());
                break;
            case R.id.rtlv_five:
                position =5;
                setBottombar((RelativeLayout) v, lastclicked);
                replaceFragment(new Add_Event_Fragmet());
                break;
            case R.id.img_f1_profile:
                try {
                    EventAttendy attendy = new EventAttendy();
                    attendy.setUserid(userInfo.getUserID());
                    attendy.setUserFacebookId(userInfo.getFacebookId());
                    attendy.setUserimage(userInfo.getUserImage());
                    attendy.setUsername(userInfo.getUserName());
                    addFragment(new Profile_Fragment().setData(attendy, true, null,0), 1);
                }catch (Exception e){

                }
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
                    getImgV(lastclicked).setImageResource(R.drawable.ic_trending_icon);
                    break;
                case R.id.rtlv_two:
                    getImgV(lastclicked).setImageResource(R.drawable.ic_map_icon);
                    break;
                case R.id.rtlv_three:
                    getImgV(lastclicked).setImageResource(R.drawable.key_icon);
                    break;
                case R.id.rtlv_four:
                    getImgV(lastclicked).setImageResource(R.drawable.ic_search_icon);
                    break;
                case R.id.rtlv_five:
                    getImgV(lastclicked).setImageResource(R.drawable.ic_add_icon);
                    break;

            }

        }
        switch (v.getId()) {
            case R.id.rtlv_one:
                getImgV(v).setImageResource(R.drawable.ic_selected_trending_icon);
                break;
            case R.id.rtlv_two:
                getImgV(v).setImageResource(R.drawable.ic_selected_map_icon);
                break;
            case R.id.rtlv_three:
                getImgV(v).setImageResource(R.drawable.selected_key_icon);
                break;
            case R.id.rtlv_four:
                getImgV(v).setImageResource(R.drawable.ic_selected_search_icon);
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
        hideKeyBoard();
        Handler handler = new Handler();
        Runnable runnable;
        Log.e(TAG, "" + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
           /* handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    doublebackpress = false;
                }
            }, 1000);
            if (doublebackpress) {
                handler.removeCallbacks(runnable);
                //finish();
            } else {
                doublebackpress = false;
                super.onBackPressed();

            }*/
            super.onBackPressed();
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
    public void setBBvisiblity(int ViewDot,String TAG) {
        Log.e(TAG," B B visiballity "+ViewDot+" TAG "+TAG);
        try{frm_bottmbar.setVisibility(ViewDot);
        if (ViewDot == View.GONE) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frame_fragments_a3.getLayoutParams();
            layoutParams.bottomMargin = 0;
            title_view.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frame_fragments_a3.getLayoutParams();
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.bottomBar_margin);
            title_view.setVisibility(View.VISIBLE);
        }}
        catch (NullPointerException e){

        }
        catch (IllegalArgumentException e){

        }
    }

    public void setBBvisiblity(final int ViewDot, final int delay,String TAG) {
        Log.e(TAG," B B visiballity "+ViewDot+" TAG "+TAG);
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

    public void showProgDilog(boolean b ,String TAG) {
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
           if(getSupportFragmentManager().getFragments() != null ) {for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                try {
                    ((Event_Fragment) fragment).onRequestPermissionsResult(requestCode, permissions, grantResults);
                        break;
                } catch (Exception e) {

                }
                try {
                    ((Key_In_Event_Fragment) fragment).onRequestPermissionsResult(requestCode, permissions, grantResults);
                    break;
                } catch (Exception e) {

                }
                try {
                    ((UpdateFragment) fragment).onRequestPermissionsResult(requestCode, permissions, grantResults);
                    break;
                } catch (Exception e) {

                }
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
                        if (status == 0) {
                            dismissProgDailog();
                            replaceFragment(new Home_no_Event());
                            eventAvailable = false;
                            try {
                                Toast.makeText(HomeActivity.this,jo.getString("message"),Toast.LENGTH_SHORT).show();
                            }catch (Exception e){

                            }
                        }
                        if (jo.has("userInfo")) {
                            if(userInfo== null) userInfo = sessionManager.getUserInfo();
                            Object intervention = jo.get("userInfo");
                            if (intervention instanceof JSONArray) {
                                sessionManager.logout(HomeActivity.this);
                            }
                            JSONObject user = jo.getJSONObject("userInfo");
                            if(user.has("makeAdmin"))   userInfo.setMakeAdmin(user.getString("makeAdmin"));
                            if(user.has("lat"))         userInfo.setLatitude(user.getString("lat"));
                            if(user.has("longi"))       userInfo.setLongitude(user.getString("longi"));
                            if(user.has("address"))       userInfo.setAddress(user.getString("address"));
                            updateSession(userInfo);
                        }
                        //else if()
                    } else {
                        if (jo.has("userInfo")) {
                            if(userInfo== null) userInfo = sessionManager.getUserInfo();
                            Object intervention = jo.get("userInfo");
                            if (intervention instanceof JSONArray) {
                                sessionManager.logout(HomeActivity.this);
                            }
                            JSONObject user = jo.getJSONObject("userInfo");
                            if(user.has("makeAdmin"))   userInfo.setMakeAdmin(user.getString("makeAdmin"));
                            if(user.has("lat"))         userInfo.setLatitude(user.getString("lat"));
                            if(user.has("longi"))       userInfo.setLongitude(user.getString("longi"));
                            if(user.has("adminLat"))    userInfo.setLatitude(user.getString("adminLat"));
                            if(user.has("adminLong"))   userInfo.setLongitude(user.getString("adminLong"));
                            if(user.has("address"))     userInfo.setAddress(user.getString("address"));
                            updateSession(userInfo);
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
                            try{
                                checkedEventtoJoint();
                            }catch (IllegalStateException e){

                            }
                            catch (IllegalArgumentException e){

                            }
                            try {
                                if (eventsNearbyList.size() <= 0) replaceFragment(new Home_no_Event());
                                else onNearByEventFound();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                try {
                                    replaceFragment(new Home_no_Event());
                                }catch (Exception ew){

                                }
                            }
                            catch (IllegalArgumentException e){

                            }
                            //if (showProgress) rtlv_three.callOnClick();
                        }
                    }
                    if (cusDialogProg != null) cusDialogProg.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (cusDialogProg != null) cusDialogProg.dismiss();
                    if(showProgress)Toast.makeText(HomeActivity.this,getString(R.string.somethingwentwrong),Toast.LENGTH_SHORT).show();
                }
                try {
                    Picasso.with(HomeActivity.this).load(userInfo.getUserImage()).placeholder(R.drawable.image_defult_profile).into(img_f1_profile);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyError(VolleyError error) {
                replaceFragment(new Home_no_Event());
                if (cusDialogProg != null) cusDialogProg.dismiss();
                if(showProgress)Toast.makeText(HomeActivity.this,getString(R.string.somethingwentwrong),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "" + error);
            }

            @Override
            public void onNetError() {
                if (cusDialogProg != null) cusDialogProg.dismiss();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("lat",getlatlong()[0]);
                params.put("long",getlatlong()[1]);
                params.put("user_id",userInfo.getUserID());
                params.put("updateLocation",Constants.ADMIN_NO);
                Log.e(TAG," params "+params.toString());
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
        String[] result ;
        //TODO remove constant Before LIVE
        if(userInfo.getMakeAdmin().contains(Constants.ADMIN_YES) && islocation()){

            result = new String[]{userInfo.getLatitude(),userInfo.getLongitude()};
        }
        else if(latitude == 0.0D && longiude == 0.0D){
            try{CustomToastDialog customToastDialog = new CustomToastDialog(instance);
            customToastDialog.setMessage("There was an error getting your location");
            customToastDialog.show();}
            catch (Exception e){

            }
            result = new String[]{latitude + "", longiude + ""};
        }
        else{
            result = new String[]{latitude + "", longiude + ""};
        }
        return result;
       // return new String[]{38.222046+"",-122.144755+""};
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void updateSession(UserInfo user){

        sessionManager.createSession(user);
        userInfo = sessionManager.getUserInfo();

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
            myLocation.setLatitude(Double.parseDouble(getlatlong()[0]));
            myLocation.setLongitude(Double.parseDouble(getlatlong()[1]));
            double distance = location.distanceTo(myLocation);
            try {
                if (distance <= Constants.MAXIMUM_DISTANCE && checkWithTime(events.getEvent().getEvent_date() , events.getEvent().getInterval() )) {
                    events.setOngoing(true);
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

    public boolean checkWithTime(final String date , double interval) throws ParseException {
        String[] dateSplit = (date.replace("TO", "T")).replace(" ", "T").split("T");
        Date startTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateSplit[0] + " " + dateSplit[1]);
        Date endTime = new Date(startTime.getTime()+(int)(interval* 60 * 60 * 1000));
        Log.e(TAG, " Date "+date+" : "+startTime+" : "+endTime);
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
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
            try {
                replaceFragment(nearEvent_fargment);
            }catch (Exception e){}
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
        Log.e(TAG, " Distance " + 6371000 * (Math.acos(Math.cos(Math.toRadians(LL[0])) * Math.cos(Math.toRadians(LL[2])) * Math.cos(Math.toRadians(LL[3]) - Math.toRadians(LL[1])) + Math.sin(Math.toRadians(LL[0])) * Math.sin(Math.toRadians(LL[2])))) );
        return 6371000 * (Math.acos(Math.cos(Math.toRadians(LL[0])) * Math.cos(Math.toRadians(LL[2])) * Math.cos(Math.toRadians(LL[3]) - Math.toRadians(LL[1])) + Math.sin(Math.toRadians(LL[0])) * Math.sin(Math.toRadians(LL[2]))));
    }

    public int getDistance(Double[] LL){
        Log.e("LAT LONG ", LL[0]+" "+LL[1]+" "+LL[2]+" "+LL[3]  );
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(LL[0]);
        startPoint.setLongitude(LL[1]);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(LL[2]);
        endPoint.setLongitude(LL[3]);

        double distance=startPoint.distanceTo(endPoint);

        return (int)distance;
    }
    public double getDistanceMile(Double[] LL){
        Log.e("LAT LONG ", LL[0]+" "+LL[1]+" "+LL[2]+" "+LL[3]  );
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(LL[0]);
        startPoint.setLongitude(LL[1]);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(LL[2]);
        endPoint.setLongitude(LL[3]);

        double distance=(startPoint.distanceTo(endPoint))*0.00062137;
        return new BigDecimal(distance ).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    boolean islocation(){
        try{if(Double.parseDouble(userInfo.getLongitude())==0.0D){
            return false;
        }
        else if(Double.parseDouble(userInfo.getLatitude())==0.0D){
            return false;
        }
        else return true;}
        catch (Exception e){
            return false;
        }
    }

    public void showToast(String s)  {
        try {
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        }catch (IllegalStateException e){

        }catch (IllegalArgumentException e){

        }
    }



    /********************************************* Location ***********************************************************************/

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
    byte[] byteArray = byteArrayOutputStream .toByteArray();
    to encode base64 from byte array use following method
    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longiude = location.getLongitude();
    }

    /**
     * Used this to jump from any page to last bottom bar position
     */
    public void backPressToposition(){
        try{
            switch (position){
                case 1:
                    rtlv_one.callOnClick();
                    break;
                case 2:
                    rtlv_two.callOnClick();
                    break;
                case 3:
                    rtlv_three.callOnClick();
                    break;
                case 4:
                    rtlv_four.callOnClick();
                    break;
                case 5:
                    rtlv_five.callOnClick();
                    break;
                default:
                    rtlv_three.callOnClick();
                    break;
            }
        }catch (IllegalStateException e){

        }

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongiude() {
        return longiude;
    }
}
