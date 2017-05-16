package com.scenekey.Utility;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mindiii on 14/4/17.
 */

public abstract class VolleyGetPost {


    Activity activity;
    Context context;
    String Url;
    boolean isMethodGet;
    int retryTime = 20000;

    /**
     * @param activity    set Your current activity Like LoginActivity.this , (LoginActivity) getActivity
     * @param context     prefferd getApplication Context
     * @param url         WebService URl
     * @param isMethodGet if false , Then Volley will call POST method you need to set the Body then, True Volley Get will executed.
     */
    public VolleyGetPost(Activity activity, Context context, String url, boolean isMethodGet) {
        this.activity = activity;
        this.context = context;
        this.Url = url;
        this.isMethodGet = isMethodGet;
    }

    /***
     * required this to execute the request
     */
    public void execute() {
        final Activity activity = this.activity;
        final String Url = this.Url;
        final boolean isMethodGet = this.isMethodGet;
        if (Util.isConnectingToInternet(activity)) {

            RequestQueue queue = Volley.newRequestQueue(activity);

            int method;
            if (this.isMethodGet) method = Request.Method.GET;
            else method = Request.Method.POST;
            StringRequest postRequest = new StringRequest(method, Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            onVolleyResponse(response);

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            onVolleyError(error);

                        }
                    }
            ) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return setParams(params);
                }


                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    return setHeaders(params);
                }
            };

            int socketTimeout = retryTime;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            queue.add(postRequest);


        } else {

            Toast.makeText(activity, "Please Check internet connection.!", Toast.LENGTH_LONG).show();
            onNetError();
        }
    }

    /***
     * @param retryTime set the secound for 30 sec pass 30000
     */
    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    /***
     * @param response use this reponse
     */
    public abstract void onVolleyResponse(String response);

    public abstract void onVolleyError(VolleyError error);

    /**
     * This method will be executed if internet connection is not there
     * Don't forget to dismiss the progressbar in this (if there)
     */
    public abstract void onNetError();

    /***
     * @param params you just need to call params.put(Key,Value)
     * @return params Do not Return null if method is post
     */
    public abstract Map<String, String> setParams(Map<String, String> params);

    /***
     * @param params you just need to call params.put(Key,Value)
     * @return params Do not return null
     */
    @NotNull
    public abstract Map<String, String> setHeaders(Map<String, String> params);


}
