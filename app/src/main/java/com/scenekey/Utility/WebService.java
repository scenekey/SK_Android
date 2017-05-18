package com.scenekey.Utility;

/**
 * Created by mindiii on 14/4/17.
 */

public class WebService {

    //Devlopment
    public final static String BASE_URL = "http://mindiii.com/scenekeyNew/scenekey/index.php/event/";

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


    public final static String LISTATTENDEDEVENT = BASE_URL + "webservices/listofuserattendedevent";


}
