package com.scenekey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
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
import com.scenekey.models.EventAttendy;
import com.scenekey.models.UserInfo;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by mindiii on 13/5/17.
 */

public class Message_Fargment extends Fragment implements View.OnClickListener {
    static final String TAG = Message_Fargment.class.toString();
    int maxNumber = 30;
    String EventId, nudgeBy;
    EventAttendy attendy;
    Event_Fragment event_fragment;
    int count = 0;
    private TextView txt_char;
    private EditText edt_comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f3_comment, null);
        txt_char = (TextView) view.findViewById(R.id.txt_char);
        edt_comment = (EditText) view.findViewById(R.id.edt_comment);
        edt_comment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNumber)});
        ImageView img_profile = (ImageView) view.findViewById(R.id.img_profile);
        TextView txt_post_comment = (TextView) view.findViewById(R.id.txt_post_comment);
        TextView txt_f1_title = (TextView) view.findViewById(R.id.txt_f1_title);
        TextView txt_char1 = (TextView) view.findViewById(R.id.txt_char1);
        txt_f1_title.setText(getResources().getString(R.string.message));
        ImageView img_f3_back = (ImageView) view.findViewById(R.id.img_f3_back);
        txt_char.setText(maxNumber + " ");
        txt_post_comment.setText(getResources().getString(R.string.send));
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
        activity().showProgDilog(false);
        VolleyGetPost volleyGetPost = new VolleyGetPost(activity(), HomeActivity.instance, WebService.ADD_NUDGE, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e("VolleyRespnce", " Data Adapter " + response);
                activity().dismissProgDailog();
                activity().onBackPressed();

            }

            @Override
            public void onVolleyError(VolleyError error) {
                activity().dismissProgDailog();
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("event_id", EventId);
                params.put("nudges_to", attendy.getUserid());
                params.put("nudges_by", nudgeBy);
                params.put("facebook_id", attendy.getUserFacebookId());
                params.put("nudges", edt_comment.getText().toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        volleyGetPost.setRetryTime(15000);
        volleyGetPost.execute();
    }

    @Override
    public void onDestroy() {
        event_fragment.canCallWebservice = true;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_post_comment:
                if (nudgeBy.equals(Constants.KEY_NOTEXIST)) {
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
        VolleyGetPost volleyGetPost = new VolleyGetPost(activity(), activity(), WebService.ADD_EVENT, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG, " : " + WebService.ADD_EVENT + response);
                activity().dismissProgDailog();
                commentEvent();

            }

            @Override
            public void onVolleyError(VolleyError error) {
                activity().dismissProgDailog();
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
        volleyGetPost.execute();
    }


    /***
     * @param eventId
     * @param nudgeBy
     * @param attendy
     * @param event_fragment
     */
    public void setData(String eventId, String nudgeBy, EventAttendy attendy, Event_Fragment event_fragment) {
        this.nudgeBy = nudgeBy;
        this.EventId = eventId;
        this.attendy = attendy;
        this.event_fragment = event_fragment;
    }

    String getCutrrentTimeinFormat() {
        return (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date(System.currentTimeMillis()));
    }


}