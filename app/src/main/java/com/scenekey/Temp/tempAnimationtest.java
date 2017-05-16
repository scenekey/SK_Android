package com.scenekey.Temp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.scenekey.R;

/**
 * Created by mindiii on 11/4/17.
 */

public class tempAnimationtest extends Fragment {


    //From Home_no event
    TextView txt_try_button_f1, txt_animated;
    Activity activity;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f1_home_no_event, null);
        txt_try_button_f1 = (TextView) v.findViewById(R.id.txt_try_button_f1);
        // txt_animated    = (TextView) v.findViewById(R.id.txt_animated);
        // Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animate_cus_button);
        Animation myFadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animate_cus_button2);
        Animation trnslate_animate = AnimationUtils.loadAnimation(getActivity(), R.anim.translet_bar);

//fade it in, and fade it out.
        // txt_try_button_f1.startAnimation(myFadeInAnimation);
        txt_try_button_f1.startAnimation(myFadeOutAnimation);
        txt_animated.startAnimation(trnslate_animate);
        return v;
    }


    void fragmentBasic(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }
/*
in your Activity
    <FrameLayout
    android:layout_width="match_parent"
    android:layout_height="@dimen/_35sdp"
    android:layout_marginLeft="@dimen/_50sdp"
    android:layout_marginRight="@dimen/_50sdp"
    android:layout_marginTop="@dimen/_20sdp"
    android:background="@drawable/background_f1_try_demo">
    <TextView
    android:id="@+id/txt_animated"
    android:layout_width="@dimen/_2sdp"
    android:layout_height="match_parent"
    android:background="@color/white"/>
    <TextView
    android:id="@+id/txt_try_button_f2"
    android:layout_width="match_parent"
    android:layout_height="@dimen/_35sdp"
    android:gravity="center"
    android:textColor="@color/white"
    android:textSize="@dimen/_16sdp"
    android:text="@string/tryd"/>
    </FrameLayout>*/
}
