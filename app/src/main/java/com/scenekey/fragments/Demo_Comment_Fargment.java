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

public class Demo_Comment_Fargment extends Fragment implements View.OnClickListener {
    static final String TAG = Demo_Comment_Fargment.class.toString();
    int maxNumber = 120;
    int count = 0;
    private TextView txt_char;
    private EditText edt_comment;

    Demo_Event_Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f3_comment, null);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_post_comment:
                fragment.addUserComment(edt_comment.getText().toString());
                activity().onBackPressed();
                break;
            case R.id.img_f3_back:
                activity().onBackPressed();
                break;
        }
    }


    Demo_Comment_Fargment setData( Demo_Event_Fragment fragment) {
        this.fragment = fragment;
        return this;
    }

    String getCutrrentTimeinFormat() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(System.currentTimeMillis()));
    }


}
