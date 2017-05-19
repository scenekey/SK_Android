package com.scenekey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.Util;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.Profile_Events_Adapter;
import com.scenekey.models.EventAttendy;
import com.scenekey.models.Feeds;
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

public class Profile_Fragment extends Fragment implements View.OnClickListener {
    public static final String TAG = Profile_Fragment.class.toString();
    CircleImageView img_profile_pic2;
    EventAttendy attendy;
    boolean myProfile;
    LinearLayout mainlayout;
    Event_Fragment event_fragment;
    RecyclerView rclv_f3_trending;
    private ImageView img_profile_pic, img_cross, img_left, img_right, img_fb, img_capture;
    private TextView txt_event_count, txt_dimmer, txt_f2_badge, txt_profile_name;
    private ArrayList<Feeds> feedslist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f2_profile_own, null);
        img_profile_pic = (ImageView) v.findViewById(R.id.img_profile_pic);
        img_left = (ImageView) v.findViewById(R.id.img_left);
        img_right = (ImageView) v.findViewById(R.id.img_right);
        img_cross = (ImageView) v.findViewById(R.id.img_cross);
        img_fb = (ImageView) v.findViewById(R.id.img_fb);
        img_capture = (ImageView) v.findViewById(R.id.img_capture);
        img_profile_pic2 = (CircleImageView) v.findViewById(R.id.img_profile_pic2);
        txt_event_count = (TextView) v.findViewById(R.id.txt_event_count);
        txt_dimmer = (TextView) v.findViewById(R.id.txt_dimmer);
        txt_f2_badge = (TextView) v.findViewById(R.id.txt_f2_badge);
        mainlayout = (LinearLayout) v.findViewById(R.id.mainlayout);
        txt_profile_name = (TextView) v.findViewById(R.id.txt_profile_name);
        rclv_f3_trending = (RecyclerView) v.findViewById(R.id.rclv_f3_trending);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txt_EE;
        txt_EE = (TextView) view.findViewById(R.id.txt_EE);
        img_profile_pic.setOnClickListener(this);
        Picasso.with(activity()).load(attendy.getUserimage()).transform(new CircleTransform()).into(img_profile_pic);
        Picasso.with(activity()).load(attendy.getUserimage()).transform(new CircleTransform()).into(img_profile_pic2);
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
        //TODO : Seeting the fonts
        //TODO :Setting the Event count to 99+ if greter then 99
        setClick(view);
        getProfile();
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
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        event_fragment.canCallWebservice = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        event_fragment.canCallWebservice = true;
    }

    void profileImgClick() {
        Log.e(TAG, "Clicked");
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

    HomeActivity activity() {
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
    public Profile_Fragment setData(EventAttendy attendy, boolean myProfile, @Nullable Event_Fragment fragment) {
        this.attendy = attendy;
        this.myProfile = myProfile;
        this.event_fragment = fragment;
        return this;
    }

    void setClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    boolean setRecyclerView() {
        if (rclv_f3_trending.getAdapter() == null) {
            Profile_Events_Adapter adapter = new Profile_Events_Adapter(activity(), feedslist);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity(), 1);
            rclv_f3_trending.setLayoutManager(layoutManager);
            rclv_f3_trending.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            rclv_f3_trending.setHasFixedSize(true);
            return true;
        } else {
            rclv_f3_trending.getAdapter().notifyDataSetChanged();
            rclv_f3_trending.setHasFixedSize(true);

            return true;
        }


    }


    /**
     * API
     */


    /**
     * will provide list of user attended event;
     */
    void getProfile() {
        VolleyGetPost getProfile = new VolleyGetPost(activity(), activity(), WebService.LISTATTENDEDEVENT, false) {
            @Override
            public void onVolleyResponse(String response) {
                Util.printBigLogcat(TAG, response);
                try {
                    getResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Log.e(TAG, error + "");
            }

            @Override
            public void onNetError() {

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                /*params.put("user_id",attendy.getUserid());
                params.put("type","app");*/
                //TODO change with live
                params.put("user_id", 163 + "");
                params.put("type", "app");
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        getProfile.execute();
    }

    /**
     * @param response the response given by listofuserattenedevent
     * @throws JSONException
     */
    void getResponse(String response) throws JSONException {
        if (feedslist == null) feedslist = new ArrayList<>();
        JSONObject object = new JSONObject(response);
        if (object.has("allfeeds")) {
            JSONArray array = object.getJSONArray("allfeeds");
            for (int i = 0; i < array.length(); i++) {
                Feeds feeds = new Feeds();
                JSONObject feedJson = array.getJSONObject(i);
                if (feedJson.has("username")) feeds.setUsername(feedJson.getString("username"));
                if (feedJson.has("userid")) feeds.setUserid(feedJson.getString("userid"));
                if (feedJson.has("userFacebookId"))
                    feeds.setUserFacebookId(feedJson.getString("userFacebookId"));
                if (feedJson.has("event_id")) feeds.setEvent_id(feedJson.getString("event_id"));
                if (feedJson.has("ratetype")) feeds.setRatetype(feedJson.getString("ratetype"));
                if (feedJson.has("event_name"))
                    feeds.setEvent_name(feedJson.getString("event_name"));
                if (feedJson.has("userimage")) feeds.setUserimage(feedJson.getString("userimage"));
                if (feedJson.has("type")) feeds.setType(feedJson.getString("type"));
                if (feedJson.has("location")) feeds.setLocation(feedJson.getString("location"));
                if (feedJson.has("date")) feeds.setDate(feedJson.getString("date"));
                if (feedJson.has("feed")) feeds.setFeed(feedJson.getString("feed"));
                feedslist.add(feeds);
            }
        }
        if (object.has("keyin_count")) {
            txt_event_count.setText(object.getInt("keyin_count") + " event");
        }
        setRecyclerView();

    }

}
