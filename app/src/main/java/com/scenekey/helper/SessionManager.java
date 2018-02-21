package com.scenekey.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.scenekey.activity.LoginActivity;
import com.scenekey.model.UserInfo;
import com.scenekey.util.SceneKey;


/*
 * Created by mindiii on 8/12/17.
 */

public class SessionManager {
    private Context context;

    private static SessionManager instance = null;

    private final String PREF_NAME2 = "Scenekey2";
    private final String PREF_TUTO = "Scenekey_Tuto";
    private final String IS_LOGGEDIN2 = "isLoggedIn2";

    public final String USER_ID = "USER_ID";
    public final String EMAIL = "EMAIL";
    public final String FULLNAME = "FULLNAME";
    public final String USERNAME = "USERNAME";
    public final String BIO = "BIO";
    public final String USERGENDER = "USERGENDER";
    public final String USERIMAGE = "USERIMAGE";
    public  final String USERLOGINTIME = "USERLOGINTIME";
    public final String STAGE_NAME = "STAGE_NAME";
    public  final String VENUE_NAME = "VENUE_NAME";
    public  final String ARTIST_TYPE = "ARTIST_TYPE";
    public  final String FIRST_NAME = "FIRST_NAME";
    public  final String LAST_NAME = "LAST_NAME";
    public  final String ENV = "ENV";
    public  final String FACEBOOK_ID = "FACEBOOK_ID";
    public  final String TRYDEMO_FIRST = "TRYDEMO_FIRST";
    public  final String ACCESSTOEKN = "ACCESSTOEKN";
    public  final String MAKE_ADMIN = "MAKE_ADMIN";
    public  final String LATITUDE = "LATITUDE";
    public  final String LONGITUDE = "LONGITUDE";
    public  final String ADDDRESS = "ADDDRESS";
    public  final String KEY_PONTS = "key_points";
    public  final String CURRENT_LOCATION = "current_location";
    public  final String TUTORIAL = "tutorial";
    public  final String ADMIN_NO = "no";

    public  final String SOFT_KEY = "SOFT_KEY";

    private SharedPreferences mypref;
    private SharedPreferences tuto_pref;

    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor2;


    public SessionManager(Context context) {
        this.context = context;
        mypref = this.context.getSharedPreferences(PREF_NAME2, Context.MODE_PRIVATE);
        tuto_pref = this.context.getSharedPreferences(PREF_TUTO, Context.MODE_PRIVATE);
        editor = mypref.edit();
        editor2 = tuto_pref.edit();
        editor.apply();
        editor2.apply();
    }

    public static SessionManager getInstance() {
        if ((instance != null)) {
            return instance;
        }
        instance = new SessionManager(SceneKey.getInstance().getApplicationContext());
        return instance;
    }

    public void canTutorial(boolean tutorial){
        editor2.putBoolean(TUTORIAL,tutorial);
        editor2.commit();
    }
    public boolean showTutorial(){
        return tuto_pref.getBoolean(TUTORIAL,true);
    }

    public void createSession(UserInfo userInfo) {
        editor.putString(USER_ID, userInfo.userID);
        editor.putString(EMAIL, userInfo.email);
        editor.putString(FULLNAME, userInfo.fullName);
        editor.putString(USERNAME, userInfo.userName);
        editor.putString(USERGENDER, userInfo.userGender);
        editor.putString(USERIMAGE, userInfo.userImage);
        editor.putString(USERLOGINTIME, userInfo.loginTime);
        editor.putString(STAGE_NAME, userInfo.stageName);
        editor.putString(VENUE_NAME, userInfo.venuName);
        editor.putString(ARTIST_TYPE, userInfo.artistType);
        editor.putString(FIRST_NAME, userInfo.firstName);
        editor.putString(LAST_NAME, userInfo.lastName);
        editor.putString(ENV, userInfo.environment);
        editor.putString(FACEBOOK_ID, userInfo.facebookId);
        editor.putBoolean(TRYDEMO_FIRST, userInfo.firstTimeDemo);
        editor.putString(ACCESSTOEKN, userInfo.userAccessToken);
        editor.putString(MAKE_ADMIN, userInfo.makeAdmin);
        editor.putString(LATITUDE, userInfo.latitude);
        editor.putString(LONGITUDE, userInfo.longitude);
        editor.putString(ADDDRESS, userInfo.address);
        editor.putString(KEY_PONTS, userInfo.keyPoints);
        editor.putString(BIO, userInfo.bio);
        editor.putBoolean(CURRENT_LOCATION, userInfo.currentLocation);
        editor.putBoolean(IS_LOGGEDIN2, true);
        editor.commit();
    }

    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.userID=(mypref.getString(USER_ID, null));
        userInfo.email=(mypref.getString(EMAIL, null));
        userInfo.fullName =(mypref.getString(FULLNAME, null));
        userInfo.userName=(mypref.getString(USERNAME, null));
        userInfo.userGender=(mypref.getString(USERGENDER, null));
        userInfo.userImage=(mypref.getString(USERIMAGE, null));
        userInfo.loginTime=(mypref.getString(USERLOGINTIME, null));
        userInfo.stageName =(mypref.getString(STAGE_NAME, null));
        userInfo.venuName=(mypref.getString(VENUE_NAME, null));
        userInfo.artistType =(mypref.getString(ARTIST_TYPE, null));
        userInfo.firstName =(mypref.getString(FIRST_NAME, null));
        userInfo.lastName =(mypref.getString(LAST_NAME, null));
        userInfo.environment =(mypref.getString(ENV, null));
        userInfo.facebookId=(mypref.getString(FACEBOOK_ID, null));
        userInfo.firstTimeDemo=(mypref.getBoolean(TRYDEMO_FIRST, false));
        userInfo.userAccessToken=(mypref.getString(ACCESSTOEKN, null));
        userInfo.makeAdmin=(mypref.getString(MAKE_ADMIN, ADMIN_NO));
        userInfo.latitude=(mypref.getString(LATITUDE, null));
        userInfo.longitude=(mypref.getString(LONGITUDE, null));
        userInfo.address=(mypref.getString(ADDDRESS, ""));
        userInfo.keyPoints=(mypref.getString(KEY_PONTS, "0"));
        userInfo.bio=(mypref.getString(BIO, ""));
        userInfo.currentLocation=(mypref.getBoolean(CURRENT_LOCATION, false));
        return userInfo;
    }

    public boolean isLoggedIn() {
        return mypref.getBoolean(IS_LOGGEDIN2, false);
    }

    public void logout(Activity activity) {
        editor.putBoolean(IS_LOGGEDIN2, false);
        editor.clear();
        editor.commit();

        /*Intent intent = new Intent();
        activity.startActivity(intent);
        activity.finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(homeIntent);
        activity.finish();*/

        Intent intent = new Intent(activity, LoginActivity.class);
       // LoginActivity.CALLBACK =0;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();

    }

    public boolean isSoftKey(){
        return mypref.getBoolean(SOFT_KEY, false);
    }

    public void setSoftKey(Boolean value){
        editor.putBoolean(SOFT_KEY, value);
        editor.commit();
    }

    public void setLogin(boolean value){
        editor.putBoolean(IS_LOGGEDIN2, value);
        editor.commit();
    }

    public String getFacebookId(){
        String s = mypref.getString(FACEBOOK_ID,"");
        if (s.equals("")){
            s = mypref.getString(USER_ID,"");
        }
        return s;}

}
