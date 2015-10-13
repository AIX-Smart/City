package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Comment extends Post {

    private Event event;

    /**
     * INTERNAL USE ONLY: use instead Event.createComment(...)
     */
    public Comment(long postID, String message, Timestamp creationTime, int likeCount, User author, boolean likeStatus, boolean delete, Event event) {
        super(postID, message, creationTime, likeCount, author, likeStatus, delete);
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
}
