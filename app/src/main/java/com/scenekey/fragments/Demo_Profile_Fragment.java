package com.scenekey.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.Util;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.ProfileListAdapter;
import com.scenekey.adapter.Profile_Events_Adapter;
import com.scenekey.models.EventAttendy;
import com.scenekey.models.Feeds;
import com.scenekey.models.RoomPerson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mindiii on 28/4/17.
 */

public class Demo_Profile_Fragment extends Fragment implements View.OnClickListener {
    public static final String TAG = Demo_Profile_Fragment.class.toString();
    CircleImageView img_profile_pic2;
    RoomPerson attendy;
    boolean myProfile;
    LinearLayout mainlayout;
    ListView rclv_f3_trending;
    private ImageView img_profile_pic, img_cross, img_left, img_right, img_fb, img_capture;
    private TextView txt_event_count, txt_dimmer, txt_f2_badge, txt_profile_name;
    private ArrayList<Feeds> feedslist;

    GridLayoutManager layoutManager;
    /*Profile_Events_Adapter adapter;*/
    int pageToshow=1;

    ProfileListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f2_profile_own, null);
        mainlayout = (LinearLayout) v.findViewById(R.id.mainlayout);
        rclv_f3_trending = (ListView) v.findViewById(R.id.rclv_f3_trending);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedslist = new ArrayList<>();
        adapter = new ProfileListAdapter(feedslist);
        LayoutInflater inflater = activity().getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_header, rclv_f3_trending, false);
        rclv_f3_trending.addHeaderView(header, null, false);
        img_profile_pic2 = (CircleImageView) view.findViewById(R.id.img_profile_pic2);
        img_profile_pic = (ImageView) header.findViewById(R.id.img_profile_pic);
        img_left = (ImageView) view.findViewById(R.id.img_left);
        img_right = (ImageView) view.findViewById(R.id.img_right);
        img_cross = (ImageView) view.findViewById(R.id.img_cross);
        img_fb = (ImageView) header.findViewById(R.id.img_fb);
        img_capture = (ImageView) header.findViewById(R.id.img_capture);
        ImageView img_setting = (ImageView) header.findViewById(R.id.img_setting);
        ImageView img_back = (ImageView) header.findViewById(R.id.img_back);

        txt_event_count = (TextView) header.findViewById(R.id.txt_event_count);
        txt_dimmer = (TextView) view.findViewById(R.id.txt_dimmer);
        txt_f2_badge = (TextView) header.findViewById(R.id.txt_f2_badge);
        txt_profile_name = (TextView) header.findViewById(R.id.txt_profile_name);
        txt_profile_name.setText(attendy.getAndroid_version_name());
        setClick(view, img_back ,img_setting);
        img_profile_pic.setOnClickListener(this);
        //img_profile_pic.setImageBitmap(new CircleTransform().transform(attendy.getResorceId()));
        //img_profile_pic2.setImageBitmap(new CircleTransform().transform(attendy.getResorceId()));

        Picasso.with(activity()).load(Integer.parseInt(attendy.getAndroid_image_url())).transform(new CircleTransform()).into(img_profile_pic);
        Picasso.with(activity()).load(Integer.parseInt(attendy.getAndroid_image_url())).transform(new CircleTransform()).into(img_profile_pic2);
        txt_dimmer.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        img_profile_pic2.setVisibility(View.INVISIBLE);

        if (myProfile) {
            img_fb.setVisibility(View.GONE);
            txt_f2_badge.setVisibility(View.GONE);

        } else {
            img_capture.setVisibility(View.GONE);
        }
        Font font = new Font(activity());
        font.setFontLibreFranklin_SemiBold(txt_profile_name, txt_event_count);

        adapter.notifyDataSetChanged();
        rclv_f3_trending.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_profile_pic:
                profileImgClick();
                break;
            case R.id.img_cross:
                crossImgClicked();
                break;
            case R.id.img_back:
                activity().onBackPressed();
                break;
            case R.id.img_setting:
                //activity().addFragment(new Setting_Fragment(),1);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    void profileImgClick() {
        Log.e(TAG, "Clicked");
        rclv_f3_trending.smoothScrollToPosition(0);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.profile_pic_scale_up);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                img_profile_pic2.setAlpha(1.0f);
                img_profile_pic2.setVisibility(View.VISIBLE);

                /*Animation dimmer = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_to_full);
                txt_dimmer.startAnimation(dimmer);*/
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_dimmer.setVisibility(View.VISIBLE);
                img_cross.setVisibility(View.VISIBLE);
                img_right.setVisibility(View.VISIBLE);
                img_left.setVisibility(View.VISIBLE);
                img_profile_pic2.setBorderColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e(TAG, "Animation Repeat");
            }
        });

        img_profile_pic2.startAnimation(animation);

    }

    void crossImgClicked() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.profile_pic_scale_down);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                img_profile_pic2.setAlpha(1.0f);
                img_profile_pic2.setBorderColor(getResources().getColor(R.color.colorPrimary));
                Animation dimmer = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_to_o);
                //txt_dimmer.startAnimation(dimmer);

                img_right.startAnimation(dimmer);
                img_left.startAnimation(dimmer);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_dimmer.setVisibility(View.GONE);
                img_cross.setVisibility(View.GONE);
                img_profile_pic2.setVisibility(View.GONE);

                img_right.setVisibility(View.GONE);
                img_left.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e(TAG, "Animation Repeat");
            }
        });

        img_profile_pic2.startAnimation(animation);
    }

    public HomeActivity activity() {
        return HomeActivity.instance;
    }

    /***
     * All Setter Methods are Here **************************************************************************************
     */

    /**
     * @param attendy   if do not Eventattendy object just create one , set userId URL and pass it.
     * @param myProfile if user comming to show his own profile then true otherwise false.
     * @return
     */
    public Demo_Profile_Fragment setData(RoomPerson attendy, boolean myProfile) {
        this.attendy = attendy;
        this.myProfile = myProfile;

        return this;
    }


    void setClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }





}
