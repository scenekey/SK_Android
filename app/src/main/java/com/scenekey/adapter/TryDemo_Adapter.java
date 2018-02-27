package com.scenekey.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragment.Demo_Event_Fragment;
import com.scenekey.fragment.Home_No_Event_Fragment;
import com.scenekey.model.RoomPerson;
import com.scenekey.util.CircleTransform;
import com.scenekey.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mindiii on 26/2/18.
 */

public class TryDemo_Adapter extends RecyclerView.Adapter<TryDemo_Adapter.ViewHolder> {

    private ArrayList<RoomPerson> roomPersonList;
    private Activity activity;
    private View popupview;
    private Dialog dialog;

    private Bitmap imageArray[];
    private Demo_Event_Fragment demo_event_fragment;


    public TryDemo_Adapter(ArrayList<RoomPerson> roomPersonList, HomeActivity activity, Home_No_Event_Fragment home_no_event, Demo_Event_Fragment demo_event_fragment) {
        this.roomPersonList = roomPersonList;
        this.activity = activity;
        this.imageArray = home_no_event.imageArray;
        this.demo_event_fragment = demo_event_fragment;
    }

    @Override
    public TryDemo_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_demo_room, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TryDemo_Adapter.ViewHolder holder, int i) {
        final RoomPerson person = roomPersonList.get(i);
        final int position = i;
        if(person != null){
            holder.txt_name_gvb1.setText(person.android_version_name.split("\\s+")[0]);
            if (position == 8)
            {
                try {

                    Picasso.with(activity).load(person.android_image_url).transform(new CircleTransform()).into(holder.img_profile_gvb1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
             else
                holder.img_profile_gvb1.setImageBitmap(imageArray[i]);

            switch (person.status) {
                case "busy":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_yellow_ring);
                    holder.txt_name_gvb1.setTextColor(activity.getResources().getColor(R.color.yellow_ring));
                    break;
                case "avilable":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_green_ring);
                    holder.txt_name_gvb1.setTextColor(activity.getResources().getColor(R.color.green_ring));
                    break;
                case "na":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring_2);
                    holder.txt_name_gvb1.setTextColor(activity.getResources().getColor(R.color.red_ring));
                    break;
            }

            holder.img_profile_gvb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* if (position == 8) popUpMy();
                    else newPopUp(position);*/
                    Utility.showToast(activity,activity.getResources().getString(R.string.underDevelopment),0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return roomPersonList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_name_gvb1;
        private ImageView img_profile_gvb1;

        ViewHolder(View view) {
            super(view);

            txt_name_gvb1 =  view.findViewById(R.id.txt_name_gvb1);
            img_profile_gvb1 =  view.findViewById(R.id.img_profile_gvb1);
        }
    }


}
