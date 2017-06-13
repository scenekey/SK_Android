package com.scenekey.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Switch;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.activity.LoginActivity;
import com.scenekey.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Notification notification;
    SessionManager session;
    /*

    //// Read This (From PHP server )
       array('notficationType'=>1)
   * $msg =$user->fullName." has requested to join " .$eventData['eventName']
   * array('notficationType'=>2)
   * $msg = $this->authData->fullName ." has invited you to join ".$eventName->row()->eventName;
   * array('notficationType'=>3)
   * $msg =$user->fullName ." has accepted your request to join " . $eventData['eventName'];
   * $msg =$user->fullName." has accepted your request to join ".$eventData['eventName'];
   * array('notficationType'=>4)
   * $msg =$user->fullName." has sent you a friend request!";array('notficationType'=>4)$user->fullName." has sent you a friend request!";
   * $msg = $this->authData->fullName." has accepted your friend request!";array('notficationType'=>4)
   *
   *
   * */
    /**
     //From Notificaiton Count at HomeActivity{    "status": "success",    "message": "OK",    "data":
     * {        "id": "34",
     * "userId": "1",
     * "createEvent": "1",  //Create Event = Myrequest(Profile no)
     * "joinEvent": "1",    // joined Request Tab
     * "teamRequest": "1",  // Team Request Tab
     * "freindRequest": "1",//Friend Request
     * "myEvent": "1",      //
     * "acceptFriendRequest": "1"    }}*/


    final String title = "Notification!";
    /*public static int USER_SEND_INVITATION      = 0;    // on Create Event of Bench Request
    public static int FRIEND_REQUEST_ACCEPT     = 0;    // on Friend Request Tab of the Profile Activity
    public static int CREATE_SEND_REQUEST       = 0;    //
    public static int NOTIFICATION_ACCEPT       = 0;
    public static int NOTFICATION_TEAM_REQ      = 0;
    public static int ACCEPT_TEAM_REQ           = 0;


    public static int FRIEND_REQUEST_TYPE_ACCEPT    = 0;
    public static int FRIEND_REQUEST_TYPE_SENT      = 0;

    public static int NOTIFICATION_ACCEPT_FRIEND    = 0;
    public static int NOTIFICATION_ACCEPT_STRANGER  = 0;*/

    boolean toActivity ;
/*
    public static int bottomBar_Profile_count       = CREATE_SEND_REQUEST +FRIEND_REQUEST_ACCEPT+NOTIFICATION_ACCEPT_FRIEND;
    public static int bottomBar_BechRequest_count   = USER_SEND_INVITATION +NOTIFICATION_ACCEPT_STRANGER;*/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom()+remoteMessage.getData());
        session = new SessionManager(getApplicationContext());
//{title=, message=Donie Darko has commented on mindiii nights, notificationType=[]}
        String notificationMsg = remoteMessage.getData().get("message");
        Intent intent =null;
        { SessionManager sessionManager = new SessionManager(this);
            if(sessionManager.isLoggedIn()){
                intent = new Intent(this, HomeActivity.class);
            }else {
                intent = new Intent(this, LoginActivity.class);
            }
        }
        sendNotification(remoteMessage.getTtl(), title, notificationMsg, intent ,false);



      /*  if(remoteMessage.getData().containsKey("message")){
            String notificationMsg =  remoteMessage.getData().get("message").toString();
            String titleMsg =  remoteMessage.getData().get("title").toString();
            System.out.println(TAG+"  notification Msg --------------"+notificationMsg+" Title "+titleMsg+" id"+remoteMessage.getTtl());
            Intent intent = new Intent(this, HomeActivity.class);;
            try {
                JSONObject obj      = new JSONObject(titleMsg);
                String  Type        = obj.getString("notficationType");
                toActivity  = true;
                switch(Type){

                    case "1":

                        USER_SEND_INVITATION +=1;
                        intent.putExtra(Constants.NOTIFICATION , Str.PREF_TYPE_USER_SEND);
                        break;

                    case "2":

                        CREATE_SEND_REQUEST +=1;
                        intent.putExtra(Constants.NOTIFICATION ,Str.PREF_TYPE_MYREQUEST);
                        break;

                    case "3":

                        NOTIFICATION_ACCEPT +=1;
                        intent.putExtra(Constants.NOTIFICATION ,Str.PREF_TYPE_ACCEPTED_JOIN);

                        break;

                    case "4":

                        if(notificationMsg.contains("accepted")){
                            FRIEND_REQUEST_TYPE_ACCEPT  +=1;
                        }
                        else FRIEND_REQUEST_TYPE_SENT   +=1;
                        FRIEND_REQUEST_ACCEPT +=1;
                        intent.putExtra(Constants.NOTIFICATION ,Str.PREF_TYPE_FRIEND);
                        toActivity = true;
                        break;
                    case "5":

                        if(notificationMsg.contains("has been drafted to")){
                            ACCEPT_TEAM_REQ  +=1;
                            intent.putExtra(Constants.NOTIFICATION ,Str.PREF_TYPE_TEAM_LIST_ACT);
                            String teamId =  obj.getString("teamId");
                            intent.putExtra(Str.TEAM_ID ,teamId);
                        }
                        else {
                            NOTFICATION_TEAM_REQ   +=1;
                            intent.putExtra(Constants.NOTIFICATION ,Str.PREF_TYPE_TEAM_REQUEST);
                        }

                        toActivity = true;
                        break;

                }

                boolean foregroud = isAppOnForeground(getApplicationContext());
               try{
                       HomeActivity.activityTest.showNotification(getApplicationContext());

                }catch (Exception e){
                   *//**//*//Log.e("ErrorFIREBASE",e+"");
               }
                *//*intent.setAction("com.benchbuddy.onMessageReceived");
                sendBroadcast(intent);*//*



            } catch (JSONException e) {
                e.printStackTrace();
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.putExtra("notification", notification);
           *//* try{
                HomeActivity.updateBottomBar();
            }catch (Exception e){
                Log.e("FireBase_HOMEBADGE","Error:"+e+"");
                e.printStackTrace();
            }*//*
            *//*try {
                ProfileFragment.updateBadge();
            } catch (Exception e) {
                e.printStackTrace();
            }*//*
            //setCountToSharredPrefrence();
            sendNotification(remoteMessage.getTtl(), title, notificationMsg, intent ,toActivity);

        }*/
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts

    private void sendNotification(int id, String title, String messageBody , Intent intent ,boolean send) {


        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Random r = new Random();
        int random = r.nextInt(899 - 65) + 65;
        int m = ((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE))+random;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, intent, PendingIntent.FLAG_ONE_SHOT);
        //if( intent == null ) pendingIntent = null;
        NotificationCompat.Builder notificationBuilder;
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if( !send ){
             notificationBuilder = new NotificationCompat.Builder(this)
                    //.setSmallIcon(R.drawable.icon_app_192_white)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setContentText(messageBody)
                     .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                             R.drawable.app_icon))
                     .setSmallIcon(R.drawable.key2)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                     .setContentIntent(pendingIntent);;
        }
        else {
            notificationBuilder = new NotificationCompat.Builder(this)
                   // .setSmallIcon(R.drawable.icon_app_192_white)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.app_icon))
                    .setSmallIcon(R.drawable.key2)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);;

        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(m, notificationBuilder.build());
    }




    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }




}
