package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Comment extends Post {

    private Event event;

    //no-argument constructor for JSON
    private Comment(){}

    /**
     * INTERNAL USE ONLY: use instead event.getPostListing().createComment(String message)
     */
    public Comment(long postID, String message, long creationTime, int likeCount, long authorId, boolean likeStatus, Event event) {
        super(postID, message, creationTime, likeCount, authorId, likeStatus);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public void update() {
        super.update();
        //TODO: update post from database
    }

    @Override
    public Location getLocation() {
        return event.getLocation();
    }

    @Override
    public boolean isComment(){
        return true;
    }
}
