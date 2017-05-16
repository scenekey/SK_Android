package com.scenekey.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragments.VenuSearch_Fragment;
import com.scenekey.models.Venue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mindiii-rahul on 1/5/17.
 */

public class VenuAdapter extends RecyclerView.Adapter<VenuAdapter.ViewHolder> {
    HomeActivity activity;
    ArrayList<Venue> venuelist;
    VenuSearch_Fragment fragment;

    public VenuAdapter(HomeActivity activity, ArrayList<Venue> venuelist, VenuSearch_Fragment venuSearch_fragment) {
        this.activity = activity;
        this.venuelist = venuelist;
        this.fragment = venuSearch_fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rclv_venue, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Venue venue = venuelist.get(position);
        Picasso.with(activity).invalidate(venue.getImage());
        Picasso.with(activity).load(venue.getImage()).into(holder.img_venu);
        holder.txt_venu_name.setText(venue.getVenue_name());
        holder.txt_venu_address.setText(venue.getCity() + " " + venue.getState());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onrecylcerViewItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (venuelist != null) return venuelist.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_venu;
        TextView txt_venu_name, txt_venu_address;

        public ViewHolder(View itemView) {
            super(itemView);
            img_venu = (ImageView) itemView.findViewById(R.id.img_venu);
            txt_venu_name = (TextView) itemView.findViewById(R.id.txt_venu_name);
            txt_venu_address = (TextView) itemView.findViewById(R.id.txt_venu_address);
        }
    }
}
