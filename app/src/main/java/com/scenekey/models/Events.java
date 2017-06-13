package com.scenekey.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mindiii on 24/4/17.
 */

public class Events implements Serializable {
    Venue venue;
    ArrayList<Artists> artistsArrayList;
    Event event;

    //For Teranding Adapter
    public String timeFormat;
    public String remainingTime;
    public boolean isOngoing;

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public ArrayList<Artists> getArtistsArrayList() {
        return artistsArrayList;
    }

    public void setArtistsArrayList(ArrayList<Artists> artistsArrayList) {
        this.artistsArrayList = artistsArrayList;
    }

    public void setVenueJSON(JSONObject venue) throws JSONException {
        Venue v = new Venue();
        if (venue.has("venue_id")) v.setVenue_id(venue.getString("venue_id"));
        if (venue.has("venue_name")) v.setVenue_name(venue.getString("venue_name"));
        if (venue.has("image")) v.setImage(venue.getString("image"));
        if (venue.has("category")) v.setCategory(venue.getString("category"));
        if (venue.has("category_id")) v.setCategory_id(venue.getString("category_id"));
        if (venue.has("address")) v.setAddress(venue.getString("address"));
        if (venue.has("city")) v.setCity(venue.getString("city"));
        if (venue.has("region")) v.setRegion(venue.getString("region"));
        if (venue.has("country")) v.setCountry(venue.getString("country"));
        if (venue.has("latitude")) v.setLatitude(venue.getString("latitude"));
        if (venue.has("longitude")) v.setLongitude(venue.getString("longitude"));
        if (venue.has("rating")) v.setRating(venue.getInt("rating"));
        this.venue = v;
    }

    public void setArtistsArray(JSONArray artists) throws JSONException {
        if (this.artistsArrayList == null) artistsArrayList = new ArrayList<>();
        for (int i = 0; i < artists.length(); i++) {
            Artists artists1 = new Artists();
            JSONObject object = artists.getJSONObject(i);
            if (object.has("artist_id")) artists1.setArtist_id(object.getString("artist_id"));
            if (object.has("artist_name")) artists1.setArtist_name(object.getString("artist_name"));
            if (object.has("rating")) artists1.setRating(object.getString("rating"));
            artistsArrayList.add(artists1);
        }
    }

    public void setEventJson(JSONObject events) throws JSONException {
        Event event = new Event();
        if (events.has("distance")) event.setDistance(events.getString("distance"));
        if (events.has("event_id")) event.setEvent_id(events.getString("event_id"));
        if (events.has("event_name")) event.setEvent_name(events.getString("event_name"));
        if (events.has("category")) event.setCategory(events.getString("category"));
        if (events.has("description")) event.setDescription(events.getString("description"));
        if (events.has("category_id")) event.setCategory_id(events.getString("category_id"));
        if (events.has("event_date")) event.setEvent_date(events.getString("event_date"));
        if (events.has("event_time")) event.setEvent_time(events.getString("event_time"));
        if (events.has("rating")) event.setRating(events.getString("rating"));
        if (events.has("image")) event.setImage(events.getString("image"));
        if (events.has("interval")) event.setInterval(events.getDouble("interval"));
        if (events.has("status")) event.setStatus(events.getString("status"));
        if (events.has("trending_point"))
            event.setTrending_point(events.getString("trending_point"));
        this.event = event;

    }

    public Event getEvent() {
        return this.event;
    }


    //For trending Adapter

    public void setOngoing(boolean ongoing) {
        isOngoing = ongoing;
    }

    public void settimeFormat(){
       timeFormat = convertDate(event.getEvent_date());
    }
    public void setRemainingTime(){
        try {
            remainingTime = convertTime(event.getEvent_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param date date of the event check format before use tie
     * @return
     * @throws ParseException
     */
    public boolean checkWithTime(final String date , Double interval) throws ParseException {
        String[] dateSplit = (date.replace("TO", "T")).replace(" ", "T").split("T");
        Date startTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateSplit[0] + " " + dateSplit[1]);
        Date endTime = new Date(startTime.getTime()+(int)(interval* 60 * 60 * 1000));
        Log.e("TrendingAdapter ",startTime +"  : "+endTime);
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
        /*if (currentTime < endTime.getTime() && currentTime > startTime.getTime()) {
            return true;
        }*/
        if (currentTime > startTime.getTime() ) {
            return true;
        }
        return false;
    }

    String convertDate(String date) {
        String[] str;
        str = date.split("TO");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date1 = format.parse(str[0]);
            return new SimpleDateFormat("MMMM dd,yyyy hh:mm aa").format(date1);

        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

    }

    String convertTime(String time) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time);
        Date date2 = new Date();
        int milis = Math.abs(date2.getHours() - date1.getHours());
        if(milis==1){
            milis = 60 - Math.abs(date2.getMinutes() - date1.getMinutes());
            return milis + " min";
        }
        return milis + " hr";
    }
}
