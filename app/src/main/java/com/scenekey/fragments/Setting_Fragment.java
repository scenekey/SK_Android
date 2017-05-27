package com.scenekey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;

/**
 * Created by mindiii on 3/5/17.
 */

public class Setting_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f2_setting, null);
        activity().setBBvisiblity(View.GONE);
        return view;
    }


    HomeActivity activity(){
        return  HomeActivity.instance;
    }

    @Override
    public void onDestroy() {
        activity().setBBvisiblity(View.VISIBLE);
        super.onDestroy();
    }
}
