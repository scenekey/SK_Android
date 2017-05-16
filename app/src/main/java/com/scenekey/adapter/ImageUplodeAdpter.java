package com.scenekey.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.fragments.Image_uploade_fragment;
import com.scenekey.helper.Constants;
import com.scenekey.models.ImagesUplode;

import java.util.ArrayList;

/**
 * Created by mindiii-rahul on 1/5/17.
 */

public class ImageUplodeAdpter extends RecyclerView.Adapter<ImageUplodeAdpter.Holder> {


    ArrayList<ImagesUplode> list;
    HomeActivity activity;
    Image_uploade_fragment fragment;

    public ImageUplodeAdpter(HomeActivity activity, Image_uploade_fragment fragment) {
        list = new ArrayList<>();
        this.activity = activity;
        this.fragment = fragment;
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(activity).inflate(R.layout.gva2_image_picker, null));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (position == list.size()) {
            holder.img_cross.setVisibility(View.GONE);
            holder.img_pic.setImageBitmap(null);
            holder.img_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    fragment.startActivityForResult(galleryIntent, Constants.RESULT_LOAD);

                }
            });
        } else {
            holder.img_cross.setVisibility(View.VISIBLE);
            holder.img_pic.setImageBitmap(list.get(position).getBitmap());
            holder.img_cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeImage(position);
                }
            });
            holder.img_pic.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) return (list.size() + 1);
        return 1;
    }

    public void addImage(Bitmap bitmap) {
        //int size = Math.max(bitmap.getWidth(),bitmap.getHeight());
        //list.add(new ImagesUplode(Bitmap.createScaledBitmap(bitmap,size,size,false)));
        list.add(new ImagesUplode(cropToSquare(bitmap)));
        //notifyDataSetChanged();
        notifyItemInserted(list.size() - 1);

    }

    public void removeImage(int position) {
        list.remove(position);
        //notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img_cross, img_pic;

        public Holder(View itemView) {
            super(itemView);
            img_pic = (ImageView) itemView.findViewById(R.id.img_pic);
            img_cross = (ImageView) itemView.findViewById(R.id.img_cross);
            img_cross.setVisibility(View.GONE);


        }
    }

}
