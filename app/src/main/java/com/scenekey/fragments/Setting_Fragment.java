package com.scenekey.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.scenekey.R;
import com.scenekey.Utility.CusDialogProg;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.Util;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.helper.Constants;
import com.scenekey.models.Events;
import com.scenekey.models.UserInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mindiii on 3/5/17.
 */

public class Setting_Fragment extends Fragment implements View.OnClickListener{

    private static final String TAG = Setting_Fragment.class.toString();
    EditText edt_last_name,edt_first_name,edt_email;
    TextView edt_location;
    TextView txt_feedback,txt_logout ,txt_admin;

    View pop_up_view;
    Dialog dialog;
    LatLng latLng;
    PlaceAutocompleteFragment autocompleteFragment;
    View view;
    CusDialogProg  cusDialogProg;
    RelativeLayout lnr_location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.f2_setting, null);
        activity().setBBvisiblity(View.GONE,TAG);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText settingText = (EditText) view.findViewById(R.id.edt_terms);
        EditText edt_privacy = (EditText) view.findViewById(R.id.edt_privacy);
        ImageView img_f1_back = (ImageView) view.findViewById(R.id.img_f1_back);
        TextView txt_f1_title = (TextView) view.findViewById(R.id.txt_f1_title);
        LinearLayout mainlayout= (LinearLayout) view.findViewById(R.id.mainlayout);
        LinearLayout lnr_deatils= (LinearLayout) view.findViewById(R.id.lnr_deatils);
        lnr_location= (RelativeLayout) view.findViewById(R.id.lnr_location);
        edt_first_name = (EditText) view.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) view.findViewById(R.id.edt_last_name);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_location = (TextView) view.findViewById(R.id.edt_location);
        txt_logout = (TextView) view.findViewById(R.id.txt_logout);
        txt_feedback = (TextView) view.findViewById(R.id.txt_feedback);
        txt_admin = (TextView) view.findViewById(R.id.txt_admin);
        edt_last_name.setText(activity().userInfo().getLastname());
        cusDialogProg = new CusDialogProg(activity(),R.layout.custom_progress_dialog_layout);
        try{
            if(activity().userInfo().getMakeAdmin().contains(Constants.ADMIN_NO)){
            txt_admin.setVisibility(View.GONE);
            lnr_location.setVisibility(View.GONE);

        }
        else {
            txt_admin.setVisibility(View.VISIBLE);
            lnr_location.setVisibility(View.VISIBLE);
                if(activity().userInfo().getAddress()==null || activity().userInfo().getAddress().length()<2){
                    getAddress(Double.parseDouble(activity().userInfo().getLatitude()),Double.parseDouble(activity().userInfo().getLongitude()));
                }
                if(activity().userInfo().getAddress().length()>60){
                    edt_location.setText(activity().userInfo().getAddress().substring(0,60)+"...");
                }
                    else edt_location.setText(activity().userInfo().getAddress());

        }}
        catch (Exception e){
            Log.e(TAG,""+e);
        }
        try
        {if(!activity().userInfo().getEmail().contains(activity().userInfo().getFacebookId()))edt_email.setText(activity().userInfo().getEmail());
        else edt_email.setText(getString(R.string.noemail));}
        catch (Exception e){

        }


        //TODO manageing on email not fond / email is auto genrated by facebook ID
        //TODO getting the Location on  GPS on  if GPS is not On
        Font font = new Font(activity());/*
        font.setFontLibreFranklin_SemiBold(settingText,edt_privacy);
        font.setFontFrankBookReg(edt_first_name,edt_last_name);*/
        font.setFontFranklinRegular(edt_email,edt_first_name,edt_last_name,settingText,edt_privacy,edt_location,txt_f1_title);
        setClick(edt_email,edt_last_name,settingText,edt_privacy,txt_f1_title,mainlayout,txt_logout,txt_feedback,img_f1_back,lnr_deatils);
        font.setFontRailRegular(txt_feedback,txt_logout);


        try {
            autocompleteFragment = (PlaceAutocompleteFragment) activity().getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    //address = place.getAddress().toString();
                    edt_location.setText(place.getAddress().toString());
                    latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    if(activity().userInfo().getMakeAdmin().contains(Constants.ADMIN_YES)){
                        UserInfo userInfo =  activity().userInfo();
                        userInfo.setLatitude(latLng.latitude+"");
                        userInfo.setLongitude(latLng.longitude+"");
                        userInfo.setAddress(place.getAddress().toString());
                        activity().updateSession(userInfo);
                        updateLocation();
                    }

                }

                @Override
                public void onError(Status status) {
                    Log.d("ERROR:::", status.getStatusMessage());
                }
            });
        }
        catch (Exception e){

        }
    }

    HomeActivity activity() {
            return  HomeActivity.instance;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //activity().setBBvisiblity(View.VISIBLE);

    }

    @Override
    public void onDestroyView() {
        try
        {
            if(activity()!=null)activity().getFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
        }catch (IllegalStateException e){

        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        activity().setBBvisiblity(View.GONE,TAG);
        super.onResume();
    }

    void setClick(View... views){
        for(View view:views){
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_logout:
            activity().getSessionManager().logout(activity());
            break;
            case R.id.txt_feedback:
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, "info@scenekey.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "feedback:");*/
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto: info@scenekey.com"));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                    //startActivity(intent);
                }catch (Exception e){

                }

                break;
            case R.id.img_f1_back:
                activity().onBackPressed();
                break;

            case R.id.lnr_deatils:
                activity().addFragment(new UpdateFragment(),1);
                break;

            case R.id.edt_terms:
                showPdf(WebService.TERMS_,"Terms & Conditions");
                break;
            case R.id.lnr_location:

                break;

        }
    }

    String getAddress(double latitude, double longitude) {
        String result = null;
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(activity(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String addressLine = addresses.get(0).getAddressLine(1);
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                //result = knownName + " ," + addressLine + " , " + city + "," + state + "," + country + " counter" + counter;// Here 1 represent max location result to returned, by documents it recommended 1 to 5
                result =  address+","+city + "," + state + "," + country ;// Here 1 represent max location result to returned, by documents it recommended 1 to 5
                edt_location.setText(result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
        return result;
    }

    View showPdf(String url,String title){
        TextView txt_pop_title,btn_pop_cancel;
        pop_up_view = LayoutInflater.from(activity()).inflate(R.layout.pop2_web_view, null);
        txt_pop_title = (TextView) pop_up_view.findViewById(R.id.txt_pop_title);
        btn_pop_cancel = (TextView) pop_up_view.findViewById(R.id.btn_pop_cancel);
        WebView webView = (WebView) pop_up_view.findViewById(R.id.webView);

        txt_pop_title.setText(title);
        (new Font(activity())).setFontRailRegular(txt_pop_title);

        dialog = new Dialog(activity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(pop_up_view);

        btn_pop_cancel.setOnClickListener(this);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        btn_pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final ProgressDialog pDialog = new ProgressDialog(activity());
        //pDialog.setTitle(this.getString(R.string.app_name));
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });
        webView.loadUrl("https://docs.google.com/gview?url=" + url + "&overridemobile=true");
        return pop_up_view;

    }

    void updateLocation() {
        cusDialogProg.setCancelable(false);
        cusDialogProg.setCanceledOnTouchOutside(false);
        cusDialogProg.setColor(R.color.transparent);
        cusDialogProg.show();

        VolleyGetPost volleyGetPost = new VolleyGetPost(activity(), activity(), WebService.EVENT_BY_LOCAL, false) {
            @Override
            public void onVolleyResponse(String response) {
                Util.printBigLogcat(TAG, " " + response);
                //{"userInfo":{"fullname":"Donie Darko","address":"Ujjain, Madhya Pradesh, India","lat":"23.179301300000002","longi":"75.7849097","makeAdmin":"yes"}}
                try {
                    JSONObject jo = new JSONObject(response);
                    if (jo.has("userInfo")) {
                        UserInfo  userInfo = activity().userInfo();
                        Object intervention = jo.get("userInfo");
                        if (intervention instanceof JSONArray) {
                            activity().getSessionManager().logout(activity());
                        }
                        JSONObject user = jo.getJSONObject("userInfo");
                        if(user.has("makeAdmin"))   {
                            userInfo.setMakeAdmin(user.getString("makeAdmin"));

                        }
                        if(user.has("lat"))         userInfo.setLatitude(user.getString("lat"));
                        if(user.has("longi"))       userInfo.setLongitude(user.getString("longi"));
                        if(user.has("adminLat"))    userInfo.setLatitude(user.getString("adminLat"));
                        if(user.has("adminLong"))   userInfo.setLongitude(user.getString("adminLong"));
                        if(user.has("address"))       userInfo.setAddress(user.getString("address"));
                        if(userInfo.getMakeAdmin().equals(Constants.ADMIN_NO)){
                            lnr_location.setVisibility(View.GONE);
                        }
                        activity().updateSession(userInfo);
                        //else if()
                    } else {
                        activity().showToast( getResources().getString(R.string.somethingwentwrong));
                    }
                    if (cusDialogProg != null) cusDialogProg.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (cusDialogProg != null) cusDialogProg.dismiss();
                    activity().showToast( getResources().getString(R.string.somethingwentwrong));
                }

            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (cusDialogProg != null) cusDialogProg.dismiss();
                try {
                    activity().showToast( getResources().getString(R.string.somethingwentwrong));
                }catch (Exception e){

                }
                Log.e(TAG, "" + error);
            }

            @Override
            public void onNetError() {
                if (cusDialogProg != null) cusDialogProg.dismiss();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("lat",latLng.latitude+"");
                params.put("long",latLng.longitude+"");
                params.put("user_id",activity().userInfo().getUserID());
                params.put("updateLocation", Constants.ADMIN_YES);
                params.put("fullAddress", edt_location.getText().toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        volleyGetPost.execute();

    }
}
