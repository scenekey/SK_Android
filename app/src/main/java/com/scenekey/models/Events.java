package com.scenekey.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mindiii on 24/4/17.
 */

public class Events implements Serializable {
    Venue venue;
    ArrayList<Artists> artistsArrayList;
    Event event;

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
        if (events.has("interval")) event.setInterval(events.getInt("interval"));
        if (events.has("status")) event.setStatus(events.getString("status"));
        if (events.has("trending_point"))
            event.setTrending_point(events.getString("trending_point"));
        this.event = event;

    }

    public Event getEvent() {
        return this.event;
    }
}
