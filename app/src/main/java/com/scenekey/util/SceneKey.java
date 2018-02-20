package com.scenekey.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.scenekey.helper.SessionManager;

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
        instance = this;
        sessionManager = new SessionManager(instance.getApplicationContext());
    }


}