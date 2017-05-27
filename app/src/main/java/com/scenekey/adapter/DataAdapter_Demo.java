package com.scenekey.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.CustomToastDialog;
import com.scenekey.Utility.Font;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragments.Home_no_Event;
import com.scenekey.models.RoomPerson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter_Demo extends RecyclerView.Adapter<DataAdapter_Demo.ViewHolder> {
    ArrayList<RoomPerson> roomPersons;
    Context context;
    Activity activity;
    View popupview;
    Dialog dialog;
    Font font;
    Bitmap imageArray[];


    public DataAdapter_Demo(Context context, ArrayList<RoomPerson> android, Activity activity, Font font, Home_no_Event home_no_event) {
        this.roomPersons = android;
        this.context = context;
        this.activity = activity;
        this.font = font;
        this.imageArray = home_no_event.getImageArrray();
    }

    @Override
    public DataAdapter_Demo.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gva1_room_people, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter_Demo.ViewHolder viewHolder, int i) {
        final RoomPerson person = roomPersons.get(i);
        final int position = i;
        viewHolder.txt_name_gvb1.setText(roomPersons.get(i).getAndroid_version_name());
        Font font = new Font(activity);
        font.setFontArial_Regular(viewHolder.txt_name_gvb1);
        if (position == 8)
            Picasso.with(context).load(person.getAndroid_image_url()).transform(new CircleTransform()).into(viewHolder.img_profile_gvb1);
            // Picasso.with(context).load(Integer.parseInt(person.getAndroid_image_url())).into(viewHolder.img_profile_gvb1);
        else
            viewHolder.img_profile_gvb1.setImageBitmap(imageArray[i]);
        // Picasso.with(context).load(person.getAndroid_image_url()).transform(new CircleTransform()).into(viewHolder.img_profile_gvb1);
        // Picasso.with(context).load(person.getAndroid_image_url()).into(viewHolder.img_profile_gvb1);
        switch (person.getStaus()) {
            case "busy":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_yellow_ring);
                break;
            case "avilable":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_green_ring);
                break;
            case "na":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring);
                break;
        }
        viewHolder.img_profile_gvb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 8) popUpMy();
                else popupRoom(position);
            }
        });

        //Log.e("Person", person.getAndroid_image_url() + "");

    }

    @Override
    public int getItemCount() {
        return roomPersons.size();
    }

    int getResources(int i) {
        int result = R.drawable.room_1;
        switch (i) {
            case 0:
                result = R.drawable.room_1;
                break;
            case 1:
                result = R.drawable.room_2;
                break;
            case 2:
                result = R.drawable.room_3;
                break;
            case 3:
                result = R.drawable.room_4;
                break;
            case 4:
                result = R.drawable.room_5;
                break;
            case 5:
                result = R.drawable.room_6;
                break;
            case 6:
                result = R.drawable.room_7;
                break;
            case 7:
                result = R.drawable.room_8;
                break;
        }
        return result;
    }

    void popUpMy() {
        final ImageView img_red, img_yellow, img_green, img_p1_profile;
        dialog = new Dialog(activity);
        final TextView txt_stop, txt_caution, txt_go;
        final TextView txt_title;

        popupview = LayoutInflater.from(activity).inflate(R.layout.pop_my_profile, null);
        img_p1_profile = (ImageView) popupview.findViewById(R.id.img_p1_profile);
        img_green = (ImageView) popupview.findViewById(R.id.img_green);
        img_yellow = (ImageView) popupview.findViewById(R.id.img_yellow);
        img_red = (ImageView) popupview.findViewById(R.id.img_red);
        txt_stop = (TextView) popupview.findViewById(R.id.txt_stop);
        txt_caution = (TextView) popupview.findViewById(R.id.txt_caution);
        txt_go = (TextView) popupview.findViewById(R.id.txt_go);
        txt_title = (TextView) popupview.findViewById(R.id.txt_title);
        img_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_red.setImageResource(R.drawable.bg_red_ring);
                img_yellow.setImageResource(R.drawable.bg_yellow_ring);
                setUserStatus(2, (ImageView) v);

            }
        });
        img_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_green.setImageResource(R.drawable.bg_green_ring);
                img_red.setImageResource(R.drawable.bg_red_ring);
                setUserStatus(1, (ImageView) v);
            }
        });
        img_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_yellow.setImageResource(R.drawable.bg_yellow_ring);
                img_green.setImageResource(R.drawable.bg_green_ring);
                setUserStatus(3, (ImageView) v);
            }
        });
        switch (roomPersons.get(8).getStaus()) {
            case "na":
                img_red.setImageResource(R.drawable.bg_red_ring_accept);
                break;
            case "busy":
                img_yellow.setImageResource(R.drawable.bg_yellow_ring_accept);
                break;
            case "avilable":
                img_green.setImageResource(R.drawable.bg_green_ring_accept);
                break;
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(popupview);
        Picasso.with(activity).load(roomPersons.get(8).getAndroid_image_url()).transform(new CircleTransform()).into(img_p1_profile);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = HomeActivity.ActivityWidth - ((int) activity.getResources().getDimension(R.dimen._30sdp));
        dialog.getWindow().setAttributes(lp);
        font.setFontFranklinRegular(txt_stop, txt_caution, txt_go);
        font.setFontFrankBookReg(txt_title);
        dialog.show();
        //popupview.setBackgroundColor(0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    void popupRoom(int i) {
        RoomPerson person = roomPersons.get(i);
        ImageView img_p2_profile2, img_p2_profile, next;
        RelativeLayout nudge;
        final TextView txt_message, txt_timer;
        final TextView txt_nudge, txt_reply, txt_view_pro;
        final TextView txt_title;
        dialog = new Dialog(activity);
        popupview = LayoutInflater.from(activity).inflate(R.layout.pop_my_room, null);
        img_p2_profile = (ImageView) popupview.findViewById(R.id.img_p2_profile);
        img_p2_profile2 = (ImageView) popupview.findViewById(R.id.img_p2_profile2);
        next = (ImageView) popupview.findViewById(R.id.next);
        txt_timer = (TextView) popupview.findViewById(R.id.txt_timer);
        txt_nudge = (TextView) popupview.findViewById(R.id.txt_nudge);
        txt_reply = (TextView) popupview.findViewById(R.id.txt_reply);
        txt_title = (TextView) popupview.findViewById(R.id.txt_title);
        txt_view_pro = (TextView) popupview.findViewById(R.id.txt_view_pro);
        txt_message = (TextView) popupview.findViewById(R.id.txt_message);
        nudge = (RelativeLayout) popupview.findViewById(R.id.nudge);
        nudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodNudge();
            }
        });
        txt_timer.setVisibility(View.GONE);
        img_p2_profile.setImageBitmap(imageArray[i]);
        img_p2_profile2.setImageBitmap(imageArray[i]);
        //Picasso.with(activity).load(person.getAndroid_image_url()).transform(new CircleTransform()).into(img_p2_profile);
        //Picasso.with(activity).load(person.getAndroid_image_url()).transform(new CircleTransform()).into(img_p2_profile2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(popupview);
        next.setVisibility(View.GONE);
        //
        font.setFontEuphemia(txt_nudge, txt_reply, txt_view_pro);
        font.setFontFrankBookReg(txt_title, txt_message, txt_timer);
        //
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = HomeActivity.ActivityWidth - ((int) activity.getResources().getDimension(R.dimen._30sdp));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        //popupview.setBackgroundColor(0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    void setUserStatus(int i, ImageView imageView) {

        switch (i) {
            case 1:
                roomPersons.get(8).setStaus("busy");
                imageView.setImageResource(R.drawable.bg_yellow_ring_accept);
                break;
            case 2:
                roomPersons.get(8).setStaus("avilable");
                imageView.setImageResource(R.drawable.bg_green_ring_accept);
                break;
            case 3:
                roomPersons.get(8).setStaus("na");
                imageView.setImageResource(R.drawable.bg_red_ring_accept);
                break;

        }
        notifyDataSetChanged();
        //dialog.dismiss();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_name_gvb1;
        private ImageView img_profile_gvb1;

        public ViewHolder(View view) {
            super(view);

            txt_name_gvb1 = (TextView) view.findViewById(R.id.txt_name_gvb1);
            img_profile_gvb1 = (ImageView) view.findViewById(R.id.img_profile_gvb1);

        }


    }

    public void goodNudge(){
        CustomToastDialog customToastDialogA = new CustomToastDialog(activity);
        customToastDialogA.setMessage(activity.getResources().getString(R.string.goodNudge));
        customToastDialogA.show();
    }

}
