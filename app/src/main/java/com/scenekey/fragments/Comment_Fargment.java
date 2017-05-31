package com.scenekey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.helper.Constants;
import com.scenekey.models.UserInfo;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by mindiii on 13/5/17.
 */

public class Comment_Fargment extends Fragment implements View.OnClickListener {
    static final String TAG = Comment_Fargment.class.toString();
    int maxNumber = 120;
    String EventId, kyeInStatus, eventDate, eventName;
    Event_Fragment event_fragment;
    Key_In_Event_Fragment key_in_event_fragment;
    int count = 0;
    private TextView txt_char;
    private EditText edt_comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f3_comment, null);
        if(event_fragment !=null) event_fragment.canCallWebservice = false;
        if(key_in_event_fragment !=null) key_in_event_fragment.canCallWebservice = false;
        txt_char = (TextView) view.findViewById(R.id.txt_char);
        edt_comment = (EditText) view.findViewById(R.id.edt_comment);
        ImageView img_profile = (ImageView) view.findViewById(R.id.img_profile);
        TextView txt_post_comment = (TextView) view.findViewById(R.id.txt_post_comment);
        TextView txt_f1_title = (TextView) view.findViewById(R.id.txt_f1_title);
        ImageView img_f3_back = (ImageView) view.findViewById(R.id.img_f3_back);
        TextView txt_char1 = (TextView) view.findViewById(R.id.txt_char1);
        txt_char.setText(maxNumber + " ");
        img_f3_back.setOnClickListener(this);
        txt_post_comment.setOnClickListener(this);
        Font font = new Font(activity());
        font.setFontFrankBookReg(txt_post_comment, txt_f1_title, edt_comment);
        font.setFontRailRegularLight(txt_char, txt_char1);
        Picasso.with(activity()).load(userInfo().getUserImage()).transform(new CircleTransform()).placeholder(R.drawable.image_defult_profile).into(img_profile);
        edt_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_char.setText((maxNumber - s.length()) + " ");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    /***********************
     * Esssential
     ********************/

    HomeActivity activity() {
        return HomeActivity.instance;
    }

    UserInfo userInfo() {
        return activity().getSessionManager().getUserInfo();
    }

    /******************************************************/

    void commentEvent() {
        VolleyGetPost volleyGetPost = new VolleyGetPost(activity(), activity(), WebService.EVENT_COMMENT, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG, " volleyResponse " + response);
                activity().dismissProgDailog();
                activity().onBackPressed();
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Log.e(TAG, " volleyResponse " + error);
                activity().dismissProgDailog();
                activity().onBackPressed();
                //TODO handle after implementing notification from server side
            }

            @Override
            public void onNetError() {

                activity().dismissProgDailog();
                activity().onBackPressed();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {

                params.put("user_id", userInfo().getUserID());
                params.put("event_id", EventId);
                params.put("location", "Fairfield,CA");
                params.put("comment", edt_comment.getText() + "");
                params.put("ratingtime", getCutrrentTimeinFormat());

                /*location:india
                comment:welcome
                Ratingtime:2017-04-12*/
                Log.e(TAG, "" + params.toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        volleyGetPost.setRetryTime(20000);
        volleyGetPost.execute();
    }

    @Override
    public void onDestroy() {
       if(event_fragment !=null) {
           event_fragment.canCallWebservice = true;
           event_fragment.getAlldata();
       }
       if(key_in_event_fragment !=null) {
           key_in_event_fragment.canCallWebservice = true;
           key_in_event_fragment.getAlldata();
       }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_post_comment:
                if (kyeInStatus.equals(Constants.KEY_NOTEXIST)) {
                    activity().showProgDilog(false);
                    addUserIntoEvent();
                } else {
                    activity().showProgDilog(false);
                    commentEvent();
                }
                break;
            case R.id.img_f3_back:
                activity().onBackPressed();
                break;
        }
    }

    /****
     * This method is used when The user is not exist in the event to first time key in th user
     */
    void addUserIntoEvent() {
        VolleyGetPost volleyaddUser = new VolleyGetPost(activity(), activity(), WebService.ADD_EVENT, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG, " : " + WebService.ADD_EVENT + response);

                commentEvent();


            }

            @Override
            public void onVolleyError(VolleyError error) {

                activity().dismissProgDailog();
                commentEvent();
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
        volleyaddUser.execute();
    }

    /***
     * @param kyeInStatus set the key in status this must be "exists" or "not exists"
     * @param eventId     event Id
     * @param eventDate   date of Event ;
     * @param eventName   event Name;
     */
    void setData(String kyeInStatus, String eventId, String eventDate, String eventName, Event_Fragment event_fragment) {
        this.kyeInStatus = kyeInStatus;
        this.eventDate = ((eventDate.replace(" ", "T")).replace("TO", "T").split("TO"))[0];
        this.eventName = eventName;
        this.EventId = eventId;
        this.event_fragment = event_fragment;
    }
    /***
     * @param kyeInStatus set the key in status this must be "exists" or "not exists"
     * @param eventId     event Id
     * @param eventDate   date of Event ;
     * @param eventName   event Name;
     */
    void setData(String kyeInStatus, String eventId, String eventDate, String eventName, Key_In_Event_Fragment key_in_event_fragment) {
        this.kyeInStatus = kyeInStatus;
        this.eventDate = ((eventDate.replace(" ", "T")).replace("TO", "T").split("TO"))[0];
        this.eventName = eventName;
        this.EventId = eventId;
        this.key_in_event_fragment = key_in_event_fragment;
    }

    String getCutrrentTimeinFormat() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(System.currentTimeMillis()));
    }


}
