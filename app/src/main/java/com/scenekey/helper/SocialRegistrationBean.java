package com.scenekey.helper;

import java.io.Serializable;

/**
 * Created by mindiii on 6/12/16.
 */
public class SocialRegistrationBean implements Serializable {
    private String fullname, socialId;
    private double latitude, longitude;
    private String SocialType, Image;
    private String gender;


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getSocialType() {
        return SocialType;
    }

    public void setSocialType(String socialType) {
        SocialType = socialType;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public String toString() {
        return "SocialRegistrationBean" +
                "fullname='" + fullname + '\n' +
                " socialId='" + socialId + '\n' +
                " latitude=" + latitude +
                " longitude=" + longitude +
                " SocialType='" + SocialType + '\n' +
                " Image='" + Image + '\n';
    }
}
