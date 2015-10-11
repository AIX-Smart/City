package com.aix.city.core;

import com.android.internal.util.Predicate;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Event extends Post implements ListingSource {

    private Location location;
    private int commentCount;
    private Timestamp eventStartTime;
    private Timestamp eventEndTime;

    public Location getLocation() {
        return location;
    }

    public Timestamp getEventStartTime() {
        return eventStartTime;
    }

    public Timestamp getEventEndTime() {
        return eventEndTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public DatabaseRequest getDatabaseRequest() {
        return null;
    }

    @Override
    public Predicate<Post> getFilter() {
        return null;
    }

    @Override
    public Listing getListing() {
        return null;
    }

}
