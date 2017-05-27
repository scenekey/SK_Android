package com.scenekey.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.scenekey.R;
import com.scenekey.Utility.CusDialogProg;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.Permission;
import com.scenekey.Utility.Util;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.helper.Constants;
import com.scenekey.helper.SessionManager;
import com.scenekey.helper.SocialRegistrationBean;
import com.scenekey.models.UserInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by mindiii on 10/4/17.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    static final String TAG = LoginActivity.class.toString();
    public static int CALLBACK = 0;
    public boolean facebookClicked;
    ImageView logo_a2;
    TextView txt_fb_login_a1;
    boolean animate;
    //Facebook
    CallbackManager callbackManager;
    String profileImage;
    SocialRegistrationBean socialRegistrationBean;
    CusDialogProg dialogProg;
    SessionManager sessionManager;
    private Permission permission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());//Facebook
        //AppEventsLogger.activateApp(this);//Facebook
        FirebaseApp.initializeApp(getApplicationContext());

        overridePendingTransition(R.anim.loginf_in, R.anim.loginf_out);
        setContentView(R.layout.a2_login_activity);
        animate = true;
        facebookClicked = false;
       /* if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setViews();
        socialRegistrationBean = new SocialRegistrationBean();
        permission = new Permission(LoginActivity.this, LoginActivity.this);
        sessionManager = new SessionManager(LoginActivity.this);

    }

    void setViews() {
        txt_fb_login_a1 = (TextView) findViewById(R.id.txt_fb_login_a1);
        logo_a2 = (ImageView) findViewById(R.id.logo_a2);
        Font font = new Font(this);
        font.setFontRailRegular(txt_fb_login_a1);
        if (animate)
            logo_a2.animate().translationYBy(-80f).setDuration(600).setStartDelay(1000).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Animation animat = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in);
                    txt_fb_login_a1.setAnimation(animat);
                    txt_fb_login_a1.setVisibility(View.VISIBLE);
                    animate = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        txt_fb_login_a1.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_fb_login_a1:

                if (permission.askForGps()) {
                    facebooklogin(socialRegistrationBean, v.getId());
                    dialogProg = new CusDialogProg(this, R.layout.custom_progress_dialog_layout);
                    dialogProg.show();

                } else {
                    facebookClicked = true;
                    CALLBACK = Constants.CB_RETURN_TO_FB;
                }
                //callIntent(v.getId());
                break;
        }
    }


    void callIntent(int Id) {
        switch (Id) {

            case R.id.txt_fb_login_a1:
                Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(in);
                finish();
                break;
        }
    }


    void facebooklogin(final SocialRegistrationBean socialRegistrationBean, final int ButtonId) {

        if (Util.isConnectingToInternet(getApplicationContext())) {
            LoginManager.getInstance().logOut();
            //LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","email","user_birthday","user_friends"));
            callbackManager = CallbackManager.Factory.create();
            CALLBACK = Constants.CALL_BACK_FB;
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    Log.e(TAG, "FACEBOOK" + loginResult.toString());
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.e(TAG, "login result" + object.toString() + response.toString());
                                    String FBemail;
                                    try {
                                        try {
                                            FBemail = object.getString("email");
                                        } catch (Exception e) {
                                            FBemail = object.getString("id") + ".scenekey" + "@fb.com";
                                        }
                                        final String FBid = object.getString("id");
                                        final String FBname = object.getString("name");
                                        String FBgender = object.getString("gender");
                                        JSONObject age = object.getJSONObject("age_range");
                                        final String FBimageurl = "https://graph.facebook.com/" + FBid + "/picture?type=large";

                                        Log.e("image", FBimageurl);
                                        Log.e("response", response.toString());
                                        Log.e("Email", FBemail);
                                        Log.e("ID", FBid);
                                        Log.e("Name", FBname);
                                        Log.e("Fb Image", FBimageurl);
                                        try{
                                            Log.e("Fb BirthDay", object.getString("birthday"));
                                        }catch (Exception e){

                                        }
                                        socialRegistrationBean.setFullname(FBname);
                                        socialRegistrationBean.setSocialId(FBid);
                                        socialRegistrationBean.setImage(FBimageurl);
                                        socialRegistrationBean.setImage(FBgender);
                                        LoginUser user = new LoginUser();
                                        user.setEmail(FBemail);
                                        user.setFacebookid(FBid);
                                        user.setFullName(FBname);
                                        user.setProfileImage(FBimageurl);
                                        user.setDevicetoken(FirebaseInstanceId.getInstance().getToken());
                                        user.setFbusername(FBname);
                                        user.setUserType("Social User");
                                        user.setGender(FBgender);

                                        SendfeedbackJob sendfeedbackJob = new SendfeedbackJob();
                                        sendfeedbackJob.execute(user);


                                    } catch (JSONException e) {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
                                        if (dialogProg != null) dialogProg.dismiss();
                                        Log.e(TAG, "FB Login" + e);
                                    }

                                }
                            });
                    Bundle parameters = new Bundle();
                    //parameters.putString("fields", "id,name,email,gender,user_birthday,age_range,location");
                    parameters.putString("fields", "id,name,email,gender,age_range,location");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    if (dialogProg != null) dialogProg.dismiss();
                    Toast.makeText(LoginActivity.this, "Cancelled by User", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(FacebookException error) {
                    if (dialogProg != null) dialogProg.dismiss();
                    Log.e("Facebook", "Error" + error);
                }
            });
        } else {
            Toast.makeText(this, "Plese connect to inernet", Toast.LENGTH_SHORT).show();
        }
    }

    void volleyCheckResponse(String s, final LoginUser user) {
        Log.e(TAG, "Volley Response" + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            int status = jsonObject.getInt("success");
            //"success":1,"msg":"Facebook user first time login"
            if (status == 1) {

                /*****************************************Volley 2 For registration***********************************/
                VolleyGetPost volleyGetPost2 = new VolleyGetPost(LoginActivity.this, getApplicationContext(), WebService.Login, false) {
                    @Override
                    public void onVolleyResponse(String response) {
                        Log.e(TAG, "Volley2" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("success");
                            if (status == 1) {
                                if(createSession(jsonObject.getJSONObject("userinfo"),user))
                                callIntent(txt_fb_login_a1.getId());
                            } else {
                                //TODO : Handle the staus != 1
                            }
                        } catch (JSONException e) {
                        }

                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        Log.e(TAG, "Volley2" + error.toString());
                        if (dialogProg != null) dialogProg.dismiss();
                    }

                    @Override
                    public void onNetError() {
                        if (dialogProg != null) dialogProg.dismiss();
                    }

                    @Override
                    public Map<String, String> setParams(Map<String, String> params) {
                        params.put("userEmail", user.getEmail());
                        params.put("facebookid", user.getFacebookid());
                        params.put("fbusername", user.getFbusername());
                        params.put("usertype", user.getUserType());
                        params.put("fullname", user.getFullName());
                        params.put("device_token", user.getDevicetoken());
                        params.put("deviceType", "2");
                        params.put("gender",user.getGender());
                        params.put("ProfileImage", user.getProfileImage());
                        Log.e("Login params", params.toString());
                        return params;
                    }

                    @NotNull
                    @Override
                    public Map<String, String> setHeaders(Map<String, String> params) {
                        return params;
                    }
                };
                volleyGetPost2.execute();

            }
            //"success":0,"msg":"Facebook user Already Added"
            if (status == 0) {
                if(createSession(jsonObject.getJSONObject("userinfo"),user))
                callIntent(txt_fb_login_a1.getId());
            } else {
                //TODO : Handle the staus != 1 or 0
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (dialogProg != null) dialogProg.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CALLBACK == Constants.CALL_BACK_FB)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GPS_RESULT_CODE && CALLBACK == Constants.CB_RETURN_TO_FB) {
            txt_fb_login_a1.callOnClick();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.GPS_RESULT_CODE) {

        }
    }

    void toast(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }


    boolean createSession(JSONObject jsonObject,LoginUser loginUser) {
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserID(jsonObject.getString("userID"));
            userInfo.setEmail(jsonObject.getString("email"));
            userInfo.setFullname(jsonObject.getString("fullname"));
            userInfo.setUserName(jsonObject.getString("userName"));
            userInfo.setUserGender(jsonObject.getString("userGender"));
            userInfo.setUserImage(jsonObject.getString("userImage"));
            userInfo.setLoginTime(jsonObject.getString("logintime"));
            userInfo.setStagename(jsonObject.getString("stagename"));
            userInfo.setVenuName(jsonObject.getString("venuename"));
            userInfo.setArtisttype(jsonObject.getString("artisttype"));
            userInfo.setFirstname(jsonObject.getString("firstname"));
            userInfo.setLastname(jsonObject.getString("lastname"));
            userInfo.setFacebookId(loginUser.getFacebookid());
            userInfo.setUserAccessToken(AccessToken.getCurrentAccessToken());
            userInfo.setMakeAdmin(jsonObject.getString("makeAdmin"));
            sessionManager.createSession(userInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogProg != null) dialogProg.dismiss();
    }


    //Facebook Image to Byte array
    private String downloadUrl(String s) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            URL toDownload = new URL(s);
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


    class SendfeedbackJob extends AsyncTask<LoginUser, Void, LoginUser> {


        @Override
        protected LoginUser doInBackground(LoginUser... params) {
            LoginUser user = params[0];
            String image = downloadUrl(user.getProfileImage());
            user.setProfileImage(image);
            while (true) {
                Log.e("image", " : " + image);
                if (image != null) break;
                else image = downloadUrl(user.getProfileImage());
            }
            return user;
        }

        @Override
        protected void onPostExecute(final LoginUser user) {
            VolleyGetPost volleyGetPost = new VolleyGetPost(LoginActivity.this, getApplicationContext(), WebService.CHK_LOGIN, false) {
                @Override
                public void onVolleyResponse(String response) {
                    Log.e(TAG," response"+response);
                    volleyCheckResponse(response, user);

                }

                @Override
                public void onVolleyError(VolleyError error) {
                    if (dialogProg != null) dialogProg.dismiss();
                    error.printStackTrace();
                }

                @Override
                public void onNetError() {
                    if (dialogProg != null) dialogProg.dismiss();
                }

                @Override
                public Map<String, String> setParams(Map<String, String> params) {
                    params.put("facebook_id", user.getFacebookid());
                    params.put("Fullname", user.getFullName());
                    params.put("device_token", user.getDevicetoken());
                    params.put("deviceType", "2");
                    params.put("ProfileImage", user.getProfileImage());
                    params.put("gender",user.getGender());
                    Log.e("Login Params",params.toString());
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

    class LoginUser {
        private String email;
        private String facebookid;
        private String fbusername;
        private String userType;
        private String fullName;
        private String devicetoken;
        private String profileImage;
        private String gender;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFacebookid() {
            return facebookid;
        }

        public void setFacebookid(String facebookid) {
            this.facebookid = facebookid;
        }

        public String getFbusername() {
            return fbusername;
        }

        public void setFbusername(String fbusername) {
            this.fbusername = fbusername;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getDevicetoken() {
            return devicetoken;
        }

        public void setDevicetoken(String devicetoken) {
            this.devicetoken = devicetoken;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}
