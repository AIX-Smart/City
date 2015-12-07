package com.aix.city.core.data;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Comment extends Post {

    int eventId;

    //no-argument constructor for JSON
    private Comment(){}

    /**
     * INTERNAL USE ONLY: use instead event.createPostListing().createComment(String message)
     */
    public Comment(int postID, String message, long creationTime, int authorId, boolean liked, int likeCount, int eventId) {
        super(postID, message, creationTime, authorId, liked, likeCount);
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    @Override
    public boolean isComment(){
        return true;
    }

}
