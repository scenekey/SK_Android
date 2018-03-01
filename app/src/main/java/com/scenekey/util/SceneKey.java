package com.scenekey.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.scenekey.helper.SessionManager;
import com.crashlytics.android.Crashlytics;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mindiii on 2/2/18.
 */

public class SceneKey extends Application {

    public static SceneKey instance = null;
    public static SessionManager sessionManager;
    // public static final String TAG = Impress.class.getSimpleName();

    public static synchronized SceneKey getInstance() {
        if (instance != null) {
            return instance;
        }
        return new SceneKey();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        sessionManager = new SessionManager(instance.getApplicationContext());
        EmojiManager.install(new IosEmojiProvider());
    }



}