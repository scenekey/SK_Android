package com.scenekey.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.scenekey.R;
import com.scenekey.helper.SessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mindiii on 10/4/17.
 */

public class SplashActivity extends Activity {

    boolean isBackPressed;
    SessionManager sessionManager;
    ImageView img_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.a1_splash_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        img_icon.setAnimation(animation);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Splashtask splashtask;
        sessionManager = new SessionManager(this);
        splashtask = new Splashtask();
        splashtask.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBackPressed = true;
        finish();
    }

    public void getFacebookKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.scenekey", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    class Splashtask extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                sleep(3000);
                if (!isBackPressed) {

                    if (sessionManager.isLoggedIn()) {
                        /*Intent in=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity( in );
                        finish();*/
                        Intent in = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(in);
                        finish();
                    } else {
                        Intent in = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    }

                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
