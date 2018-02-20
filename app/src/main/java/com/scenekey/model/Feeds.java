package com.scenekey.model;

import java.io.Serializable;

/**
 * Created by mindiii on 15/2/18.
 */

public class Feeds implements Serializable {

    public String username;
    public String userid;
    public String userFacebookId;
    public String event_id;
    public String ratetype;
    public String userimage;
    public String type;
    public String location;
    public String date;
    public String feed;

    /**
     * In list of user attended event
     */
    public String event_name;
}
