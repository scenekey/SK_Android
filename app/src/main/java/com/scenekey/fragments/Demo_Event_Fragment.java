package com.scenekey.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.Util;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.DataAdapter;
import com.scenekey.adapter.DataAdapter_Demo;
import com.scenekey.helper.SessionManager;
import com.scenekey.lib_sources.Floting_menuAction.FloatingActionButton;
import com.scenekey.lib_sources.Floting_menuAction.FloatingActionMenu;
import com.scenekey.lib_sources.SwipeCard.Card;
import com.scenekey.lib_sources.SwipeCard.CardsAdapter;
import com.scenekey.lib_sources.SwipeCard.SwipeCardView;
import com.scenekey.models.NotificationData;
import com.scenekey.models.RoomPerson;
import com.scenekey.models.UserInfo;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mindiii on 12/4/17.
 */

public class Demo_Event_Fragment extends Fragment implements View.OnClickListener {

    TextView txt_discipI_f2;
    double latitude;
    double longitude;
    LinearLayout info_view;
    RelativeLayout rtlv2_animate_f2;
    ImageView img_infoget_f2, img_f10_back;
    Boolean isInfoVisible;
    RecyclerView rclv_grid;
    HomeActivity activity;
    ScrollView scrl_all;
    //private MapView imagemap;
    //private GoogleMap googleMap;
    Handler handler;
    SessionManager sessionManager;
    UserInfo userInfo;
    View popupview;
    Dialog dialog;
    int noNotify;
    int timer;
    Timer t;
    boolean initialized = false;
    Home_no_Event home_no_event;
    private ImageView imagemap;
    private FloatingActionButton fabMenu1;
    private FloatingActionButton fabMenu2;
    private FloatingActionButton fabMenu3;
    private FloatingActionMenu menu_blue;
    private TextView txt_discrp;
    private TextView txt_address_i1;
    private TextView txt_room;
    private TextView txt_f2_badge;
    //Tinder Sswipe
    private SwipeCardView card_stack_view;
    private ArrayList<Card> al;
    private ArrayList<NotificationData> nlist;
    private CardsAdapter arrayAdapter;
    private Font font;

    public void setImageArrray(Home_no_Event home_no_event) {
        this.home_no_event = home_no_event;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f2_demo_event, null);

        activity = (HomeActivity) getActivity();
        txt_discipI_f2 = (TextView) view.findViewById(R.id.txt_discipI_f2);

        info_view = (LinearLayout) view.findViewById(R.id.info_view);
        img_infoget_f2 = (ImageView) view.findViewById(R.id.img_infoget_f2);
        rclv_grid = (RecyclerView) view.findViewById(R.id.rclv_grid);
        img_f10_back = (ImageView) view.findViewById(R.id.img_f10_back);
        txt_f2_badge = (TextView) view.findViewById(R.id.txt_f2_badge);

        txt_discrp = (TextView) view.findViewById(R.id.txt_discrp);
        txt_room = (TextView) view.findViewById(R.id.txt_room);
        txt_address_i1 = (TextView) view.findViewById(R.id.txt_address_i1);
        scrl_all = (ScrollView) view.findViewById(R.id.scrl_all);
        imagemap = (ImageView) view.findViewById(R.id.image_map);
        RelativeLayout rtlv_top = (RelativeLayout) view.findViewById(R.id.rtlv_top);
        menu_blue = (FloatingActionMenu) view.findViewById(R.id.menu_blue);
        fabMenu1 = (FloatingActionButton) view.findViewById(R.id.fabMenu1_like);
        fabMenu2 = (FloatingActionButton) view.findViewById(R.id.fabMenu2_picture);
        fabMenu3 = (FloatingActionButton) view.findViewById(R.id.fabMenu3_comment);

        card_stack_view = (SwipeCardView) view.findViewById(R.id.card_stack_view);//TinderSwipe
        rtlv2_animate_f2 = (RelativeLayout) view.findViewById(R.id.rtlv2_animate_f2);
        LinearLayout no_one = (LinearLayout) view.findViewById(R.id.no_one);
        no_one.setVisibility(View.GONE);


