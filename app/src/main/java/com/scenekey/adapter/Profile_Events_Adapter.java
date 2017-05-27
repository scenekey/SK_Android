package com.scenekey.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.Font;
import com.scenekey.activity.HomeActivity;
import com.scenekey.helper.Constants;
import com.scenekey.models.Feeds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mindiii on 19/5/17.
 */

public class Profile_Events_Adapter extends RecyclerView.Adapter<Profile_Events_Adapter.Holder> {

    private static String TAG = Profile_Events_Adapter.class.toString();
    HomeActivity activity;
    ArrayList<Feeds> feedlist;
    int size;
    int pageToshow = 1;

    public Profile_Events_Adapter(HomeActivity activity, ArrayList<Feeds> feedlist ) {
        this.activity = activity;
        this.feedlist = feedlist;
        size =  10;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(activity).inflate(R.layout.rclv_profile_events, null));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Feeds feeds = feedlist.get(position);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.img_event.getLayoutParams();
        layoutParams.height = activity.ActivityWidth * 3 / 4;
        holder.img_event.setLayoutParams(layoutParams);
        holder.txt_comment.setLayoutParams(layoutParams);

        if (position == 0) holder.txt_EE.setText(feeds.getEvent_name());
        else if (feeds.getEvent_name().equals(feedlist.get(position - 1).getEvent_name())) {
            holder.txt_EE.setVisibility(View.GONE);
        } else {
            holder.txt_EE.setVisibility(View.VISIBLE);
            holder.txt_EE.setText(feeds.getEvent_name());
        }
        if (feeds.getType().equals(Constants.FEED_TYPE_COMMENT)) {
            holder.img_event.setVisibility(View.GONE);
            holder.txt_comment.setVisibility(View.VISIBLE);
            holder.txt_comment.setText(feeds.getFeed());
        } else if (feeds.getType().equals(Constants.FEED_TYPE_PICTURE)) {
            holder.txt_comment.setVisibility(View.GONE);
            holder.img_event.setVisibility(View.VISIBLE);
            Log.e(TAG, feeds.getFeed() + " ");
            Picasso.with(activity).load("http://mindiii.com/scenekeyNew/scenekey/" + feeds.getFeed()).placeholder(R.drawable.def_scene).into(holder.img_event);

        }
        /*if(position==feedlist.size()-1){
            activity.dismissProgDailog();
        }*/

    }

    @Override
    public int getItemCount() {

        if (feedlist != null) return feedlist.size();
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView txt_comment, txt_EE;
        ImageView img_event;

        public Holder(View itemView) {
            super(itemView);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
            txt_EE = (TextView) itemView.findViewById(R.id.txt_EE);
            img_event = (ImageView) itemView.findViewById(R.id.img_event);

            Font font = new Font(activity);
            font.setFontFrank_Heavy_Reg(txt_comment);
            font.setFontLibreFranklin_SemiBold(txt_EE);
        }
    }

    public int getPageToshow() {
        return pageToshow;
    }

    public void setPageToshow(int pageToshow) {
        this.pageToshow = pageToshow;
    }
}
