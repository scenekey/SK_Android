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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.CustomToastDialog;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragments.Demo_Event_Fragment;
import com.scenekey.fragments.Demo_Event_Fragment_ListView;
import com.scenekey.fragments.Demo_Message_Fargment;
import com.scenekey.fragments.Demo_Profile_Fragment;
import com.scenekey.fragments.Home_no_Event;
import com.scenekey.fragments.Profile_Fragment;
import com.scenekey.helper.Constants;
import com.scenekey.models.EventAttendy;
import com.scenekey.models.Feeds;
import com.scenekey.models.RoomPerson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mindiii on 8/6/17.
 */

public class Event_Grid_Adapter extends BaseAdapter {
    ArrayList<RoomPerson> personlist;
    Activity activity;
    View popupview;
    Dialog dialog;
    Font font;
    Bitmap imageArray[];
    Demo_Event_Fragment_ListView demo_event_fragment;

    public Event_Grid_Adapter(ArrayList<RoomPerson> roomPersons) {
        this.personlist = roomPersons;
    }

    public Event_Grid_Adapter(HomeActivity activity, ArrayList<RoomPerson> roomPersons, HomeActivity instance, Font font, Home_no_Event home_no_event, Demo_Event_Fragment_ListView demo_event_fragment_listView) {
        this.personlist = roomPersons;
        this.activity = activity;
        this.font = font;
        this.imageArray = home_no_event.getImageArrray();
        this.demo_event_fragment = demo_event_fragment_listView;
    }

