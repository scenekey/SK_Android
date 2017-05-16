package com.scenekey.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.Font;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragments.Event_Fragment;
import com.scenekey.models.Event;
import com.scenekey.models.Events;
import com.scenekey.models.Venue;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mindiii on 26/4/17.
 */

public class TreandingAdapter extends RecyclerView.Adapter<TreandingAdapter.Holder> {
    HomeActivity activity;
    ArrayList<Events> eventsList;
    boolean clicked;


    public TreandingAdapter(HomeActivity activity, ArrayList<Events> eventsList) {
        this.activity = activity;
        this.eventsList = eventsList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(View.inflate(activity, R.layout.rclv_trending, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final Events object = eventsList.get(position);
        final Venue venue = object.getVenue();
        final Event event = object.getEvent();
        Font font = new Font(activity);
        font.setFontFrankBookReg(holder.txt_eventAdress, holder.txt_eventDate, holder.txt_eventName, holder.txt_time);
        try {
            //int width = holder.img_event.getWidth();
            //int height =holder.img_event.getHeight();
            //.resize(width,height).onlyScaleDown()
            Picasso.with(activity).load(event.getImage()).placeholder(R.drawable.scene3).into(holder.img_event);
        } catch (Exception e) {

        }
        try {
            holder.txt_time.setText(convertTime(event.getEvent_time()));
            holder.txt_time.setTypeface(font.FrankBookRegular());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txt_eventName.setText(event.getEvent_name());
        holder.txt_eventAdress.setText(venue.getAddress());
        holder.txt_eventDate.setText(convertDate(event.getEvent_date()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                if (!clicked) {
                    Event_Fragment frg = new Event_Fragment();
                    frg.setEventId(event.getEvent_id());
                    activity.addFragment(frg, 0);
                    clicked = true;
                }
                try {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            v.setClickable(true);
                            clicked = false;
                        }
                    }, 2000);
                } catch (Exception e) {
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if (eventsList != null) return eventsList.size();
        return 0;
    }

    String convertDate(String date) {
        //2017-04-26 21:00:00TO01:00:00
        /*Log.e("Date",date +" ");
        String[] str = (((date.replace("TO"," ")).replace("-"," ")).replace(":"," ")).replace("T"," ").split(" ");

        for(String s: str){
            Log.e("Date",s +" : ");
        }
        Date date1 = new Date();
        date1.setMonth(Integer.parseInt(str[1])-1);
        date1.setYear(Integer.parseInt(str[0]));
        date1.setDate(Integer.parseInt(str[2]));
        date1.setHours(Integer.parseInt(str[3]));
        date1.setMinutes(Integer.parseInt(str[4]));
        return new SimpleDateFormat("MMMM dd,yyyy HH:mm aa").format(date1);*/
        String[] str = date.split("TO");
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date1 = format.parse(str[0]);
            return new SimpleDateFormat("MMMM dd,yyyy hh:mm aa").format(date1);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    String convertTime(String time) throws ParseException {
        Log.e("Time ", time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time);
        Date date2 = new Date();
        int milis = Math.abs(date2.getHours() - date1.getHours());
        return milis + " hr";
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img_event;
        TextView txt_eventName, txt_eventAdress, txt_eventDate, txt_time;
        RelativeLayout rtv_all;

        public Holder(View itemView) {
            super(itemView);
            rtv_all = (RelativeLayout) itemView.findViewById(R.id.rtv_all);
            img_event = (ImageView) itemView.findViewById(R.id.img_event);
            txt_eventName = (TextView) itemView.findViewById(R.id.txt_eventName);
            txt_eventAdress = (TextView) itemView.findViewById(R.id.txt_eventAdress);
            txt_eventDate = (TextView) itemView.findViewById(R.id.txt_eventDate);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            RelativeLayout.LayoutParams parameters = (RelativeLayout.LayoutParams) rtv_all.getLayoutParams();
            DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            parameters.height = ((HomeActivity.ActivityWidth) * 3 / 4);

        }
    }
}
