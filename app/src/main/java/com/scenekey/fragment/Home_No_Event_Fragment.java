package com.scenekey.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.util.SceneKey;
import com.scenekey.util.Utility;

public class Home_No_Event_Fragment extends Fragment implements View.OnClickListener {

    private Context context;
    private HomeActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        activity= (HomeActivity) getActivity();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvTryDemo:
                Utility.showToast(context,getString(R.string.underDevelopment),0);
                break;

            case R.id.tvSearch:
                activity.rtlv_four.callOnClick();
                break;
        }
    }
}
