package com.scenekey.model;

/**
 * Created by mindiii on 28/2/18.
 */

public class NotificationData {

    public int img;

    public String nudges;
    public String user_id;
    public String facebook_id;
    public String username;
    public String userimage;

    public boolean message;

    public NotificationData(){

    }

    public NotificationData(int img, String nudges ) {
        this.img = img;
        this.nudges = nudges;
    }

    public NotificationData(int img, String nudges ,String username ) {
        this.img = img;
        this.nudges = nudges;
        this.username = username;
    }
}
