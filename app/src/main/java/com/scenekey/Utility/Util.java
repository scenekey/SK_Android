package com.scenekey.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.scenekey.R;


/**
 * Created by mindiii on 3/12/16.
 */
public class Util {


    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)

                for (int i = 0; i < info.length; i++)

                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }

        return false;
    }

    public static boolean isGPSEnabled(Context context) {

        boolean result = true;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            result = false;
            ;
        }
        return result;
    }

    /***
     * @param activity
     * @param color
     */

    public static void setStatusBarColor(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color == 0)
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorprimarytop));
            else activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
        }

    }




    /*public static Bitmap URLImage(String url, final ImageView imageView, Context context){
        final Bitmap[] bitmap = new Bitmap[1];
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap response) {
                        bitmap[0] =response;
                        imageView.setImageBitmap(RoundImage.getCircleBitmap(response));
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setBackgroundColor(Color.parseColor("#ff0000"));
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(context).add(imgRequest);
        return bitmap[0];}

    public static Bitmap URLImageNocircle(String url, final ImageView imageView,Context context){
        final Bitmap[] bitmap = new Bitmap[1];
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap response) {
                        bitmap[0] =response;
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setBackgroundColor(Color.parseColor("#ff0000"));
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(context).add(imgRequest);
        return bitmap[0];}*/


    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static void printBigLogcat(String TAG, String response) {
        int maxLogSize = 1000;
        for (int i = 0; i <= response.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > response.length() ? response.length() : end;
            Log.e(TAG, response.substring(start, end));
        }
    }
}
