package com.scenekey.lib_sources.SwipeCard;

import android.graphics.Bitmap;

import com.scenekey.helper.WebServices;

public class Card {
    public String name;
    public int imageId;
    public String text;
    public String imageUrl;
    public String userImage;
    public int imageint;
    public String date;
    public Bitmap bitmap;

    public String getUserImage() {
        return (userImage.contains("https:")?userImage: WebServices.USER_IMAGE +userImage);
    }
}
