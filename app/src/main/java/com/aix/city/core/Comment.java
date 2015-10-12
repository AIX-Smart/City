package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Comment extends Post {

    private Event event;

    //Internal Use Only: use instead Event.create(...)
    protected Comment(long postID, String message, Timestamp creationTime, int likeCount, User author, boolean likeStatus, Event event) {
        super(postID, message, creationTime, likeCount, author, likeStatus);
        this.event = event;
    }

    public Event getEvent() {
        return null;
    }

    @Override
    public void update() {
        //TODO: update post from database
    }
}
