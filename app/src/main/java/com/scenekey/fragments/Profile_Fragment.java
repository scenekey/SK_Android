package com.scenekey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.Font;
import com.scenekey.activity.HomeActivity;
import com.scenekey.models.EventAttendy;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mindiii on 28/4/17.
 */

public class Profile_Fragment extends Fragment implements View.OnClickListener {
    public static final String TAG = Profile_Fragment.class.toString();
    CircleImageView img_profile_pic2;
    EventAttendy attendy;
    boolean myProfile;
    private ImageView img_profile_pic, img_cross, img_left, img_right;
    private TextView txt_event_count, txt_dimmer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f2_profile_own, null);
        img_profile_pic = (ImageView) v.findViewById(R.id.img_profile_pic);
        img_left = (ImageView) v.findViewById(R.id.img_left);
        img_right = (ImageView) v.findViewById(R.id.img_right);
        img_cross = (ImageView) v.findViewById(R.id.img_cross);
        img_profile_pic2 = (CircleImageView) v.findViewById(R.id.img_profile_pic2);
        txt_event_count = (TextView) v.findViewById(R.id.txt_event_count);
        txt_dimmer = (TextView) v.findViewById(R.id.txt_dimmer);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txt_EE;
        txt_EE = (TextView) view.findViewById(R.id.txt_EE);
        img_profile_pic.setOnClickListener(this);
        txt_dimmer.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        img_profile_pic2.setVisibility(View.INVISIBLE);


        Font font = new Font(activity());
        //TODO : Seeting the fonts
        //TODO :Setting the Event count to 99+ if greter then 99

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


    void profileImgClick() {
        Log.e(TAG, "Clicked");
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.profile_pic_scale_up);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                img_profile_pic2.setAlpha(1.0f);
                img_profile_pic2.setVisibility(View.VISIBLE);
                txt_dimmer.setVisibility(View.VISIBLE);
                Animation dimmer = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_to_full);
                txt_dimmer.startAnimation(dimmer);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_dimmer.setVisibility(View.VISIBLE);
                img_cross.setVisibility(View.VISIBLE);
                img_right.setVisibility(View.VISIBLE);
                img_left.setVisibility(View.VISIBLE);
                img_profile_pic2.setBorderColor(getResources().getColor(R.color.white));

               /* WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) rtlv_all_popUP.getLayoutParams();
                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.3f;
                wm.updateViewLayout(rtlv_all_popUP, p);*/
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
                txt_dimmer.startAnimation(dimmer);

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


    /**
     * @param attendy   if do not Eventattendy object just create one , set userId URL and pass it.
     * @param myProfile if user comming to show his own profile then true otherwise false.
     * @return
     */
    Profile_Fragment setData(EventAttendy attendy, boolean myProfile) {
        this.attendy = attendy;
        this.myProfile = myProfile;
        return this;
    }
}
