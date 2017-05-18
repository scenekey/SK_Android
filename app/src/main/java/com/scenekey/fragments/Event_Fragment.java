package com.scenekey.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.CustomToastDialog;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.ImageUtil;
import com.scenekey.Utility.Permission;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.DataAdapter;
import com.scenekey.helper.Constants;
import com.scenekey.lib_sources.Floting_menuAction.FloatingActionButton;
import com.scenekey.lib_sources.Floting_menuAction.FloatingActionMenu;
import com.scenekey.lib_sources.SwipeCard.Card;
import com.scenekey.lib_sources.SwipeCard.CardsAdapter;
import com.scenekey.lib_sources.SwipeCard.SwipeCardView;
import com.scenekey.models.EventAttendy;
import com.scenekey.models.Event_Profile_Rating;
import com.scenekey.models.Feeds;
import com.scenekey.models.NotificationData;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mindiii on 2/5/17.
 */

public class Event_Fragment extends Fragment implements View.OnClickListener {
    public static final String TAG = Event_Fragment.class.toString();
    static String FEED_TYPE_PICTURE = "Picture";
    static String FEED_TYPE_COMMENT = "Comment";
    public boolean canCallWebservice;
    public boolean inLocation, inTime;
    String EventId;
    TextView txt_discipI_f2;
    double latitude;
    double longitude;
    LinearLayout info_view;
    RelativeLayout rtlv2_animate_f2;
    ImageView img_infoget_f2, img_f10_back;
    //private MapView mMapView;
    //private GoogleMap googleMap;
    Boolean isInfoVisible;
    RecyclerView rclv_grid;
    ScrollView scrl_all;
    FloatingActionButton fabMenu2_picture;
    Handler handler;
    View popupview;
    Dialog dialog;
    int noNotify;
    int timer;
    Timer timerHttp, timerNudge;
    NotificationData nudge;
    FloatingActionButton fabMenu1_like;
    ImageView img_p2_profile2, img_p2_profile, next;
    TextView txt_message, txt_timer;
    TextView txt_nudge, txt_reply, txt_view_pro;
    TextView txt_title;
    private ImageView image_map, img_reply_img;
    private FloatingActionMenu menu_blue;
    private TextView txt_hide_all_one;
    private TextView txt_hide_all_two;
    private TextView txt_calender_i1;
    private TextView txt_event_name;
    private TextView txt_discrp;
    private TextView txt_address_i1;
    private TextView txt_room;
    private RelativeLayout rtlv_top;
    private TextView txt_f2_badge;
    private ImageView img_notif;
    //Tinder Sswipe
    private SwipeCardView card_stack_view;
    private ArrayList<Card> cardslist;
    private Font font;
    private EventDetails eventDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f2_demo_event, null);

        try {
            activity().setBBvisiblity(View.GONE);
        } catch (Exception e) {

        }
        txt_discipI_f2 = (TextView) view.findViewById(R.id.txt_discipI_f2);
        txt_calender_i1 = (TextView) view.findViewById(R.id.txt_calender_i1);
        info_view = (LinearLayout) view.findViewById(R.id.info_view);
        img_infoget_f2 = (ImageView) view.findViewById(R.id.img_infoget_f2);
        rclv_grid = (RecyclerView) view.findViewById(R.id.rclv_grid);
        img_f10_back = (ImageView) view.findViewById(R.id.img_f10_back);
        txt_f2_badge = (TextView) view.findViewById(R.id.txt_f2_badge);
        txt_event_name = (TextView) view.findViewById(R.id.txt_event_name);
        txt_discrp = (TextView) view.findViewById(R.id.txt_discrp);
        txt_room = (TextView) view.findViewById(R.id.txt_room);
        txt_address_i1 = (TextView) view.findViewById(R.id.txt_address_i1);
        scrl_all = (ScrollView) view.findViewById(R.id.scrl_all);
        image_map = (ImageView) view.findViewById(R.id.image_map);
        rtlv_top = (RelativeLayout) view.findViewById(R.id.rtlv_top);
        menu_blue = (FloatingActionMenu) view.findViewById(R.id.menu_blue);

        img_notif = (ImageView) view.findViewById(R.id.img_notif);
        card_stack_view = (SwipeCardView) view.findViewById(R.id.card_stack_view);//TinderSwipe
        rtlv2_animate_f2 = (RelativeLayout) view.findViewById(R.id.rtlv2_animate_f2);
        txt_hide_all_one = (TextView) view.findViewById(R.id.txt_hide_all_one);
        txt_hide_all_two = (TextView) view.findViewById(R.id.txt_hide_all_two);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabMenu1_like = (FloatingActionButton) view.findViewById(R.id.fabMenu1_like);
        fabMenu2_picture = (FloatingActionButton) view.findViewById(R.id.fabMenu2_picture);
        RelativeLayout mainlayout = (RelativeLayout) view.findViewById(R.id.mainlayout);
        FloatingActionButton fabMenu3_comment = (FloatingActionButton) view.findViewById(R.id.fabMenu3_comment);
        fabMenu1_like.setTextView(new TextView[]{txt_hide_all_one, txt_hide_all_two});
        font = new Font(activity());
        rtlv_top.getLayoutParams().height = ((HomeActivity.ActivityWidth) * 3 / 4);
        getAlldata();
        if (timerHttp == null) setDataTimer();
        isInfoVisible = false;
        rclv_grid.hasFixedSize();
        txt_event_name.setText("");
        setOnClick(mainlayout, scrl_all, rtlv_top, img_infoget_f2, img_f10_back, fabMenu1_like, fabMenu2_picture, fabMenu3_comment, img_notif, txt_hide_all_one, txt_hide_all_two, txt_event_name);
        cardslist = new ArrayList<>();
        info_view.setVisibility(View.GONE);
        nudge = new NotificationData();
        font.setFontFranklinRegular(txt_hide_all_one);
        font.setFontFrankBookReg(txt_event_name, txt_calender_i1, txt_address_i1);
        font.setFontEuphemia(txt_discrp, txt_room);
        font.setFontRailRegular(txt_discipI_f2);
    }

    /******************************
     * Essential
     ************************/
    HomeActivity activity() {
        return HomeActivity.instance;
    }

    UserInfo userInfo() {
        return activity().getSessionManager().getUserInfo();
    }

    @Override
    public void onDestroy() {
        if (timerHttp != null) timerHttp.cancel();
        if (timerNudge != null) timerNudge.cancel();
        timerHttp = null;
        timerNudge = null;
        super.onDestroy();
    }



    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            txt_discipI_f2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_to_position);
        rtlv2_animate_f2.setAnimation(animation);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rtlv2_animate_f2.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }, 2000);
        activity().dismissProgDailog();
        activity().setBBvisiblity(View.GONE);
        canCallWebservice = true;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity().dismissProgDailog();
        activity().setBBvisiblity(View.GONE);
        canCallWebservice = true;
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        ((HomeActivity) getActivity()).setBBvisiblity(View.VISIBLE, 300);
        super.onDestroyView();
    }

    void setOnClick(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    void animateInfo(boolean currentVisible) {
        if (!currentVisible) {
            Animation trnslate_animate = AnimationUtils.loadAnimation(getActivity(), R.anim.translet_up_down);
            info_view.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) info_view.getLayoutParams();
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            info_view.setLayoutParams(layoutParams);
            scrl_all.smoothScrollTo(0, 0);
            info_view.setAnimation(trnslate_animate);
            rtlv2_animate_f2.setAnimation(trnslate_animate);
            rclv_grid.setAnimation(trnslate_animate);

            isInfoVisible = true;
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) info_view.getLayoutParams();
            layoutParams.height = 0;
            info_view.setLayoutParams(layoutParams);
            Animation trnslate_animate = AnimationUtils.loadAnimation(getActivity(), R.anim.translet_up_down);
            rtlv2_animate_f2.setAnimation(trnslate_animate);
            rclv_grid.setAnimation(trnslate_animate);
            isInfoVisible = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_infoget_f2:
                animateInfo(isInfoVisible);
                break;
            case R.id.img_f10_back:
                activity().onBackPressed();
                break;
            case R.id.fabMenu1_like:
                menu_blue.close(true);
                try {
                    if (activity().phpDistance(new Double[]{latitude, longitude, Double.valueOf(eventDetails.profile_rating.getVenue_lat()), Double.valueOf(eventDetails.profile_rating.getVenue_long())}) <= Constants.MAXIMUM_DISTANCE && checkWithTime(eventDetails.profile_rating.getEvent_date())) {
                        if (eventDetails.getProfile_rating().getKey_in().equals(Constants.KEY_NOTEXIST)) {
                            addUserIntoEvent(0, null);
                        } else likeEvent();
                    } else {
                        cantJoinDialogue();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity(), getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_LONG).show();

                }


                break;
            case R.id.fabMenu2_picture:
                menu_blue.close(true);
                try {
                    if (activity().phpDistance(new Double[]{latitude, longitude, Double.valueOf(eventDetails.profile_rating.getVenue_lat()), Double.valueOf(eventDetails.profile_rating.getVenue_long())}) <= Constants.MAXIMUM_DISTANCE && checkWithTime(eventDetails.profile_rating.getEvent_date())) {
                        captureImage();
                    } else {
                        cantJoinDialogue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity(), getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.fabMenu3_comment:
                menu_blue.close(true);
                try {
                    if (activity().phpDistance(new Double[]{latitude, longitude, Double.valueOf(eventDetails.profile_rating.getVenue_lat()), Double.valueOf(eventDetails.profile_rating.getVenue_long())}) <= Constants.MAXIMUM_DISTANCE && checkWithTime(eventDetails.profile_rating.getEvent_date())) {
                        Comment_Fargment comment_fargment = new Comment_Fargment();
                        activity().addFragment(comment_fargment, 1);
                        canCallWebservice = false;
                        comment_fargment.setData(eventDetails.getProfile_rating().getKey_in(), EventId, eventDetails.getProfile_rating().getEvent_date(), eventDetails.getProfile_rating().getEvent_name(), this);
                    } else {
                        cantJoinDialogue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity(), getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txt_hide_all_two:
                menu_blue.close(true);
                break;
            case R.id.txt_hide_all_one:
                menu_blue.close(true);
                break;
            case R.id.img_notif:
                if (noNotify > 0) getNudges();
                else noNotification();
                break;
            case R.id.txt_event_name:

                break;
            case R.id.rtlv_top:
                break;
            case R.id.menu_blue:
                break;
            default:
                break;
        }
    }

    /**
     * @param response the responce provided by getAlldata()
     * @throws JSONException
     */
    void getResponse(String response) throws JSONException {
        JSONObject obj1 = new JSONObject(response);
        if (eventDetails == null) eventDetails = new EventDetails();
        try {
            if (obj1.has("eventattendy"))
                eventDetails.setAttendyJson(obj1.getJSONArray("eventattendy"));
        } catch (JSONException e) {
        }
        try {
            if (obj1.has("nudges_count"))
                eventDetails.setNudges_count(obj1.getString("nudges_count"));
            noNotify = Integer.parseInt(eventDetails.getNudges_count());
            setTextbadge();
        } catch (JSONException e) {
        }
        try {
            if (obj1.has("allfeeds")) eventDetails.setFeedsJson(obj1.getJSONArray("allfeeds"));
        } catch (JSONException e) {
        }
        try {
            if (obj1.has("event_profile_rating"))
                eventDetails.setProfile_ratingJSon(obj1.getJSONArray("event_profile_rating"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int height = (int) activity().ActivityWidth;
        int width = (int) activity().ActivityHeight;
        String url = "http://maps.google.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=12&size=" + width + "x" + height + "&sensor=false";
        Log.e(TAG, "URL" + url + "Lat lin" + latitude + " : " + longitude);
        Picasso.with(activity()).load(url).into(image_map);
        inLocation = (activity().phpDistance(new Double[]{latitude, longitude, Double.valueOf(eventDetails.profile_rating.getVenue_lat()), Double.valueOf(eventDetails.profile_rating.getVenue_long())}) <= Constants.MAXIMUM_DISTANCE);
        try {
            inTime = checkWithTime(eventDetails.profile_rating.getEvent_date());
        } catch (Exception e) {

        }
        try {
            setDateTime(eventDetails.getProfile_rating().getEvent_date());
        } catch (ParseException e) {
            String[] dateSplit;
            dateSplit = eventDetails.getProfile_rating().getEvent_date().replace("TO", "T").split("T");
            txt_calender_i1.setText(dateSplit[0] + " " + dateSplit[1] + " " + dateSplit[2]);
            e.printStackTrace();
        }

    }

    /***
     * For setting the Grid Layout of room Persons showing at bottom of the Room
     *
     * @param list
     */
    void setRecyclerView(ArrayList<EventAttendy> list) {
        if (rclv_grid.getLayoutManager() == null)
            rclv_grid.setLayoutManager(new GridLayoutManager(activity(), 3));
        if (rclv_grid.getAdapter() == null) {
            DataAdapter dataAdapter = new DataAdapter(activity(), list, activity(), font, new String[]{getEventId(), userInfo().getUserID()}, this);
            rclv_grid.setAdapter(dataAdapter);
        } else {
            rclv_grid.getAdapter().notifyDataSetChanged();
        }
        Log.e(TAG, "List SIZE " + list.size());

    }

    void setCardAdapter(ArrayList<Card> cardslist) {
        Log.e("Size Card", cardslist.size() + "");
        if (card_stack_view.getAdapter() == null) {
            CardsAdapter arrayAdapter = new CardsAdapter(getContext(), cardslist);
            card_stack_view.setAdapter(arrayAdapter);
        }

        ((CardsAdapter) card_stack_view.getAdapter()).notifyDataSetChanged();
        card_stack_view.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override
            public void onCardExitLeft(Object dataObject) {
                //makeToast(CardSwipeActivity.this, "Left!");
            }

            @Override
            public void onCardExitRight(Object dataObject) {
                //makeToast(CardSwipeActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (itemsInAdapter == 0) {
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //start your activity here
                            card_stack_view.restart();
                        }

                    }, 20L);


                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }

            @Override
            public void onCardExitTop(Object dataObject) {
                // makeToast(CardSwipeActivity.this, "Top!");
            }

            @Override
            public void onCardExitBottom(Object dataObject) {
                //makeToast(CardSwipeActivity.this, "Bottom!");
            }
        });
    }



    void setDataTimer() {
        timerHttp = new Timer();
        //Set the schedule function and rate
        timerHttp.scheduleAtFixedRate(new TimerTask() {

                                          @Override
                                          public void run() {
                                              activity().runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {

                                                      if (canCallWebservice) getAlldata();


                                                  }
                                              });
                                          }

                                      },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                60000);
    }

    /******************************************************************************************/


    void captureImage() {
        Permission permission = new Permission(activity(), activity());
        if (permission.checkForCamera()) callIntent(Constants.INTENT_CAMERA);
    }

    public void callIntent(int caseid) {

        switch (caseid) {
            case Constants.INTENT_CAMERA:
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constants.INTENT_CAMERA);
                } catch (Exception e) {

                }
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {


            if (requestCode == Constants.INTENT_CAMERA && data != null) {
                final Bitmap eventImg = (Bitmap) data.getExtras().get("data");
                /*final Bitmap eventImg = BitmapFactory.decodeResource(getResources(),
                        R.drawable.scene2);*/
                if (eventDetails.profile_rating.getKey_in().equals(Constants.KEY_NOTEXIST))
                    addUserIntoEvent(1, eventImg);
                else sendPicture(eventImg);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_CAMERA) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (activity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //TODO : May be need to toast the msg that user denied permission
                } else captureImage();
            }
        }

    }


    /**
     * @param bitmap the bitmap return by the activity result
     */
    void sendPicture(final Bitmap bitmap) {
        VolleyGetPost sendPictureVolley = new VolleyGetPost(activity(), activity(), WebService.EVENT_POST_PIC, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG, "Response" + response);
                activity().dismissProgDailog();
                getAlldata();
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Log.e(TAG, "error" + error);
                activity().dismissProgDailog();
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("user_id", userInfo().getUserID());
                params.put("event_id", EventId);
                params.put("location", "Fairfield,CA");
                params.put("image", ImageUtil.encodeTobase64(bitmap));
                params.put("Ratingtime", "2017-04-13 15:28:16");
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        sendPictureVolley.execute();
    }


    /**
     * The dialogue use to show if user is not in the range of the event and evneet is not started yet
     */
    void cantJoinDialogue() {
        CustomToastDialog customToastDialog = new CustomToastDialog(activity());
        customToastDialog.setMessage(getResources().getString(R.string.sorryEvent));
        customToastDialog.show();
        // Toast.makeText(activity(), getResources().getString(R.string.sorryEvent), Toast.LENGTH_LONG).show();
    }

    public void cantInteract() {
        CustomToastDialog customToastDialog = new CustomToastDialog(activity());
        customToastDialog.setMessage(getResources().getString(R.string.sorryEvent));
        customToastDialog.show();
        //Toast.makeText(activity(), getResources().getString(R.string.noNotification), Toast.LENGTH_LONG).show();
    }

    public void noNotification() {
        CustomToastDialog customToastDialog = new CustomToastDialog(activity());
        customToastDialog.setMessage(getResources().getString(R.string.noNotification));
        customToastDialog.show();
        //Toast.makeText(activity(),getResources().getString(R.string.noNotification),Toast.LENGTH_SHORT).show();
    }


    /**
     * @param date date of the event check format before use tie
     * @return
     * @throws ParseException
     */
    public boolean checkWithTime(final String date) throws ParseException {
        String[] dateSplit = (date.replace("TO", "T")).replace(" ", "T").split("T");
        Date startTime = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(dateSplit[0] + " " + dateSplit[1]);
        Date endTime = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(dateSplit[0] + " " + dateSplit[2]);
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
        if (currentTime < endTime.getTime() && currentTime > startTime.getTime()) {
            return true;
        }
        return false;
    }


    /**
     * text badge count from 15 to 0 sec.
     */
    void setTextbadge() {

        txt_f2_badge.setText(noNotify + "");
        if (noNotify > 0) {
            txt_f2_badge.setBackground(getResources().getDrawable(R.drawable.bg_circle_red_badge));
            if (noNotify > 99) txt_f2_badge.setText("99+");
        } else {
            txt_f2_badge.setText("0");
            txt_f2_badge.setBackground(getResources().getDrawable(R.drawable.bg_circle_gray_badge));
        }
    }


    /***
     *nottification popup shown on clcik of bell icon
     */
    void popupNotification() {

        if (dialog == null) {
            dialog = new Dialog(activity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        if (popupview == null)
            popupview = LayoutInflater.from(activity()).inflate(R.layout.pop_my_room, null);
        if (img_p2_profile == null)
            img_p2_profile = (ImageView) popupview.findViewById(R.id.img_p2_profile);
        if (img_p2_profile2 == null)
            img_p2_profile2 = (ImageView) popupview.findViewById(R.id.img_p2_profile2);
        if (txt_message == null) {
            txt_message = (TextView) popupview.findViewById(R.id.txt_message);
            txt_message.setText(nudge.getNudges());

        } else {
            txt_message.setText("");
            txt_message.setText(nudge.getNudges());
            Animation animation = AnimationUtils.loadAnimation(activity(), R.anim.slide_left);
            txt_message.startAnimation(animation);

        }
        if (txt_timer == null) txt_timer = (TextView) popupview.findViewById(R.id.txt_timer);
        if (txt_nudge == null) txt_nudge = (TextView) popupview.findViewById(R.id.txt_nudge);
        if (txt_reply == null) txt_reply = (TextView) popupview.findViewById(R.id.txt_reply);
        if (txt_title == null) txt_title = (TextView) popupview.findViewById(R.id.txt_title);
        if (img_reply_img == null)
            img_reply_img = (ImageView) popupview.findViewById(R.id.img_reply_img);
        if (txt_message.getText().toString().equals(Constants.NUDGE_YOUR)) {
            txt_nudge.setText(getResources().getString(R.string.nudgeBack));
        } else txt_nudge.setText(getResources().getString(R.string.Nudge));
        txt_reply.setText(getResources().getString(R.string.Reply));
        if (txt_view_pro == null)
            txt_view_pro = (TextView) popupview.findViewById(R.id.txt_view_pro);
        if (next == null) next = (ImageView) popupview.findViewById(R.id.next);
        Picasso.with(activity()).load(nudge.getUserimage()).transform(new CircleTransform()).into(img_p2_profile);
        Picasso.with(activity()).load(nudge.getUserimage()).transform(new CircleTransform()).into(img_p2_profile2);


        dialog.setCanceledOnTouchOutside(true);


        if (!dialog.isShowing()) {
            font.setFontFranklinRegular(txt_message);
            dialog.setContentView(popupview);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.gravity = Gravity.CENTER;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.width = HomeActivity.ActivityWidth - ((int) activity().getResources().getDimension(R.dimen._30sdp));
            dialog.getWindow().setAttributes(lp);
            dialog.show();
        }

        noNotify--;
        setTextbadge();
        /***
         * Handling all the Click of pop up here
         */
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noNotify > 0) {
                    if (timerNudge != null) timerNudge.cancel();
                    timerNudge = null;
                    getNudges();
                } else dialog.dismiss();
            }
        });
        img_reply_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventAttendy attendy = new EventAttendy();
                attendy.setUserid(nudge.getUser_id());
                attendy.setUserFacebookId(nudge.getFacebook_id());
                attendy.setUserimage(nudge.getUserimage());
                attendy.setUsername(nudge.getUsername());
                Message_Fargment message_fargment = new Message_Fargment();
                activity().addFragment(message_fargment, 1);
                message_fargment.setData(EventId, userInfo().getUserID(), attendy, Event_Fragment.this);
            }
        });
        //popupview.setBackgroundColor(0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //Declare the timer
        popUptimer(txt_timer);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (timerNudge != null) timerNudge.cancel();
                timerNudge = null;
            }
        });
        font.setFontEuphemia(txt_nudge, txt_reply, txt_view_pro);
        font.setFontFrankBookReg(txt_title, txt_message, txt_timer);
    }

    void popUptimer(final TextView txt_timer) {
        timer = 15;
        timerNudge = new Timer();
        //Set the schedule function and rate
        timerNudge.scheduleAtFixedRate(new TimerTask() {

                                           @Override
                                           public void run() {
                                               activity().runOnUiThread(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       txt_timer.setText(timer + "");
                                                       if (timer == 0) dialog.dismiss();
                                                       timer--;

                                                   }
                                               });


                                               //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                           }

                                       },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                1000);
    }


    void setDateTime(String eventDate) throws ParseException {
        String[] dateSplit;
        dateSplit = eventDate.replace("TO", "T").split("T");

        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateSplit[0] + " " + dateSplit[1]);
        Date date2 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateSplit[0] + " " + dateSplit[2]));
        txt_calender_i1.setText(new SimpleDateFormat("MMMM dd,yyyy hh:mm aa").format(date1) + "-" + new SimpleDateFormat("hh:mm aa").format(date2));
    }

    /**
     * GetALl the data for that event
     */
    void getAlldata() {
        VolleyGetPost volleyGetPost = new VolleyGetPost(activity(), HomeActivity.instance, WebService.LISTEVENTFEED, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG, "response volley :" + response);
                try {
                    getResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Toast.makeText(activity(), getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetError() {

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {

                params.put("event_id", getEventId());
                params.put("user_id", userInfo().getUserID());
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

    /***
     * Event like volley
     */
    void likeEvent() {
        activity().showProgDilog(false);
        VolleyGetPost likeEventVolley = new VolleyGetPost(activity(), activity(), WebService.EVENT_LIKE, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG, " volleyResponse " + response);
                activity().dismissProgDailog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("success")) if (object.getInt("success") == 1) {
                        getAlldata();
                        //if("":1,"msg":"your have liked the event."})
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onVolleyError(VolleyError error) {

                activity().dismissProgDailog();
                Log.e(TAG, " Volley Error " + error);
                Toast.makeText(activity(), getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {

                params.put("user_id", userInfo().getUserID());
                params.put("event_id", EventId);
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        likeEventVolley.execute();
    }

    /***
     * For getting the nudge at notification popUp and show on it
     */
    void getNudges() {
        ;
        VolleyGetPost volleyGetPost2 = new VolleyGetPost(activity(), activity(), WebService.GET_NUDGE, false) {
            @Override
            public void onVolleyResponse(String response) {

                Log.e(TAG, "Nudge" + response);
                try {
                    JSONObject nudgeJson = new JSONObject(response);
                    if (nudgeJson.has("nudges")) nudge.setNudges(nudgeJson.getString("nudges"));
                    if (nudgeJson.has("user_id")) nudge.setUser_id(nudgeJson.getString("user_id"));
                    if (nudgeJson.has("facebook_id"))
                        nudge.setFacebook_id(nudgeJson.getString("facebook_id"));
                    if (nudgeJson.has("username"))
                        nudge.setUsername(nudgeJson.getString("username"));
                    if (nudgeJson.has("userimage"))
                        nudge.setUserimage(nudgeJson.getString("userimage"));
                    if (nudge.getNudges().equals(Constants.NUDGE_YOUR)) nudge.setMessage(false);
                    popupNotification();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Log.e(TAG, "Nudge" + error);
                Toast.makeText(activity(), getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onNetError() {

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("user_id", userInfo().getUserID());
                params.put("event_id", EventId);
                params.put("nudges_no", noNotify + "");
                Log.e(TAG, params.toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        volleyGetPost2.execute();

    }

    /****
     * This method is used when The user is not exist in the event to first time key in the user
     *
     * @param type must be 0 or 1
     */
    void addUserIntoEvent(final int type, @Nullable final Bitmap bitmap) {
        activity().showProgDilog(false);
        VolleyGetPost adduserVolley = new VolleyGetPost(activity(), activity(), WebService.ADD_EVENT, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG, " : " + WebService.ADD_EVENT + response);
                if (type == 0) likeEvent();
                else if (type == 1) sendPicture(bitmap);

            }

            @Override
            public void onVolleyError(VolleyError error) {
                activity().dismissProgDailog();
                Toast.makeText(activity(), getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("userid", userInfo().getUserID());
                params.put("eventname", userInfo().getUserID());
                params.put("eventid", EventId);
                params.put("Eventdate", userInfo().getUserID());
                Log.e(TAG, params.toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        adduserVolley.execute();
    }

    /**
     * Inner class to get Data as object
     */
    class EventDetails {
        ArrayList<Feeds> feedlist;
        ArrayList<EventAttendy> attendylist;
        Event_Profile_Rating profile_rating;
        String nudges_count;

        public void setAttendyJson(JSONArray Json) throws JSONException {
            if (attendylist != null) attendylist.clear();
            if (attendylist == null) attendylist = new ArrayList<>();
            for (int i = 0; i < Json.length(); i++) {
                EventAttendy attendy = new EventAttendy();
                JSONObject attendyJosn = Json.getJSONObject(i);
                if (attendyJosn.has("username"))
                    attendy.setUsername(attendyJosn.getString("username"));
                if (attendyJosn.has("userFacebookId"))
                    attendy.setUserFacebookId(attendyJosn.getString("userFacebookId"));
                if (attendyJosn.has("userid")) attendy.setUserid(attendyJosn.getString("userid"));
                if (attendyJosn.has("user_status"))
                    attendy.setUser_status(attendyJosn.getString("user_status"));
                if (attendyJosn.has("usertype"))
                    attendy.setUsertype(attendyJosn.getString("usertype"));
                if (attendyJosn.has("rating")) attendy.setRating(attendyJosn.getInt("rating") + "");
                if (attendyJosn.has("stagename"))
                    attendy.setStagename(attendyJosn.getString("stagename"));
                if (attendyJosn.has("userimage"))
                    attendy.setUserimage(attendyJosn.getString("userimage"));
                attendylist.add(attendy);
            }

            setRecyclerView(attendylist);

        }

        public void setFeedsJson(JSONArray Json) throws JSONException {
            cardslist.clear();
            if (feedlist == null) feedlist = new ArrayList<>();
            for (int i = 0; i < Json.length(); i++) {
                JSONObject feedsJson = Json.getJSONObject(i);
                Feeds feeds = new Feeds();
                if (feedsJson.has("username")) feeds.setUsername(feedsJson.getString("username"));
                if (feedsJson.has("userid")) feeds.setUserid(feedsJson.getString("userid"));
                if (feedsJson.has("userFacebookId"))
                    feeds.setUserFacebookId(feedsJson.getString("userFacebookId"));
                if (feedsJson.has("event_id")) feeds.setEvent_id(feedsJson.getString("event_id"));
                if (feedsJson.has("ratetype")) feeds.setRatetype(feedsJson.getString("ratetype"));
                if (feedsJson.has("userimage"))
                    feeds.setUserimage(feedsJson.getString("userimage"));
                if (feedsJson.has("type")) feeds.setType(feedsJson.getString("type"));
                if (feedsJson.has("location")) feeds.setLocation(feedsJson.getString("location"));
                if (feedsJson.has("date")) feeds.setDate(feedsJson.getString("date"));
                if (feedsJson.has("feed")) feeds.setFeed(feedsJson.getString("feed"));
                if (feeds.getType().equals(FEED_TYPE_PICTURE)) {
                    Card card = new Card();
                    card.imageUrl = feeds.getFeed();
                    cardslist.add(card);

                } else {
                    Card card = new Card();
                    card.imageUrl = null;
                    card.text = feeds.getFeed();
                    cardslist.add(card);
                }
                feedlist.add(feeds);
            }


            setCardAdapter(cardslist);


        }

        public void setProfile_ratingJSon(JSONArray jsonAr) throws JSONException {
            this.profile_rating = new Event_Profile_Rating();
            JSONObject JSon = jsonAr.getJSONObject(0);
            if (JSon.has("username"))
                profile_rating.setEvent_rating(JSon.getString("event_rating"));
            if (JSon.has("venue_detail")) {
                profile_rating.setVenue_detail(JSon.getString("venue_detail"));
                txt_address_i1.setText(profile_rating.getVenue_detail());
            }
            if (JSon.has("venue_id")) profile_rating.setVenue_id(JSon.getString("venue_id"));
            if (JSon.has("venue_lat")) {
                profile_rating.setVenue_lat(JSon.getString("venue_lat"));
                latitude = Double.parseDouble(profile_rating.getVenue_lat());
            }
            if (JSon.has("venue_long")) {
                profile_rating.setVenue_long(JSon.getString("venue_long"));
                longitude = Double.parseDouble(profile_rating.getVenue_long());
            }
            if (JSon.has("description")) {
                profile_rating.setDescription(JSon.getString("description"));
                txt_discipI_f2.setText(profile_rating.getDescription());
            }
            if (JSon.has("event_name")) {
                profile_rating.setEvent_name(JSon.getString("event_name"));
                if (profile_rating.getEvent_name().length() < 45)
                    txt_event_name.setText(profile_rating.getEvent_name());
                else
                    txt_event_name.setText(profile_rating.getEvent_name().substring(0, 45) + "...");
            }
            if (JSon.has("interval")) profile_rating.setInterval(JSon.getInt("interval") + "");
            if (JSon.has("event_date")) profile_rating.setEvent_date(JSon.getString("event_date"));
            if (JSon.has("key_in")) profile_rating.setKey_in(JSon.getString("key_in"));
            if (JSon.has("like")) {
                profile_rating.setLike(JSon.getInt("like") + "");
                if (profile_rating.getLike().equals("1")) {
                    fabMenu1_like.setImageDrawable(getResources().getDrawable(R.drawable.red_heart));
                }
            }

        }

        public ArrayList<Feeds> getFeedlist() {
            return feedlist;
        }

        public ArrayList<EventAttendy> getAttendylist() {
            return attendylist;
        }

        public Event_Profile_Rating getProfile_rating() {
            return profile_rating;
        }

        public String getNudges_count() {
            return nudges_count;
        }

        public void setNudges_count(String nudges_count) {
            this.nudges_count = nudges_count;
        }
    }
}