    @Override
    public int getCount() {

        return personlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        final int i = position;
        if(itemView == null){
            LayoutInflater inflater = (LayoutInflater) HomeActivity.instance
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.gva1_room_people, parent, false);
            Event_Grid_Adapter.Holder holder = new Event_Grid_Adapter.Holder();
            holder.txt_name_gvb1 = (TextView) itemView.findViewById(R.id.txt_name_gvb1);
            holder.img_profile_gvb1 = (ImageView) itemView.findViewById(R.id.img_profile_gvb1);
            final RoomPerson person = personlist.get(i);
            Font font = new Font(activity);
            font.setFontArial_Regular(holder.txt_name_gvb1);
            holder.txt_name_gvb1.setText(personlist.get(i).getAndroid_version_name().split("\\s+")[0]);
            if (i == 8)
                Picasso.with(activity).load(person.getAndroid_image_url()).transform(new CircleTransform()).into(holder.img_profile_gvb1);
                // Picasso.with(context).load(Integer.parseInt(person.getAndroid_image_url())).into(viewHolder.img_profile_gvb1);
            else
                holder.img_profile_gvb1.setImageBitmap(imageArray[i]);
            // Picasso.with(context).load(person.getAndroid_image_url()).transform(new CircleTransform()).into(viewHolder.img_profile_gvb1);
            // Picasso.with(context).load(person.getAndroid_image_url()).into(viewHolder.img_profile_gvb1);
            switch (person.getStaus()) {
                case "busy":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_yellow_ring);
                    break;
                case "avilable":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_green_ring);
                    break;
                case "na":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring);
                    break;
            }
            holder.img_profile_gvb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (i == 8) popUpMy();
                    else popupRoom(position);*/
                }
            });
            itemView.setTag(holder);
        }
        else {
            Event_Grid_Adapter.Holder holder  = (Event_Grid_Adapter.Holder) itemView.getTag();
            final RoomPerson person = personlist.get(i);

            Font font = new Font(activity);
            font.setFontArial_Regular(holder.txt_name_gvb1);
            holder.txt_name_gvb1.setText(personlist.get(i).getAndroid_version_name().split("\\s+")[0]);
            if (i == 8)
                Picasso.with(activity).load(person.getAndroid_image_url()).transform(new CircleTransform()).into(holder.img_profile_gvb1);

            else
                holder.img_profile_gvb1.setImageBitmap(imageArray[i]);
            switch (person.getStaus()) {
                case "busy":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_yellow_ring);
                    break;
                case "avilable":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_green_ring);
                    break;
                case "na":
                    holder.img_profile_gvb1.setBackgroundResource(R.drawable.bg_red_ring);
                    break;
            }
            holder.img_profile_gvb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (i == 8) popUpMy();
                    else popupRoom(position);*/
                }
            });
            itemView.setTag(holder);

        }
        return itemView;
    }

    class Holder{
        ImageView img_profile_gvb1;
        TextView txt_name_gvb1;
    }

    void popUpMy() {
        final ImageView img_red, img_yellow, img_green, img_p1_profile;
        dialog = new Dialog(activity);
        final TextView txt_stop, txt_caution, txt_go;
        final TextView txt_title ,txt_my_details;

        popupview = LayoutInflater.from(activity).inflate(R.layout.pop_my_profile, null);
        img_p1_profile = (ImageView) popupview.findViewById(R.id.img_p1_profile);
        img_green = (ImageView) popupview.findViewById(R.id.img_green);
        img_yellow = (ImageView) popupview.findViewById(R.id.img_yellow);
        img_red = (ImageView) popupview.findViewById(R.id.img_red);
        txt_stop = (TextView) popupview.findViewById(R.id.txt_stop);
        txt_caution = (TextView) popupview.findViewById(R.id.txt_caution);
        txt_go = (TextView) popupview.findViewById(R.id.txt_go);
        txt_my_details = (TextView) popupview.findViewById(R.id.txt_my_details);
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
        img_p1_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventAttendy eventAttendy = new EventAttendy();
                eventAttendy.setUserimage(((HomeActivity)activity).userInfo().getUserImage());
                eventAttendy.setUsername(((HomeActivity)activity).userInfo().getUserName());
                eventAttendy.setUserid(((HomeActivity)activity).userInfo().getUserID());
                eventAttendy.setUserFacebookId(((HomeActivity)activity).userInfo().getFacebookId());
                callProfile(eventAttendy,true);
            }
        });
        txt_my_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventAttendy eventAttendy = new EventAttendy();
                eventAttendy.setUserimage(((HomeActivity)activity).userInfo().getUserImage());
                eventAttendy.setUsername(((HomeActivity)activity).userInfo().getUserName());
                eventAttendy.setUserid(((HomeActivity)activity).userInfo().getUserID());
                eventAttendy.setUserFacebookId(((HomeActivity)activity).userInfo().getFacebookId());
                callProfile(eventAttendy,true);
            }
        });
        switch (personlist.get(8).getStaus()) {
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
        Picasso.with(activity).load(personlist.get(8).getAndroid_image_url()).transform(new CircleTransform()).into(img_p1_profile);
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

    void popupRoom(final int i) {
        final RoomPerson person = personlist.get(i);
        ImageView img_p2_profile2, img_p2_profile, next ,img_reply_img;
        RelativeLayout nudge;
        final TextView txt_message, txt_timer;
        final TextView txt_nudge, txt_reply, txt_view_pro;
        final TextView txt_title;
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
        txt_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageFregment();
            }
        });
        img_reply_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageFregment();
            }
        });
        img_p2_profile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callProfile(person,false);
            }
        });
        txt_view_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callProfile(person,false);
            }
        });
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
                personlist.get(8).setStaus("busy");
                imageView.setImageResource(R.drawable.bg_yellow_ring_accept);
                break;
            case 2:
                personlist.get(8).setStaus("avilable");
                imageView.setImageResource(R.drawable.bg_green_ring_accept);
                break;
            case 3:
                personlist.get(8).setStaus("na");
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

    void messageFregment(){
        dialog.dismiss();
        ((HomeActivity)activity).addFragment(new Demo_Message_Fargment(),1);
    }

    void callProfile(RoomPerson attendy , boolean ownProfile) {
        dialog.dismiss();
        ((HomeActivity) activity).addFragment(new Demo_Profile_Fragment().setData(attendy, ownProfile), 1);
    }

    void callProfile(EventAttendy attendy ,boolean ownProfile) {
        dialog.dismiss();
       // ((HomeActivity) activity).addFragment(new Profile_Fragment().setData(attendy, ownProfile,demo_event_fragment), 1);
    }
}
