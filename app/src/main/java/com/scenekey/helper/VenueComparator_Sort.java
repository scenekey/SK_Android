package com.scenekey.helper;

import com.scenekey.models.Venue;

import java.util.Comparator;

/**
 * Created by mindiii on 9/5/17.
 */

public class VenueComparator_Sort implements Comparator<Venue> {

    @Override
    public int compare(Venue o1, Venue o2) {

        return (o1.getVenue_name().toLowerCase()).compareTo(o2.getVenue_name().toLowerCase());
    }
}
