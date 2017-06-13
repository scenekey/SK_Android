package com.scenekey.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.scenekey.R;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.ImageUtil;
import com.scenekey.Utility.WebService;
import com.scenekey.models.Events;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mindiii on 27/4/17.
 */

public class MapInfoAdapter implements GoogleMap.InfoWindowAdapter {
    Activity activity;
    ArrayList<Events> eventArrayList;
    private View myContentsView;

    public MapInfoAdapter(Activity activity, ArrayList<Events> eventArrayList) {
        this.activity = activity;
        this.eventArrayList = eventArrayList;
        for (Events events : eventArrayList) {
            Picasso.with(activity).load(events.getEvent().getImage()).into(new ImageView(activity));
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        try {
            int position = Integer.parseInt(marker.getId().replace("m", ""));
        final Events events = eventArrayList.get(position);
        myContentsView = activity.getLayoutInflater().inflate(R.layout.z_custom_map_info, null);
        ImageView img_event = (ImageView) myContentsView.findViewById(R.id.img_event);
        int radius = (int) activity.getResources().getDimension(R.dimen._8sdp);

        try {

            Bitmap bitmap = ImageUtil.getBitmapByUrl(events.getEvent().getImage());

            //bitmap = (new RoundedTransformation(radius,1).transform(bitmap));
            img_event.setImageBitmap(bitmap);
        } catch (Exception e) {
            Picasso.with(activity).load(events.getEvent().getImage()).placeholder(R.drawable.scene1).into(img_event);
            e.printStackTrace();
        }


        // Picasso.with(activity).load(events.getEvent().getImage()).transform(new RoundedTransformation(radius,1)).into(img_event);
        Log.e("map", events.getEvent().getEvent_name() + " : " + events.getEvent().getImage());
        TextView txt_name = ((TextView) myContentsView.findViewById(R.id.txt_name));
        TextView txt_time = ((TextView) myContentsView.findViewById(R.id.txt_time));
        TextView txt_distance = ((TextView) myContentsView.findViewById(R.id.txt_distance));
        ImageView img_clock = ((ImageView) myContentsView.findViewById(R.id.img_clock));
        txt_name.setText(events.getEvent().getEvent_name());
        Font font = new Font(activity);
        txt_distance.setText(events.getEvent().getDistance());
        try {
            if(events.isOngoing){
                img_clock.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_heart));
                if(Integer.parseInt(events.getEvent().getRating())==0)txt_time.setText("--");
                else txt_time.setText(events.getEvent().getRating());
            }
            else txt_time.setText(convertTime(events.getEvent().getEvent_time()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        font.setFontFrankBookReg(txt_name, txt_time, txt_distance);
        /*img_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicked","map iNFO");
                Event_Fragment frg = new Event_Fragment();
                frg.setData(events.getEvent().getEvent_id());
                ((HomeActivity) activity).addFragment(frg);
            }
        });*/
            return myContentsView;
        } catch (Exception e) {

            return null;
        }

    }

    String convertTime(String time) throws ParseException {
        Log.e("Time ", time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time);
        Date date2 = new Date();
        int milis = Math.abs(date2.getHours() - date1.getHours());
        if(milis==1){
            milis = 60 - Math.abs(date2.getMinutes() - date1.getMinutes());
            return milis + " min";
        }
        return milis + " hr";
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    public ArrayList<Events> getEventArrayList() {
        return eventArrayList;
    }

    public void setEventArrayList(ArrayList<Events> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }
}
