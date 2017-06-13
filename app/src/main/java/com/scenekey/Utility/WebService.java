package com.scenekey.Utility;

/**
 * Created by mindiii on 14/4/17.
 */

public class WebService {

    //Devlopment
    //public final static String BASE_URL = "http://mindiii.com/scenekeyNew/scenekey/index.php/event/";

    //By PHP
    //public final static String BASE_URL = "http://mindiii.com/scenekeyFinal/index.php/event/";


    //By HIBYEE
    //public final static String BASE_URL = "http://hiiandbyee.com/scenekeyFinal/index.php/event/";
    //By WedRewward
   // public final static String BASE_URL = "http://www.weedrwrds.com/scene/index.php/event/";
    //Amazon
    //public final static String BASE_URL = "http://php-env.us-west-1.elasticbeanstalk.com/event/";
    //AWS
    public final static String BASE_URL = "http://php-env.us-west-1.elasticbeanstalk.com/mindiiiNew/event/";

    public final static String TERMS_ ="http://hiiandbyee.com/scenekeyFinal/Terms&conditions.pdf";

    //Final
   // public final static String BASE_URL = "http://scenekey.com/scenekey/index.php/event/";


    //http://mindiii.com/scenekeyNew/scenekey/
    public final static String BASE_IMAGE_URL = "http://php-env.us-west-1.elasticbeanstalk.com/mindiiiNew/";



    public final static String Login = BASE_URL + "webservices/facebookLogin";
    public final static String CHK_LOGIN = BASE_URL + "webservices/chkLogin";
    public final static String EVENT_BY_LOCAL = BASE_URL + "eventByLocation";
    public final static String TRENDING = BASE_URL + "trending";

    public final static String LISTUSEREVENT = BASE_URL + "webservices/listofuserattendedevent";
    public final static String LISTEVENTFEED = BASE_URL + "webservices/listofeventfeeds";

    public final static String VENUE_SEARCH = BASE_URL + "venueSearch";
    public final static String CATEGORY_TAG = BASE_URL + "allCategory";

    /**
     * Event Like , Comment and post picture
     ***/
    public final static String EVENT_LIKE = BASE_URL + "webservices/addEventLike";
    public final static String EVENT_COMMENT = BASE_URL + "webservices/addeventcomment";
    public final static String EVENT_POST_PIC = BASE_URL + "webservices/addeventpicture";
    public final static String ADD_EVENT = BASE_URL + "webservices/addevent";

    public final static String ADD_NUDGE = BASE_URL + "webservices/addnudges";
    public final static String GET_NUDGE = BASE_URL + "webservices/getnudges";

    public final static String SET_STATUS = BASE_URL + "webservices/SetUserStatus";


    public final static String LISTATTENDEDEVENT = BASE_URL + "webservices/listofuserattendedevent";

    //Update
    public final static String Update_INFO = BASE_URL + "webservices/updateuserInfo";
    public final static String MAKE_SCENE = BASE_URL + "makescen";
    public final static String MAKE_SCENE_UPDATE = BASE_URL + "makescenUpdate";
    public final static String DELETE_EVENT = BASE_URL + "deleteEvent";

    /*[{"key":"user_id","value":"174"},{"key":"venue_id","value":"130178"},{"key":"date","value":"2017-06-05"},{"key":"time","value":"9:00:00"},{"key":"event_name","value":"Monday ..... Test "},{"key":"interval","value":"15"},{"key":"description","value":"Android Testing Event For IOS also"}]*/


}
