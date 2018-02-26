package com.scenekey.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.util.CircleTransform;

public class Home_No_Event_Fragment extends Fragment implements View.OnClickListener {

    private final String TAG = Home_No_Event_Fragment.class.toString();
    private Context context;
    private HomeActivity activity;
    private Demo_Event_Fragment demo_event_fragment;

    public Bitmap imageArray[];
    private boolean clicked;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home_no_event, container, false);
        v.findViewById(R.id.tvTryDemo).setOnClickListener(this);
        v.findViewById(R.id.tvSearch).setOnClickListener(this);
        activity.setTitleVisibility(View.VISIBLE);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle(context.getResources().getString(R.string.enter));

        demo_event_fragment=new Demo_Event_Fragment();
        try {
            createBitmap();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        activity= (HomeActivity) getActivity();
    }

    @Override
    public void onStart() {
        activity.setBBVisibility(View.VISIBLE,TAG);
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setBBVisibility(View.VISIBLE,TAG);
        activity.setTitleVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvTryDemo:
                try {
                    if (!clicked) {
                        activity.showProgDialog(false,TAG);

                        activity.addFragment(demo_event_fragment, 0);
                        demo_event_fragment.setImageArray(this);
                        clicked = true;
                    }


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clicked = false;
                        }
                    }, 5000);
                }catch (Exception e){
                    e.printStackTrace();
                }


                break;

            case R.id.tvSearch:
                activity.rtlv_four.callOnClick();
                break;
        }
    }

    private void createBitmap() {
        imageArray = new Bitmap[9];
        imageArray[0] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_1)).getBitmap());
        imageArray[1] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_2)).getBitmap());
        imageArray[2] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_3)).getBitmap());
        imageArray[3] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_4)).getBitmap());
        imageArray[4] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_5)).getBitmap());
        imageArray[5] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_6)).getBitmap());
        imageArray[6] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_7)).getBitmap());
        imageArray[7] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_8)).getBitmap());
        imageArray[8] = new CircleTransform().transform(((BitmapDrawable) getResources().getDrawable(R.drawable.room_8)).getBitmap());
    }
}
