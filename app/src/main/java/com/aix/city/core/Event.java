package com.aix.city.core;

import com.android.internal.util.Predicate;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Event extends Post implements ListingSource {

    private Location location;
    private int commentCount;
    private boolean commented;
    private EditableCommentListing listing;
    //private Timestamp eventStartTime;
    //private Timestamp eventEndTime;

    /**
     * INTERNAL USE ONLY: use instead location.getPostListing().createEvent(String message)
     */
    public Event(long postID, String message, Timestamp creationTime, int likeCount, User author, boolean likeStatus, Location location, int commentCount, boolean commented) {
        super(postID, message, creationTime, likeCount, author, likeStatus);
        this.location = location;
        this.commentCount = commentCount;
        this.commented = commented;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public int getCommentCount() {
        return commentCount;
    }

    /**
     * @return returns true if this event has been commented by the logged in user
     */
    public boolean isCommented() {
        return commented;
    }

    public void setCommented(boolean commented) {
        this.commented = commented;
    }

    @Override
    public void update() {
        super.update();
        //TODO: update post from database
    }

    @Override
    public boolean isComment(){
        return false;
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
    public EditableCommentListing getPostListing() {
        if (listing == null) {
            listing = new EditableCommentListing(this);
        }
        return listing;
    }
}
