package com.scenekey.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragment.Event_Fragment;
import com.scenekey.model.EventAttendy;
import com.scenekey.util.CircleTransform;
import com.scenekey.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import org.apache.commons.lang3.StringEscapeUtils;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private static final String TAG = DataAdapter.class.toString();
    private HomeActivity activity;

    private Dialog dialog;
    private View popupview;
    private String data[];
    private Event_Fragment fragment;
    private ArrayList<EventAttendy> roomPersons;
    int count;


    public DataAdapter(Activity activity, ArrayList<EventAttendy> list, String[] data, Event_Fragment fragment) {
        this.roomPersons = list;
        this.activity = (HomeActivity) activity;
        this.data = data;
        this.fragment = fragment;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_demo_room, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        final EventAttendy attendy = roomPersons.get(i);
        final int position = i;
        viewHolder.txt_name_gvb1.setText(roomPersons.get(i).username.split("\\s+")[0]);
        try {
            Picasso.with(activity).load(roomPersons.get(i).getUserimage()).placeholder(R.drawable.image_defult_profile).transform(new CircleTransform()).into(viewHolder.img_profile_gvb1);
        }catch (Exception e){}
        switch (attendy.user_status) {

            case "1":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_green_ring);
                viewHolder.txt_name_gvb1.setTextColor(activity.getResources().getColor(R.color.green_ring));
                break;
            case "2":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_yellow_ring);
                viewHolder.txt_name_gvb1.setTextColor(activity.getResources().getColor(R.color.yellow_ring));
                break;
            case "3":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring_2);
                viewHolder.txt_name_gvb1.setTextColor(activity.getResources().getColor(R.color.red_ring));
                break;
            default:
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring_2);
                viewHolder.txt_name_gvb1.setTextColor(activity.getResources().getColor(R.color.red_ring));
                break;
        }
        if(attendy.userid.equals(activity.userInfo().userID)){
            //TOdo new
           // viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring);
           // viewHolder.txt_name_gvb1.setTextColor(activity.getResources().getColor(R.color.red_ring));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   if (attendy.userid.equals(activity.userInfo().userID)) {
                    popUpMy(position );
                } else {
                    try {
                        if (fragment.check())newPopUp(attendy,false);
                        //    popupRoom(position);

                        else fragment.cantInteract();
                    } catch (ParseException e) {
                       Utility.showToast(activity,activity.getString(R.string.somethingwentwrong),0);
                    }

                }*/
             Utility.showToast(activity,activity.getString(R.string.underDevelopment),0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomPersons.size();
    }

    void popUpMy(final int position) {
        final ImageView img_red, img_yellow, img_green, img_p1_profile;
        dialog = new Dialog(activity ,android.R.style.Theme_Translucent);
        final TextView txt_stop, txt_caution, txt_go;
        final TextView txt_title ,txt_my_details;

        popupview = LayoutInflater.from(activity).inflate(R.layout.custom_my_profile_popup, null);
        img_p1_profile =  popupview.findViewById(R.id.img_p1_profile);
        img_green =  popupview.findViewById(R.id.img_green);
        img_yellow =  popupview.findViewById(R.id.img_yellow);
        img_red =  popupview.findViewById(R.id.img_red);
        txt_stop =  popupview.findViewById(R.id.txt_stop);
        txt_caution =  popupview.findViewById(R.id.txt_caution);
        txt_go =  popupview.findViewById(R.id.txt_go);
        txt_title =  popupview.findViewById(R.id.txt_title);
        txt_my_details =  popupview.findViewById(R.id.txt_my_details);
        img_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_red.setImageResource(R.drawable.bg_red_ring_2);
                img_yellow.setImageResource(R.drawable.bg_yellow_ring);
               // setUserStatus(1, (ImageView) v);

            }
        });
        img_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_green.setImageResource(R.drawable.bg_green_ring);
                img_red.setImageResource(R.drawable.bg_red_ring_2);
              //  setUserStatus(2, (ImageView) v);
            }
        });
        img_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_yellow.setImageResource(R.drawable.bg_yellow_ring);
                img_green.setImageResource(R.drawable.bg_green_ring);
              //  setUserStatus(3, (ImageView) v);
            }
        });
        txt_my_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // callProfile(roomPersons.get(position),true,0);
            }
        });
        img_p1_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // callProfile(roomPersons.get(position),true,0);
            }
        });
        switch (roomPersons.get(position).user_status) {
            case "1":
                img_green.setImageResource(R.drawable.bg_green_ring_accept);
                break;
            case "2":
                img_yellow.setImageResource(R.drawable.bg_yellow_ring_accept);
                break;
            case "3":
                img_red.setImageResource(R.drawable.bg_red_ring_accept);
                break;
            default:
                img_red.setImageResource(R.drawable.bg_red_ring_accept);
                break;
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(popupview);
        Picasso.with(activity).load(roomPersons.get(position).getUserimage()).transform(new CircleTransform()).placeholder(R.drawable.image_defult_profile).into(img_p1_profile);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.width = HomeActivity.ActivityWidth - ((int) activity.getResources().getDimension(R.dimen._30sdp)); old
        lp.width = HomeActivity.ActivityWidth ;
        dialog.getWindow().setAttributes(lp);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                activity.hideStatusBar();
            }
        });
        dialog.show();
        //popupview.setBackgroundColor(0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_name_gvb1;
        private ImageView img_profile_gvb1;

        public ViewHolder(View view) {
            super(view);

            txt_name_gvb1 =  view.findViewById(R.id.txt_name_gvb1);
            img_profile_gvb1 =  view.findViewById(R.id.img_profile_gvb1);
        }
    }








}
