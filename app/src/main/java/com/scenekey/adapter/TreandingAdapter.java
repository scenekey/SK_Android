package com.scenekey.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.ImageUtil;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragments.Event_Fragment;
import com.scenekey.models.Event;
import com.scenekey.models.Events;
import com.scenekey.models.Venue;
import com.squareup.picasso.MemoryPolicy;
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
    int heightA,widthA;


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
        try {

            Bitmap bitmap = ImageUtil.getBitmapByUrl(event.getImage());
            holder.img_event.setImageBitmap(bitmap);
        } catch (Exception e) {
            Picasso.with(activity).load(event.getImage()).resize(widthA,heightA).into(holder.img_event);
            e.printStackTrace();
        }

            if (object.isOngoing) {
                holder.hour.setVisibility(View.GONE);
                holder.like.setVisibility(View.VISIBLE);
                try{
                    if(Integer.parseInt(event.getRating())==0)holder.txt_like.setText("--");
                    else holder.txt_like.setText(event.getRating());
                }
                catch (Exception e){

                }
              /*  if(event.getRating()!=null)holder.txt_like.setText(event.getRating());
                if(event.getRating()==null)holder.txt_like.setText("--");*/
            }
            else {
                holder.hour.setVisibility(View.VISIBLE);
                holder.like.setVisibility(View.GONE);
                holder.txt_time.setText(object.remainingTime);
            }


        holder.txt_eventName.setText(event.getEvent_name());
        holder.txt_eventAdress.setText(venue.getAddress()+" "+ activity.getDistanceMile(new Double[]{Double.valueOf(venue.getLatitude()), Double.valueOf(venue.getLongitude()), Double.valueOf(activity.getlatlong()[0]), Double.valueOf(activity.getlatlong()[1])})+" M");
        holder.txt_eventDate.setText(object.timeFormat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                if (!clicked) {
                    Event_Fragment frg = new Event_Fragment();
                    frg.setData(event.getEvent_id(),venue.getVenue_name());
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
        String[] str;
        str = date.split("TO");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date1 = format.parse(str[0]);
            return new SimpleDateFormat("MMMM dd,yyyy hh:mm aa").format(date1);

        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

    }

    String convertTime(String time) throws ParseException {
        //Log.e("Time ", time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time);
        Date date2 = new Date();
        int milis = Math.abs(date2.getHours() - date1.getHours());
        return milis + " hr";
    }

    /**
     * @param date date of the event check format before use tie
     * @return
     * @throws ParseException
     */
    public boolean checkWithTime(final String date , int interval) throws ParseException {
        String[] dateSplit = (date.replace("TO", "T")).replace(" ", "T").split("T");
        Date startTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateSplit[0] + " " + dateSplit[1]);
        Date endTime = new Date(startTime.getTime()+(interval* 60 * 60 * 1000));
        Log.e("TrendingAdapter ",startTime +"  : "+endTime);
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
        if (currentTime < endTime.getTime() && currentTime > startTime.getTime()) {
            return true;
        }
        return false;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img_event;
        TextView txt_eventName, txt_eventAdress, txt_eventDate, txt_time ,txt_like;
        RelativeLayout rtv_all;
        LinearLayout like,hour;

        public Holder(View itemView) {
            super(itemView);
            rtv_all = (RelativeLayout) itemView.findViewById(R.id.rtv_all);
            img_event = (ImageView) itemView.findViewById(R.id.img_event);
            txt_eventName = (TextView) itemView.findViewById(R.id.txt_eventName);
            txt_eventAdress = (TextView) itemView.findViewById(R.id.txt_eventAdress);
            txt_eventDate = (TextView) itemView.findViewById(R.id.txt_eventDate);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_like = (TextView) itemView.findViewById(R.id.txt_like);
            like = (LinearLayout) itemView.findViewById(R.id.like);
            hour = (LinearLayout) itemView.findViewById(R.id.hour);
            RelativeLayout.LayoutParams parameters = (RelativeLayout.LayoutParams) rtv_all.getLayoutParams();
            DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            heightA = parameters.height = ((HomeActivity.ActivityWidth) * 3 / 4);
            widthA = HomeActivity.ActivityWidth;
            Font font = new Font(activity);
            font.setFontFrankBookReg(txt_eventAdress, txt_eventDate, txt_eventName, txt_time);
            txt_time.setTypeface(font.FrankBookRegular());

        }
    }


    /*public int phpDistance(Double[] LL , String distance) {
        Log.e("LAT LONG ", LL[0]+" "+LL[1]+" "+LL[2]+" "+LL[3] +" Distance Given "+distance);
        return (int)(6371000 * (Math.acos(Math.cos(Math.toRadians(LL[0])) * Math.cos(Math.toRadians(LL[2])) * Math.cos(Math.toRadians(LL[3]) - Math.toRadians(LL[1])) + Math.sin(Math.toRadians(LL[0])) * Math.sin(Math.toRadians(LL[2])))));
    }*/
}
