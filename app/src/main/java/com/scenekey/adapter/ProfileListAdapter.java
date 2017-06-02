package com.scenekey.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.helper.Constants;
import com.scenekey.models.Feeds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mindiii on 23/5/17.
 */

public class ProfileListAdapter extends BaseAdapter{

    ArrayList<Feeds> feedlist;


    public ProfileListAdapter(ArrayList<Feeds> feedlist) {
        this.feedlist = feedlist;
    }

    @Override
    public int getCount() {
        return feedlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            LayoutInflater inflater = (LayoutInflater) HomeActivity.instance
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.rclv_profile_events, parent, false);
            Holder holder = new Holder();
            holder.txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
            holder.txt_EE = (TextView) itemView.findViewById(R.id.txt_EE);
            holder.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            holder.txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            holder.txt_time.setVisibility(View.INVISIBLE);
            holder.txt_date.setVisibility(View.INVISIBLE);
            holder.img_event = (ImageView) itemView.findViewById(R.id.img_event);
            holder.rtlv_EE = (RelativeLayout) itemView.findViewById(R.id.rtlv_EE);

            final Feeds feeds = feedlist.get(position);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.img_event.getLayoutParams();
            layoutParams.height = HomeActivity.ActivityWidth * 3 / 4;
            holder.img_event.setLayoutParams(layoutParams);
            holder.txt_comment.setLayoutParams(layoutParams);

            if (position == 0) {
                holder.txt_EE.setText(feeds.getEvent_name());
                holder.rtlv_EE.setVisibility(View.VISIBLE);
                try {
                    holder.txt_date.setText(getDateInFormat(feeds.getDate()));
                    holder.txt_time.setText(getTimeInFormat(feeds.getDate()));
                }
                catch (Exception e){

                }
            }
            else if (feeds.getEvent_name().equals(feedlist.get(position - 1).getEvent_name())) {
                holder.rtlv_EE.setVisibility(View.GONE);
            } else {
                holder.rtlv_EE.setVisibility(View.VISIBLE);
                holder.txt_EE.setText(feeds.getEvent_name());
                try {
                    holder.txt_date.setText(getDateInFormat(feeds.getDate()));
                    holder.txt_time.setText(getTimeInFormat(feeds.getDate()));
                }
                catch (Exception e){

                }
            }
            if (feeds.getType().equals(Constants.FEED_TYPE_COMMENT)) {
                holder.img_event.setVisibility(View.GONE);
                holder.txt_comment.setVisibility(View.VISIBLE);
                holder.txt_comment.setText(feeds.getFeed());
            } else if (feeds.getType().equals(Constants.FEED_TYPE_PICTURE)) {
                holder.txt_comment.setVisibility(View.GONE);
                holder.img_event.setVisibility(View.VISIBLE);
                Picasso.with(HomeActivity.instance).load(WebService.BASE_IMAGE_URL + feeds.getFeed()).placeholder(R.drawable.def_scene).into(holder.img_event);

            }

            Font font = new Font(HomeActivity.instance);
            font.setFontFrank_Heavy_Reg(holder.txt_comment);
            font.setFontLibreFranklin_SemiBold(holder.txt_EE);
            itemView.setTag(holder);
        }
        else {
            Holder holder  = (Holder) itemView.getTag();
            final Feeds feeds = feedlist.get(position);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.img_event.getLayoutParams();
            layoutParams.height = HomeActivity.ActivityWidth * 3 / 4;
            holder.img_event.setLayoutParams(layoutParams);
            holder.txt_comment.setLayoutParams(layoutParams);

            if (position == 0) {
                holder.txt_EE.setText(feeds.getEvent_name());
                holder.rtlv_EE.setVisibility(View.VISIBLE);
                try {
                    holder.txt_date.setText(getDateInFormat(feeds.getDate()));
                    holder.txt_time.setText(getTimeInFormat(feeds.getDate()));
                }
                catch (Exception e){

                }

            }
            else if (feeds.getEvent_name().equals(feedlist.get(position - 1).getEvent_name())) {
                holder.rtlv_EE.setVisibility(View.GONE);
            } else {
                holder.rtlv_EE.setVisibility(View.VISIBLE);
                holder.txt_EE.setText(feeds.getEvent_name());
                try {
                    holder.txt_date.setText(getDateInFormat(feeds.getDate()));
                    holder.txt_time.setText(getTimeInFormat(feeds.getDate()));
                }
                catch (Exception e){

                }
            }
            if (feeds.getType().equals(Constants.FEED_TYPE_COMMENT)) {
                holder.img_event.setVisibility(View.GONE);
                holder.txt_comment.setVisibility(View.VISIBLE);
                holder.txt_comment.setText(feeds.getFeed());
            } else if (feeds.getType().equals(Constants.FEED_TYPE_PICTURE)) {
                holder.txt_comment.setVisibility(View.GONE);
                holder.img_event.setVisibility(View.VISIBLE);
                Picasso.with(HomeActivity.instance).load(WebService.BASE_IMAGE_URL + feeds.getFeed()).placeholder(R.drawable.scene2).into(holder.img_event);

            }
            Font font = new Font(HomeActivity.instance);
            font.setFontFrank_Heavy_Reg(holder.txt_comment);
            font.setFontLibreFranklin_SemiBold(holder.txt_EE);
        }
        return itemView;
    }

    public class Holder {
       public TextView txt_comment, txt_EE;
       public TextView txt_date, txt_time;
       public ImageView img_event;
       public RelativeLayout rtlv_EE;
    }

    /**
     *
     * @param date date should be String like 2017-04-08 13:02:54
     * @return
     */
    String getDateInFormat(String date){
        String dateS = date.split(" ")[0];
        String dateArray[] = dateS.split("-");

        return dateArray[2]+"/"+dateArray[1]+"/"+dateArray[0];
    }


    String getTimeInFormat(String date) {
        String dateS = date.split(" ")[1];
        String dateArray[] = dateS.split(":");
        if (Integer.parseInt(dateArray[0]) > 12) {
            int hour = Integer.parseInt(dateArray[0]) - 12;
            return hour + ":" + dateArray[1] + " pm";
        }
        return dateArray[0] + ":" + dateArray[1] + " am ";
    }



}
