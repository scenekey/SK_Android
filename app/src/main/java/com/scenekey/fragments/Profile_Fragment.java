package com.scenekey.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
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
import com.scenekey.models.UserInfo;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
    Key_In_Event_Fragment key_in_event_fragment;
    Demo_Event_Fragment demo_event_fragment;
    ListView rclv_f3_trending;
    private ImageView img_profile_pic, img_cross, img_left, img_right, img_fb, img_capture;
    private TextView txt_event_count, txt_dimmer, txt_f2_badge, txt_profile_name;
    private ArrayList<Feeds> feedslist;
    int mutulFriendCount;
    //GridLayoutManager layoutManager;
   // Profile_Events_Adapter adapter;
    int pageToshow=1;

    ProfileListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(activity());
        View v = inflater.inflate(R.layout.f2_profile_own, null);
        mainlayout = (LinearLayout) v.findViewById(R.id.mainlayout);
        rclv_f3_trending = (ListView) v.findViewById(R.id.rclv_f3_trending);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //TODO : Seeting the fonts
        //TODO :Setting the Event count to 99+ if greter then 99

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity().showProgDilog(false);
                getProfile();
            }
        },200);
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
        txt_profile_name.setText(attendy.getUsername());
        setClick(view, img_back ,img_setting);
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

        txt_f2_badge.setText(mutulFriendCount+"");
        /*adapter = new Profile_Events_Adapter(activity(), feedslist);
        layoutManager = new GridLayoutManager(activity(), 1);*/
        /*rclv_f3_trending.setLayoutManager(layoutManager);
        rclv_f3_trending.setAdapter(adapter);*/

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
                if(myProfile)activity().addFragment(new Setting_Fragment(),1);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(event_fragment != null)event_fragment.canCallWebservice = false;
        if(key_in_event_fragment != null)key_in_event_fragment.canCallWebservice = false;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(event_fragment != null)event_fragment.canCallWebservice = true;
        if(key_in_event_fragment != null)key_in_event_fragment.canCallWebservice = true;
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
    public Profile_Fragment setData(EventAttendy attendy, boolean myProfile,  Event_Fragment fragment,int mutulFriendCount) {
        this.attendy = attendy;
        this.myProfile = myProfile;
        this.event_fragment = fragment;
        this.mutulFriendCount = mutulFriendCount;
        return this;
    }
    /**
     * @param attendy   if do not Eventattendy object just create one , set userId URL and pass it.
     * @param myProfile if user comming to show his own profile then true otherwise false.
     * @return
     */
    public Profile_Fragment setData(EventAttendy attendy, boolean myProfile,  Key_In_Event_Fragment fragment) {
        this.attendy = attendy;
        this.myProfile = myProfile;
        this.key_in_event_fragment = fragment;
        return this;
    }

    /**
     * @param attendy   if do not Eventattendy object just create one , set userId URL and pass it.
     * @param myProfile if user comming to show his own profile then true otherwise false.
     * @return
     */
    public Profile_Fragment setData(EventAttendy attendy, boolean myProfile, @Nullable Demo_Event_Fragment fragment) {
        this.attendy = attendy;
        this.myProfile = myProfile;
        this.demo_event_fragment = fragment;
        return this;
    }

    void setClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    boolean setRecyclerView() {


        /*if (adapter == null) {
            adapter = new Profile_Events_Adapter(activity(), feedslist);
            layoutManager = new GridLayoutManager(activity(), 1);
            rclv_f3_trending.setLayoutManager(layoutManager);
            rclv_f3_trending.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
            return true;
        } else {
            adapter.notifyDataSetChanged();
            return true;
        }*/

        return false;

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
                }catch (InterruptedException e){

                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Log.e(TAG, error + "");
                activity().dismissProgDailog();
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("user_id",attendy.getUserid());
                params.put("type","app");
                //TODO change with live
               /* params.put("user_id", activity().);
                params.put("type", "app");*/
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
    synchronized void getResponse(String response) throws JSONException, InterruptedException {
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
                if(i==1)activity().dismissProgDailog();

            }
            adapter.notifyDataSetChanged();
            rclv_f3_trending.setAdapter(adapter);

        }
        if(feedslist.size()==0){
            Toast.makeText(activity()," No event found !",Toast.LENGTH_SHORT).show();
        }
        if (object.has("keyin_count")) {
            txt_event_count.setText(object.getInt("keyin_count") + " event");
        }
        //setRecyclerView();
        //rclv_f3_trending.setHasFixedSize(true);
    }

    void mutualFriend(){

        /*Bundle params = new Bundle();
        params.putString("fields", "context.fields(mutual_friends)");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + attendy.getUserFacebookId(),
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        try {
                            JSONObject jsonObject = new JSONObject(graphResponse.getRawResponse());
                            if (jsonObject.has("context")) {
                                jsonObject = jsonObject.getJSONObject("context");
                                if (jsonObject.has("mutual_friends")) {
                                    JSONArray mutualFriendsJSONArray = jsonObject.getJSONObject("mutual_friends").getJSONArray("data");
                                    // this mutualFriendsJSONArray contains the id and name of the mutual friends.
                                    Log.e(TAG," Mutul Friend"+mutualFriendsJSONArray.length()+ mutualFriendsJSONArray.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();*/

        Bundle params = new Bundle();
        params.putString("fields", "context.fields(mutual_friends)");
        // make the API call
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+attendy.getUserFacebookId(),
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            // handle the result
                        Log.e(TAG," : "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response.getRawResponse());
                            if (jsonObject.has("context")) {
                                jsonObject = jsonObject.getJSONObject("context");
                                if (jsonObject.has("mutual_friends")) {
                                    JSONArray mutualFriendsJSONArray = jsonObject.getJSONObject("mutual_friends").getJSONArray("data");

                                    Log.e(TAG," Mutul Friend"+mutualFriendsJSONArray.length()+ mutualFriendsJSONArray.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
/*https://graph.facebook.com/100000025257108?access_token=EAAU6Ok4XbPYBAKtk5p0WKtK22jfAiV2kUzFbG6qojkCw7TM9iWK9L5qocDGDdpSeQjfUFmwzgoBMPpjl1NC99tpNpRfTR6i48utfrV1O3ZBQWljK5sYIaMa3JUralW2NPPcbBEIsb4INJZCmStvz23eWOqJJ0V6oCSZA7JGZBT5jD1ZB56JWnwAV6INEUK4RxsNnFTURWrxmg6WXXMnmy%20&&fields=context.fields%28mutual_friends%29*/

        try {
            Log.e(TAG,activity().userInfo().getUserAccessToken()+" : ");
        }catch (Exception e){
            e.printStackTrace();
        }
        /*VolleyGetPost volleyGetPost = new VolleyGetPost(activity(),activity(),"https://graph.facebook.com/v2.9/100000025257108",true) {
            @Override
            public void onVolleyResponse(String response) {

                Log.e(TAG,response+"");
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Log.e(TAG,error+" Error");
            }

            @Override
            public void onNetError() {

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                params.put("access_token",activity().userInfo().getUserAccessToken());
                params.put("fields","context.fields(mutual_friends)");
                return params;
            }
        };
        volleyGetPost.execute();*/
    }




}
