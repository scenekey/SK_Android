package com.scenekey.model;

import com.scenekey.helper.WebServices;
import com.scenekey.util.Utility;

import java.io.Serializable;

import static com.scenekey.helper.WebServices.USER_IMAGE;

/**
 * Created by mindiii on 31/1/18.
 */

public class UserInfo implements Serializable {


    public String userID;
    public String email;
    public String password;
    public String fullName;
    public String userName;
    public String userGender;
    public String userImage;
    public String bio;
    public String loginTime;
    public String stageName;
    public String venuName;
    public String artistType;
    public String firstName;
    public String lastName;
    public String environment;
    public String facebookId;
    public String makeAdmin;
    public String userAccessToken;
    public boolean firstTimeDemo;
    public String latitude;
    public String longitude;
    public String address;
    public String keyPoints;
    public boolean currentLocation;

    public String getUserImage() {
        return (userImage.contains("https:")?userImage: WebServices.USER_IMAGE +userImage);
    }


}
