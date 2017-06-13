package com.scenekey.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scenekey.R;
import com.scenekey.Utility.Permission;
import com.scenekey.Utility.Util;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.ImageUplodeAdpter;
import com.scenekey.helper.Constants;
import com.squareup.picasso.Picasso;

/**
 * Created by mindiii-rahul on 1/5/17.
 */

public class Image_uploade_fragment extends Fragment implements View.OnClickListener {
    private static final String TAG = Image_uploade_fragment.class.toString();
    RecyclerView recyvlerview;
    RecyclerView.LayoutManager layoutManager;
    ImageUplodeAdpter adpter;
    ImageView img_profile;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fx_image_uploade, null);
        recyvlerview = (RecyclerView) v.findViewById(R.id.recyvlerview);
        img_profile = (ImageView) v.findViewById(R.id.img_profile);
        HomeActivity.instance.setBBvisiblity(View.GONE,TAG);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        adpter = new ImageUplodeAdpter((HomeActivity) getActivity(), this);
        recyvlerview.setLayoutManager(layoutManager);
        recyvlerview.setAdapter(adpter);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) img_profile.getLayoutParams();
        params.height = (HomeActivity.ActivityWidth * 3) / 4;
        Util.setStatusBarColor(HomeActivity.instance, R.color.white);
        ImageView img_back = (ImageView) view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        //getActivity().setTheme(R.style.theme);
        Picasso.with(HomeActivity.instance).load(HomeActivity.instance.userInfo().getUserImage()).into(img_profile);
    }

    @Override
    public void onStart() {
        super.onStart();
        Permission permission = new Permission(getActivity(), HomeActivity.instance);
        permission.checkExternalStorage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RESULT_LOAD) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String img_Decodable_Str = cursor.getString(columnIndex);
            adpter.addImage(BitmapFactory
                    .decodeFile(img_Decodable_Str));
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HomeActivity.instance.setBBvisiblity(View.GONE,TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
        }
    }
}