        latitude = 34.0430175;
        longitude = -118.2694481;
        info_view.setVisibility(View.GONE);
        rtlv_top.getLayoutParams().height = ((HomeActivity.ActivityWidth) * 3 / 4);
        Util.setStatusBarColor(HomeActivity.instance, R.color.colorPrimary);
        (activity).setBBvisiblity(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txt_hide_all_one = (TextView) view.findViewById(R.id.txt_hide_all_one);
        TextView txt_hide_all_two = (TextView) view.findViewById(R.id.txt_hide_all_two);
        TextView txt_calender_i1 = (TextView) view.findViewById(R.id.txt_calender_i1);
        TextView txt_event_name = (TextView) view.findViewById(R.id.txt_event_name);
        ImageView img_notif = (ImageView) view.findViewById(R.id.img_notif);
        fabMenu1.setTextView(new TextView[]{txt_hide_all_one, txt_hide_all_two});

        setOnClick(img_infoget_f2, img_f10_back, fabMenu1, fabMenu2, fabMenu3, img_notif, txt_hide_all_one, txt_hide_all_two);
        isInfoVisible = false;
        rclv_grid.hasFixedSize();
        SessionManager sessionManager = new SessionManager(activity);
        userInfo = sessionManager.getUserInfo();
        activity.dismissProgDailog();


        //TinderSwipe
        al = new ArrayList<>();
        getDummyData(al);
        arrayAdapter = new CardsAdapter(getContext(), al);
        card_stack_view.setAdapter(arrayAdapter);
        noNotify = 5;

        setTextbadge();
        font = new Font(getContext());
        font.setFontFranklinRegular(txt_hide_all_one);
        font.setFontFrankBookReg(txt_event_name, txt_calender_i1, txt_address_i1);
        font.setFontEuphemia(txt_discrp, txt_room);
        font.setFontRailRegular(txt_discipI_f2);

        String date = new SimpleDateFormat("MMMM dd, yyyy").format(Calendar.getInstance().getTime());
        txt_calender_i1.setText(date + " 8:00 AM - 12:00 PM");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_infoget_f2:
                animateInfo(isInfoVisible);
                break;
            case R.id.img_f10_back:
                activity.onBackPressed();
                break;
            case R.id.fabMenu1_like:
                menu_blue.close(true);
                break;
            case R.id.fabMenu2_picture:
                menu_blue.close(true);
                break;
            case R.id.fabMenu3_comment:
                menu_blue.close(true);
                break;
            case R.id.txt_hide_all_two:
                menu_blue.close(true);
                break;
            case R.id.txt_hide_all_one:
                menu_blue.close(true);
                break;
            case R.id.img_notif:
                if (noNotify > 0) popupNotification();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        activity.setBBvisiblity(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            txt_discipI_f2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        txt_discipI_f2.setText("Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place " +
                "of English to emphasise design elements over content. It's also called placeholder (or filler) text. " +
                "It's a convenient tool for mock-ups.");


        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_to_position);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!initialized) {
                    InitializeGrid();
                    int height = (int) activity.getResources().getDimension(R.dimen._100sdp);
                    int width = (int) activity.getResources().getDimension(R.dimen._300sdp);
                    String url = "http://maps.google.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=12&size=" + width + "x" + height + "&sensor=false";
                    Picasso.with(activity).load(url).into(imagemap);
                    initialized = true;

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rtlv2_animate_f2.setAnimation(animation);
        handler = new Handler();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rtlv2_animate_f2.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }, 2000);


    }

    @Override
    public void onResume() {
        super.onResume();

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

    void setOnClick(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }


    void InitializeGrid() {
        rclv_grid.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rclv_grid.setLayoutManager(layoutManager);

        final String android_version_names[] = {
                "Alexander",
                "Alizee",
                "Amy",
                "Dahn-mein",
                "Darrin",
                "Erin",
                "James",
                "Morgan",
                userInfo.getUserName(),

        };

        /*final String android_image_urls[] = {
                "" + R.drawable.room_1,
                "" + R.drawable.room_2,
                "" + R.drawable.room_3,
                "" + R.drawable.room_4,
                "" + R.drawable.room_5,
                "" + R.drawable.room_6,
                "" + R.drawable.room_7,
                "" + R.drawable.room_8,
                userInfo.getUserImage()
        };*/
        final String android_image_urls[] = {
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                userInfo.getUserImage()
        };

        final String staus[] = {
                "avilable",
                "busy",
                "na",
                "busy",
                "na",
                "avilable",
                "avilable",
                "busy",
                "na",

        };


        final ArrayList<RoomPerson> roomPersons = new ArrayList<>();
        final DataAdapter_Demo adapter = new DataAdapter_Demo(getContext(), roomPersons, HomeActivity.instance, font, home_no_event);
        rclv_grid.setAdapter(adapter);
        for (int i = 0; i < 9; i++) {
            roomPersons.add(new RoomPerson(android_version_names[i], android_image_urls[i], staus[i]));
            adapter.notifyItemInserted(i);
        }
       /* adapter.notifyDataSetChanged();*/

                /*Animation animation = AnimationUtils.loadAnimation(activity,R.anim.enter_from_right);
                rclv_grid.startAnimation(animation);*/
        //Notifaication Data
        if (nlist == null) nlist = new ArrayList<NotificationData>();
        nlist.add(new NotificationData(R.drawable.room_3, getResources().getString(R.string.notification5)));
        nlist.add(new NotificationData(R.drawable.room_1, getResources().getString(R.string.notification4)));
        nlist.add(new NotificationData(R.drawable.room_2, getResources().getString(R.string.notification3)));
        nlist.add(new NotificationData(R.drawable.room_4, getResources().getString(R.string.notification2)));
        nlist.add(new NotificationData(R.drawable.room_5, getResources().getString(R.string.notification1)));
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

        getDemoData2();


    }

    ArrayList<RoomPerson> prepareData(String[] android_version_names, String[] android_image_urls, String[] status) {

        ArrayList<RoomPerson> android_version = new ArrayList<>();
        for (int i = 0; i < android_version_names.length; i++) {
            RoomPerson roomPerson = new RoomPerson();
            roomPerson.setAndroid_version_name(android_version_names[i]);
            roomPerson.setAndroid_image_url(android_image_urls[i]);
            roomPerson.setStaus(status[i]);
            android_version.add(roomPerson);
        }
        return android_version;
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        ((HomeActivity) getActivity()).setBBvisiblity(View.VISIBLE, 300);
        initialized = false;
        super.onDestroyView();
    }


    //TinderSwipe
    private void getDummyData(ArrayList<Card> al) {
        Card card = new Card();
        card.name = "Card1";
        card.imageId = R.drawable.demo_1;
        card.imageint = R.drawable.room_1;
        al.add(card);

        Card card2 = new Card();
        card2.name = "Card2";
        card2.text  = getResources().getString(R.string.omg__smooth);
        card2.imageint = R.drawable.room_2;
        al.add(card2);

        /*al.add(card);*/


    }

    void getDemoData2() {
        Card card3 = new Card();
        card3.name = "Card3";
        card3.imageId = R.drawable.demo_2;
        card3.imageint = R.drawable.room_3;
        al.add(card3);

        Card card4 = new Card();
        card4.name = "Card4";
        card4.text = getResources().getString(R.string.ihavebest);
        card4.imageint = R.drawable.room_4;
        al.add(card4);

        Card card5 = new Card();
        card5.name = "Card5";
        card5.imageId = R.drawable.demo_3;
        card5.imageint = R.drawable.room_5;
        al.add(card5);

        Card card6 = new Card();
        card6.name = "Card6";
        card6.imageId = R.drawable.demo_4;
        card6.imageint = R.drawable.room_6;
        al.add(card6);

        Card card7 = new Card();
        card7.name = "Card7";
        card7.text = getResources().getString(R.string.omg__smooth);
        card7.imageint = R.drawable.room_7;
        al.add(card7);

        Card card8 = new Card();
        card8.name = "Card8";
        card8.imageId =R.drawable.demo_5;
        card8.imageint = R.drawable.room_8;
        al.add(card8);

        Card card9 = new Card();
        card9.name = "card9";
        card9.text = getResources().getString(R.string.ihavebest);
        card9.imageint = R.drawable.room_1;
        al.add(card9);

        Card card10 = new Card();
        card10.name = "card10";
        card10.imageId =R.drawable.demo_6;
        card10.imageint = R.drawable.room_2;
        al.add(card10);

        Card card11 = new Card();
        card11.name = "Card8";
        card11.imageId =R.drawable.demo_7;
        card11.imageint = R.drawable.room_3;
        al.add(card11);

        Card card12 = new Card();
        card12.name = "card9";
        card12.text = getResources().getString(R.string.ilovemike);
        card12.imageint = R.drawable.room_5;
        al.add(card12);



        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        rtlv2_animate_f2.setBackgroundColor(getResources().getColor(R.color.transparent));
    }
    //Notifaication PopUp

    void popupNotification() {
        final ImageView img_p2_profile2, img_p2_profile, next;
        final TextView txt_message, txt_timer;
        final TextView txt_nudge, txt_reply, txt_view_pro;
        final TextView txt_title;
        RelativeLayout nudge;
        NotificationData person = nlist.get(noNotify - 1);
        dialog = new Dialog(activity);
        popupview = LayoutInflater.from(activity).inflate(R.layout.pop_my_room, null);
        img_p2_profile = (ImageView) popupview.findViewById(R.id.img_p2_profile);
        img_p2_profile2 = (ImageView) popupview.findViewById(R.id.img_p2_profile2);
        txt_message = (TextView) popupview.findViewById(R.id.txt_message);
        txt_timer = (TextView) popupview.findViewById(R.id.txt_timer);
        txt_nudge = (TextView) popupview.findViewById(R.id.txt_nudge);
        txt_reply = (TextView) popupview.findViewById(R.id.txt_reply);
        txt_title = (TextView) popupview.findViewById(R.id.txt_title);
        txt_view_pro = (TextView) popupview.findViewById(R.id.txt_view_pro);
        next = (ImageView) popupview.findViewById(R.id.next);
        Picasso.with(activity).load(person.getImg()).transform(new CircleTransform()).into(img_p2_profile);
        Picasso.with(activity).load(person.getImg()).transform(new CircleTransform()).into(img_p2_profile2);
        txt_message.setText(person.getNudges());
        nudge = (RelativeLayout) popupview.findViewById(R.id.nudge);
        nudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DataAdapter_Demo) rclv_grid.getAdapter()).goodNudge();
            }
        });
        //font.setFontFranklinRegular(txt_message);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(popupview);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = HomeActivity.ActivityWidth - ((int) activity.getResources().getDimension(R.dimen._30sdp));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        noNotify--;
        setTextbadge();
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.beat_animation);
        txt_timer.startAnimation(animation);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noNotify > 0) {
                    txt_message.setText("");
                    NotificationData person = nlist.get(noNotify - 1);
                    Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_left);
                    txt_message.setAnimation(animation);
                    Picasso.with(activity).load(person.getImg()).transform(new CircleTransform()).into(img_p2_profile);
                    Picasso.with(activity).load(person.getImg()).transform(new CircleTransform()).into(img_p2_profile2);
                    txt_message.setText(person.getNudges());
                    noNotify--;
                    setTextbadge();
                    t.cancel();
                    t = null;
                    popUptimer(txt_timer);
                } else dialog.dismiss();
            }
        });
        //popupview.setBackgroundColor(0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //Declare the timer
        popUptimer(txt_timer);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                t.cancel();
                t = null;
            }
        });
        font.setFontEuphemia(txt_nudge, txt_reply, txt_view_pro);
        font.setFontFrankBookReg(txt_title, txt_message, txt_timer);
    }


    void setTextbadge() {

        txt_f2_badge.setText(noNotify + "");
        if (noNotify > 0)
            txt_f2_badge.setBackground(getResources().getDrawable(R.drawable.bg_circle_red_badge));
        else
            txt_f2_badge.setBackground(getResources().getDrawable(R.drawable.bg_circle_gray_badge));
    }

    void popUptimer(final TextView txt_timer) {
        timer = 15;
        t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      activity.runOnUiThread(new Runnable() {
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
}
