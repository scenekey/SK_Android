package com.scenekey.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.scenekey.activity.LoginActivity;
import com.scenekey.models.UserInfo;

/**
 * Created by mindiii on 10/4/17.
 */

public class SessionManager {

    private static final String PREF_NAME = "Scenekey";
    private static final String IS_LOGGEDIN = "isLoggedIn";
    SharedPreferences mypref;
    SharedPreferences.Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        mypref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = mypref.edit();
        editor.apply();
    }

    public void createSession(UserInfo userInfo) {
        editor.putString(Constants.USER_ID, userInfo.getUserID());
        editor.putString(Constants.EMAIL, userInfo.getEmail());
        editor.putString(Constants.FULLNAME, userInfo.getFullname());
        editor.putString(Constants.USERNAME, userInfo.getUserName());
        editor.putString(Constants.USERGENDER, userInfo.getUserGender());
        editor.putString(Constants.USERIMAGE, userInfo.getUserImage());
        editor.putString(Constants.USERLOGINTIME, userInfo.getLoginTime());
        editor.putString(Constants.STAGE_NAME, userInfo.getStagename());
        editor.putString(Constants.VENUE_NAME, userInfo.getVenuName());
        editor.putString(Constants.ARTIST_TYPE, userInfo.getArtisttype());
        editor.putString(Constants.FIRST_NAME, userInfo.getFirstname());
        editor.putString(Constants.LAST_NAME, userInfo.getLastname());
        editor.putString(Constants.FACEBOOK_ID, userInfo.getLastname());
        editor.putBoolean(IS_LOGGEDIN, true);
        editor.commit();
    }


    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserID(mypref.getString(Constants.USER_ID, null));
        userInfo.setEmail(mypref.getString(Constants.EMAIL, null));
        userInfo.setFullname(mypref.getString(Constants.FULLNAME, null));
        userInfo.setUserName(mypref.getString(Constants.USERNAME, null));
        userInfo.setUserGender(mypref.getString(Constants.USERGENDER, null));
        userInfo.setUserImage(mypref.getString(Constants.USERIMAGE, null));
        userInfo.setLoginTime(mypref.getString(Constants.USERLOGINTIME, null));
        userInfo.setStagename(mypref.getString(Constants.STAGE_NAME, null));
        userInfo.setVenuName(mypref.getString(Constants.VENUE_NAME, null));
        userInfo.setArtisttype(mypref.getString(Constants.ARTIST_TYPE, null));
        userInfo.setFirstname(mypref.getString(Constants.FIRST_NAME, null));
        userInfo.setLastname(mypref.getString(Constants.LAST_NAME, null));
        userInfo.setLastname(mypref.getString(Constants.FACEBOOK_ID, null));
        return userInfo;
    }

    public boolean isLoggedIn() {
        return mypref.getBoolean(IS_LOGGEDIN, false);
    }

    public void logout(Activity activity) {
        editor.clear();
        editor.commit();
        /*Intent intent = new Intent();
        activity.startActivity(intent);
        activity.finish();*/
        /*Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(homeIntent);
        activity.finish();*/

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
