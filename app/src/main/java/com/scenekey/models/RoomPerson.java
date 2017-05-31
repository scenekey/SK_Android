package com.scenekey.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class RoomPerson implements Serializable {

    private String android_version_name;
    private String android_image_url;
    private String staus;
    private int resorceId;

    public RoomPerson(String android_version_name, String android_image_url, String staus) {
        this.android_version_name = android_version_name;
        this.android_image_url = android_image_url;
        this.staus = staus;
    }

    public RoomPerson() {
    }

    public String getAndroid_version_name() {
        return android_version_name;
    }

    public void setAndroid_version_name(String android_version_name) {
        this.android_version_name = android_version_name;
    }

    public String getAndroid_image_url() {
        return android_image_url;
    }

    public void setAndroid_image_url(String android_image_url) {
        this.android_image_url = android_image_url;
    }

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    /*public Bitmap getResorceId() {
        return resorceId;
    }

    public void setResorceId(Bitmap resorceId) {
        this.resorceId = resorceId;
    }*/
}