package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Comment extends Post {

    long eventId;

    //no-argument constructor for JSON
    private Comment(){}

    /**
     * INTERNAL USE ONLY: use instead event.getPostListing().createComment(String message)
     */
    public Comment(long postID, String message, long creationTime, int likeCount, long authorId, boolean likeStatus, long eventId) {
        super(postID, message, creationTime, likeCount, authorId, likeStatus);
        this.eventId = eventId;
    }

    public long getEventId() {
        return eventId;
    }

    @Override
    public void update() {
        super.update();
        //TODO: update post from database
    }

    @Override
    public boolean isComment(){
        return true;
    }

    @Override
    public String getSourceName() {
        return String.valueOf(eventId);
    }
}
