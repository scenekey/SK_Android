package com.scenekey.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.Font;
import com.scenekey.activity.HomeActivity;

/**
 * Created by mindiii on 3/5/17.
 */

public class Setting_Fragment extends Fragment implements View.OnClickListener{

    EditText edt_last_name,edt_first_name,edt_email,edt_location;
    TextView txt_feedback,txt_logout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f2_setting, null);
        activity().setBBvisiblity(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText settingText = (EditText) view.findViewById(R.id.edt_terms);
        EditText edt_privacy = (EditText) view.findViewById(R.id.edt_privacy);
        ImageView img_f1_back = (ImageView) view.findViewById(R.id.img_f1_back);
        TextView txt_f1_title = (TextView) view.findViewById(R.id.txt_f1_title);
        LinearLayout mainlayout= (LinearLayout) view.findViewById(R.id.mainlayout);
        edt_first_name = (EditText) view.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) view.findViewById(R.id.edt_last_name);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_location = (EditText) view.findViewById(R.id.edt_location);
        txt_logout = (TextView) view.findViewById(R.id.txt_logout);
        txt_feedback = (TextView) view.findViewById(R.id.txt_feedback);
        edt_first_name.setText(activity().userInfo().getFirstname());
        edt_last_name.setText(activity().userInfo().getLastname());
        if(!activity().userInfo().getEmail().contains(activity().userInfo().getFacebookId()))edt_email.setText(activity().userInfo().getEmail());
        else edt_email.setText(getString(R.string.noemail));


        Font font = new Font(activity());/*
        font.setFontLibreFranklin_SemiBold(settingText,edt_privacy);
        font.setFontFrankBookReg(edt_first_name,edt_last_name);*/
        font.setFontFranklinRegular(edt_email,edt_first_name,edt_last_name,settingText,edt_privacy,edt_location,txt_f1_title);
        setClick(edt_email,edt_first_name,edt_last_name,settingText,edt_privacy,edt_location,txt_f1_title,mainlayout,txt_logout,txt_feedback,img_f1_back);
        font.setFontRailRegular(txt_feedback,txt_logout);
    }

    HomeActivity activity(){
        return  HomeActivity.instance;
    }

    @Override
    public void onDestroy() {
        //activity().setBBvisiblity(View.VISIBLE);
        super.onDestroy();
    }
    void setClick(View... views){
        for(View view:views){
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_logout:
            activity().getSessionManager().logout(activity());
            break;
            case R.id.txt_feedback:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, "info@scenekey.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "feedback:");
                try {
                    startActivity(intent);
                }catch (Exception e){

                }

                break;
            case R.id.img_f1_back:
                activity().onBackPressed();
                break;
        }
    }
}
