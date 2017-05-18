package com.scenekey.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragments.Event_Fragment;
import com.scenekey.fragments.Message_Fargment;
import com.scenekey.helper.Constants;
import com.scenekey.models.EventAttendy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    Activity activity;
    Font font;
    Dialog dialog;
    View popupview;
    String data[];
    Event_Fragment fragment;
    private ArrayList<EventAttendy> roomPersons;
    private Context context;

    /***
     * @param context
     * @param android
     * @param activity
     * @param font
     * @param data     eventId , userId ,userFacebookID
     */
    public DataAdapter(Context context, ArrayList<EventAttendy> android, Activity activity, Font font, String[] data, Event_Fragment fragment) {
        this.roomPersons = android;
        this.context = context;
        this.activity = activity;
        this.font = font;
        this.data = data;
        this.fragment = fragment;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gva1_room_people, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        final EventAttendy attendy = roomPersons.get(i);
        final int position = i;
        viewHolder.txt_name_gvb1.setText(roomPersons.get(i).getUsername());
        //TODO : what to set user name or the stage name of the person.
        Picasso.with(context).load(roomPersons.get(i).getUserimage()).transform(new CircleTransform()).into(viewHolder.img_profile_gvb1);
        switch (attendy.getUser_status()) {
            case "busy":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_yellow_ring);
                break;
            case "avilable":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_green_ring);
                break;
            case "na":
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring);
                break;
            default:
                viewHolder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring);
                break;
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendy.getUserid().equals(HomeActivity.instance.getSessionManager().getUserInfo().getUserID())) {
                    popUpMy(position);
                } else {
                    if (fragment.inTime && fragment.inLocation) popupRoom(position);
                    else fragment.cantInteract();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomPersons.size();
    }

    void popUpMy(int position) {
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


            }
        });
        img_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_green.setImageResource(R.drawable.bg_green_ring);
                img_red.setImageResource(R.drawable.bg_red_ring);

            }
        });
        img_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_yellow.setImageResource(R.drawable.bg_yellow_ring);
                img_green.setImageResource(R.drawable.bg_green_ring);

            }
        });
        switch (roomPersons.get(position).getUser_status()) {
            case "na":
                img_red.setImageResource(R.drawable.bg_red_ring_accept);
                break;
            case "busy":
                img_yellow.setImageResource(R.drawable.bg_yellow_ring_accept);
                break;
            case "avilable":
                img_green.setImageResource(R.drawable.bg_green_ring_accept);
                break;
            default:
                img_red.setImageResource(R.drawable.bg_red_ring_accept);
                break;
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(popupview);
        Picasso.with(activity).load(roomPersons.get(position).getUserimage()).transform(new CircleTransform()).into(img_p1_profile);
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
        final EventAttendy attendy = roomPersons.get(i);
        ImageView img_p2_profile2, img_p2_profile, next, img_reply_img;
        final TextView txt_message, txt_timer;
        final TextView txt_nudge, txt_reply, txt_view_pro;
        final TextView txt_title;
        RelativeLayout nudge, profile;
        dialog = new Dialog(activity);
        popupview = LayoutInflater.from(activity).inflate(R.layout.pop_my_room, null);
        img_p2_profile = (ImageView) popupview.findViewById(R.id.img_p2_profile);
        img_p2_profile2 = (ImageView) popupview.findViewById(R.id.img_p2_profile2);
        img_reply_img = (ImageView) popupview.findViewById(R.id.img_reply_img);
        next = (ImageView) popupview.findViewById(R.id.next);
        txt_timer = (TextView) popupview.findViewById(R.id.txt_timer);
        txt_nudge = (TextView) popupview.findViewById(R.id.txt_nudge);
        txt_reply = (TextView) popupview.findViewById(R.id.txt_reply);
        txt_title = (TextView) popupview.findViewById(R.id.txt_title);
        txt_view_pro = (TextView) popupview.findViewById(R.id.txt_view_pro);
        txt_message = (TextView) popupview.findViewById(R.id.txt_message);
        nudge = (RelativeLayout) popupview.findViewById(R.id.nudge);
        txt_timer.setVisibility(View.GONE);
        profile = (RelativeLayout) popupview.findViewById(R.id.profile);

        Picasso.with(activity).load(attendy.getUserimage()).transform(new CircleTransform()).into(img_p2_profile);
        Picasso.with(activity).load(attendy.getUserimage()).transform(new CircleTransform()).into(img_p2_profile2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(popupview);
        next.setVisibility(View.GONE);
        //
        try {
            font.setFontEuphemia(txt_nudge, txt_reply, txt_view_pro);

            font.setFontFrankBookReg(txt_title, txt_message, txt_timer);
        } catch (Exception e) {

            e.printStackTrace();
        }
        //
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = HomeActivity.ActivityWidth - ((int) activity.getResources().getDimension(R.dimen._30sdp));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        img_reply_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


                Message_Fargment message_fargment = new Message_Fargment();
                ((HomeActivity) activity).addFragment(message_fargment, 1);
                message_fargment.setData(data[0], data[1], attendy, fragment);
            }
        });
        //popupview.setBackgroundColor(0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        nudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((HomeActivity) activity).showProgDilog(false);
                VolleyGetPost volleyGetPost = new VolleyGetPost(activity, HomeActivity.instance, WebService.ADD_NUDGE, false) {
                    @Override
                    public void onVolleyResponse(String response) {
                        Log.e("VolleyRespnce", " Data Adapter " + response);
                        ((HomeActivity) activity).dismissProgDailog();
                        dialog.dismiss();

                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        ((HomeActivity) activity).dismissProgDailog();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNetError() {
                        ((HomeActivity) activity).dismissProgDailog();
                        dialog.dismiss();
                    }

                    @Override
                    public Map<String, String> setParams(Map<String, String> params) {
                        params.put("event_id", data[0]);
                        params.put("nudges_to", attendy.getUserid());
                        params.put("nudges_by", data[1]);
                        params.put("facebook_id", attendy.getUserFacebookId());
                        params.put("nudges", Constants.NUDGE_YOUR);
                        return params;
                    }

                    @NotNull
                    @Override
                    public Map<String, String> setHeaders(Map<String, String> params) {
                        return params;
                    }
                };
                volleyGetPost.execute();
            }
        });

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


}
