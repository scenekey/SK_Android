package com.scenekey.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.ImageUtil;
import com.scenekey.Utility.Permission;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.helper.Constants;
import com.scenekey.lib_sources.SwipeCard.Card;
import com.scenekey.models.UserInfo;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mindiii on 5/6/17.
 */
public class UpdateFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = UpdateFragment.class.toString();
    TextView txt_feedback,txt_logout;
    CircleImageView img_profile_update;
    EditText edt_email;
    String bashImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f2_details,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText edt_first_name = (EditText) view.findViewById(R.id.edt_first_name);
        EditText edt_last_name = (EditText) view.findViewById(R.id.edt_last_name);
         edt_email = (EditText) view.findViewById(R.id.edt_email);
        TextView txt_updateImage = (TextView) view.findViewById(R.id.txt_updateImage);
        ImageView img_f1_back = (ImageView) view.findViewById(R.id.img_f1_back);
        txt_feedback = (TextView) view.findViewById(R.id.txt_feedback);
        txt_logout = (TextView) view.findViewById(R.id.txt_logout);

        LinearLayout mainlayout= (LinearLayout) view.findViewById(R.id.mainlayout);
        img_profile_update = (CircleImageView) view.findViewById(R.id.img_profile_update);

        edt_first_name.setText(activity().userInfo().getFirstname());
        edt_last_name.setText(activity().userInfo().getLastname());
        edt_email.setText(activity().userInfo().getEmail());
        Font font = new Font(activity());
        font.setFontRailRegular(txt_updateImage,txt_logout,txt_feedback);
        Picasso.with(activity()).load(activity().userInfo().getUserImage()).into(img_profile_update);
        setClick(mainlayout,txt_updateImage,img_f1_back,txt_feedback);

    }


    void setClick(View... views){
        for(View view:views){
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            bashImage = ImageUtil.encodeTobase64(  ((BitmapDrawable)img_profile_update.getDrawable()).getBitmap());
        }catch (Exception e){

        }
    }

    HomeActivity activity(){
        return HomeActivity.instance;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_updateImage:
                captureImage();
                break;
            case R.id.txt_logout:
                activity().onBackPressed();
                break;
            case R.id.img_f1_back:
                activity().onBackPressed();
                break;
            case R.id.txt_feedback:
                postImage();
                break;


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        activity().setBBvisiblity(View.GONE,TAG);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == Constants.INTENT_CAMERA && data != null) {

                final Bitmap eventImg = (Bitmap) data.getExtras().get("data");
                bashImage = ImageUtil.encodeTobase64(eventImg);
                img_profile_update.setImageBitmap(eventImg);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_CAMERA) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (activity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //TODO : May be need to toast the msg that user denied permission
                } else captureImage();
            }
            else captureImage();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    void captureImage() {
        Permission permission = new Permission(activity(), activity());
        if (permission.checkForCamera()) callIntent(Constants.INTENT_CAMERA);
    }

    public void callIntent(int caseid) {

        switch (caseid) {
            case Constants.INTENT_CAMERA:
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constants.INTENT_CAMERA);
                } catch (Exception e) {

                }
                break;
        }
    }

    void postImage(){
        activity().showProgDilog(false,TAG);
        VolleyGetPost postImage = new VolleyGetPost(activity(),activity(),WebService.Update_INFO,false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG,response+" ");
                activity().dismissProgDailog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("userinfo"))getUserinfo(jsonObject.getJSONObject("userinfo"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                activity().dismissProgDailog();
                Log.e(TAG,error+" ");
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();
            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("user_id",activity().userInfo().getUserID());
                params.put("type","Social User");
                params.put("email",edt_email.getText().toString());
                params.put("Name",activity().userInfo().getUserName());
                if(bashImage!=null)params.put("image",bashImage);
                else params.put("image",ImageUtil.encodeTobase64(((BitmapDrawable)img_profile_update.getDrawable()).getBitmap()));
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        postImage.execute();

    }

    void getUserinfo(JSONObject userInfo) throws JSONException {
        UserInfo userInfo1 = activity().userInfo();
        if(userInfo.has("email"))userInfo1.setEmail(userInfo.getString("email"));
        if(userInfo.has("userImage"))userInfo1.setUserImage(userInfo.getString("userImage"));
        if(userInfo.has("makeAdmin"))userInfo1.setMakeAdmin(userInfo.getString("makeAdmin"));
        //if(userInfo.has("makeAdmin"))userInfo1.setMakeAdmin("yes");
        activity().updateSession(userInfo1);


       /* {
            "success":1, "msg":"Success",
                "userinfo":{
            "userID":"174", "email":"ratnesh.mindiii@gmail.com", "password":"", "fullname":
            "Thomas Lewis", "userName":"Thomas Lewis", "userGender":"0", "address":
            false, "makeAdmin":"no", "userImage":
            "http:\/\/hiiandbyee.com\/scenekeyFinal\/upload\/593507fa3ad9a.jpg", "logintime":
            "2017-06-05 00:26:28", "stagename":"0", "venuename":"", "artisttype":"0", "firstname":
            "Thomas", "lastname":"Lewis"
        }*/

    }


}
