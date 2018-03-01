package com.scenekey.adapter;

import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragment.Event_Fragment;
import com.scenekey.fragment.Home_No_Event_Fragment;
import com.scenekey.helper.SortByPoint;
import com.scenekey.model.Event;
import com.scenekey.model.Events;
import com.scenekey.model.Venue;
import com.scenekey.util.RoundedTransformation;
import com.scenekey.util.SceneKey;
import com.scenekey.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mindiii on 12/2/18.
 */

public class Trending_Adapter extends RecyclerView.Adapter<Trending_Adapter.ViewHolder> {

    private HomeActivity activity;
    private ArrayList<Events> eventsArrayList;
    private String[] currentLatLng;
    private boolean clicked;

    private Events object;
    private  Venue venue;
    private  Event event;
    //  private int heightA,widthA;

    public Trending_Adapter(HomeActivity activity, ArrayList<Events> eventsArrayList,String[] currentLatLng) {
        this.activity = activity;
        this.eventsArrayList = eventsArrayList;
        this.currentLatLng = currentLatLng;
    }

    @Override
    public Trending_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_custom_trending, parent, false);

        return new Trending_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Trending_Adapter.ViewHolder holder, int position) {

        object = eventsArrayList.get(position);
        venue = object.getVenue();
        event = object.getEvent();

        Collections.sort(eventsArrayList, new SortByPoint());
        try {
            if(position == eventsArrayList.size()-1){
                holder.txt_gap.setVisibility(View.VISIBLE);
                if(SceneKey.sessionManager.isSoftKey()){
                    holder.txt_gap2.setVisibility(View.VISIBLE);
                }
            }
            else {
                holder.txt_gap.setVisibility(View.GONE);
                holder.txt_gap2.setVisibility(View.GONE);
            }
          /*  Bitmap bitmap = ImageUtil.getBitmapByUrl(event.getImage());
            holder.img_event.setImageBitmap(bitmap);*/
            int radius = (int) activity.getResources().getDimension(R.dimen.trending_round);
            //  Picasso.with(activity).load(event.getImage().contains("defaultevent.jpg")?venue.getImage():event.getImage()).resize(widthA,heightA).transform(new RoundedTransformation(radius,0)).placeholder(R.drawable.transparent).into(holder.img_event);//TODO chnage in all if work fine
            Picasso.with(activity).load(event.getImage().contains("defaultevent.jpg")?venue.getImage():event.getImage()).placeholder(R.drawable.transparent).into(holder.img_event);//TODO chnage in all if work fine
        } catch (Exception e) {
            int radius = (int) activity.getResources().getDimension(R.dimen.trending_round);
            // Picasso.with(activity).load(event.getImage().contains("defaultevent.jpg")?venue.getImage():event.getImage()).resize(widthA,heightA).transform(new RoundedTransformation(radius,0)).placeholder(R.drawable.transparent).into(holder.img_event);
            Picasso.with(activity).load(event.getImage().contains("defaultevent.jpg")?venue.getImage():event.getImage()).placeholder(R.drawable.transparent).into(holder.img_event);
            e.printStackTrace();
        }

        if (object.isOngoing) {
            holder.hour.setVisibility(View.GONE);
            holder.like.setVisibility(View.VISIBLE);
            holder.txt_like.setTextColor(activity.getResources().getColor(R.color.black2));
            holder.heart.setImageResource(R.drawable.ico_like);
            try{
                if(Integer.parseInt(event.rating)==0)holder.txt_like.setText("--");
                else {
                    holder.txt_like.setText(event.rating);
                    holder.txt_like.setTextColor(activity.getResources().getColor(R.color.old_primary));
                    holder.heart.setImageResource(R.drawable.ic_favorite_heart);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else {
            holder.hour.setVisibility(View.VISIBLE);
            holder.like.setVisibility(View.GONE);
            holder.txt_time.setText(object.remainingTime);
        }

        holder.txt_eventName.setText(event.event_name);
        holder.txt_eventAdress.setText((venue.getVenue_name().trim().length()>29?venue.getVenue_name().trim().substring(0,29):venue.getVenue_name().trim()));

        //distance bw event and user
        String miles= String.valueOf(activity.getDistanceMile(new Double[]{Double.valueOf(venue.getLatitude()), Double.valueOf(venue.getLongitude()), Double.valueOf(currentLatLng[0]), Double.valueOf(currentLatLng[1])}));
        Utility.e("Miles---",miles);
        holder.txt_eventmile.setText(miles+" M");
        //holder.txt_eventAdress.setText(venue.getAddress().trim()+" "+ activity.getDistanceMile(new Double[]{Double.valueOf(venue.getLatitude()), Double.valueOf(venue.getLongitude()), Double.valueOf(activity.getlatlong()[0]), Double.valueOf(activity.getlatlong()[1])})+" M");
        //holder.txt_eventAdress.setText(venue.getAddress());
        holder.txt_eventDate.setText(object.timeFormat);

    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img_event,heart;
        private TextView txt_eventName, txt_eventAdress, txt_eventDate, txt_time ,txt_like,txt_gap,txt_gap2 ,txt_eventmile;
        private RelativeLayout rl_main;
        private LinearLayout like,hour;

        ViewHolder(View view) {
            super(view);

            rl_main =  view.findViewById(R.id.rl_main);
            img_event =  view.findViewById(R.id.img_event);
            txt_eventName =  view.findViewById(R.id.txt_eventName);
            txt_eventAdress =  view.findViewById(R.id.txt_eventAdress);
            txt_eventDate =  view.findViewById(R.id.txt_eventDate);
            txt_gap =  view.findViewById(R.id.txt_gap);
            txt_gap2 =  view.findViewById(R.id.txt_gap2);
            txt_eventmile =  view.findViewById(R.id.txt_eventmile);
            txt_time =  view.findViewById(R.id.txt_time);
            txt_like =  view.findViewById(R.id.txt_like);
            heart =  view.findViewById(R.id.heart);
            like =  view.findViewById(R.id.like);
            hour =  view.findViewById(R.id.hour);

           /* CardView.LayoutParams parameters = (CardView.LayoutParams) rl_main.getLayoutParams();
            DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;

            heightA  = parameters.height = ((HomeActivity.ActivityWidth) * 3 / 4);
            widthA = HomeActivity.ActivityWidth;*/

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(final View v) {
            //check
            v.setClickable(false);
            if (!clicked) {
                try {
                    Event_Fragment fragment = new Event_Fragment();
                    fragment.setData(event.event_id,venue.getVenue_name(),object,currentLatLng);
                    activity.addFragment(fragment, 0);
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                clicked = true;
            }
            try {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                        clicked = false;
                    }
                }, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}