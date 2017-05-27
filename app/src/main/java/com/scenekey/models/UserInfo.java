package com.scenekey.models;

import com.facebook.AccessToken;

import java.io.Serializable;

/**
 * Created by mindiii on 20/4/17.
 */

public class UserInfo implements Serializable {
    private String userID;
    private String email;
    private String password;
    private String fullname;
    private String userName;
    private String userGender;
    private String userImage;
    private String loginTime;
    private String stagename;
    private String venuName;
    private String artisttype;
    private String firstname;
    private String lastname;
    private String facebookId;
    private String makeAdmin;
    private AccessToken userAccessToken;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getStagename() {
        return stagename;
    }

    public void setStagename(String stagename) {
        this.stagename = stagename;
    }

    public String getArtisttype() {
        return artisttype;
    }

    public void setArtisttype(String artisttype) {
        this.artisttype = artisttype;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getVenuName() {
        return venuName;
    }

    public void setVenuName(String venuName) {
        this.venuName = venuName;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getMakeAdmin() {
        return makeAdmin;
    }

    public void setMakeAdmin(String makeAdmin) {
        this.makeAdmin = makeAdmin;
    }

    public AccessToken getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(AccessToken userAccessToken) {
        this.userAccessToken = userAccessToken;
    }
}
