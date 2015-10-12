package com.aix.city.core;

import com.android.internal.util.Predicate;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Event extends Post implements ListingSource {

    private Location location;
    private int commentCount;
    private Timestamp eventStartTime;
    private Timestamp eventEndTime;

    public static Event create(String message, Location location, Timestamp eventStartTime, Timestamp eventEndTime) {
        User user = AIxLoginModule.getInstance().getLoggedUser();
        long ID = 0; //TODO: getID from server
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        Event event = new Event(ID, message, now, 0, user, false, location, 0, eventStartTime, eventEndTime);
        //TODO: Add Post to database
        return event;
    }

    //Internal Use Only: use instead Event.create(...)
    protected Event(long postID, String message, Timestamp creationTime, int likeCount, User author, boolean likeStatus, Location location, int commentCount, Timestamp eventStartTime, Timestamp eventEndTime) {
        super(postID, message, creationTime, likeCount, author, likeStatus);
        this.location = location;
        this.commentCount = commentCount;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }

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
    public void update() {
        //TODO: update post from database
    }

    @Override
    public DatabaseRequest getDatabaseRequest() {
        //TODO: Implementation
        return null;
    }

    @Override
    public Predicate<Post> getFilter() {
        //TODO: Implementation
        return null;
    }

    @Override
    public Listing getListing() {
        //TODO: Implementation
        return null;
    }

}
