package com.scenekey.models;

import java.io.Serializable;

/**
 * Created by mindiii on 25/4/17.
 */

public class NotificationData implements Serializable {

    int img;

    String nudges;
    String user_id;
    String facebook_id;
    String username;
    String userimage;

    boolean message;

    public NotificationData(int img, String nudges) {
        this.img = img;
        this.nudges = nudges;
    }

    public NotificationData() {

    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getNudges() {
        return nudges;
    }

    public void setNudges(String nudges) {
        this.nudges = nudges;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }
}
