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
    //private Timestamp eventStartTime;
    //private Timestamp eventEndTime;

    //TODO: unter umständen stattdessen eine Factory zur Erstellung nutzen
    public static Event create(String message, Location location) {
        long ID = 0; //TODO: getID from server
        User user = AIxLoginModule.getInstance().getLoggedInUser();
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        Event event = new Event(ID, message, now, 0, user, false, false, location, 0, false);
        //TODO: Add Post to database
        return event;
    }

    /**
     * INTERNAL USE ONLY: use instead Event.create(...)
     */
    public Event(long postID, String message, Timestamp creationTime, int likeCount, User author, boolean likeStatus, boolean deleted, Location location, int commentCount, boolean commented) {
        super(postID, message, creationTime, likeCount, author, likeStatus, deleted);
        this.location = location;
        this.commentCount = commentCount;
        this.commented = commented;
    }

    public Location getLocation() {
        return location;
    }


    /*public Timestamp getEventStartTime() {
        return eventStartTime;
    }*/

    /*public Timestamp getEventEndTime() {
        return eventEndTime;
    }*/

    public int getCommentCount() {
        return commentCount;
    }

    /**
     * @return returns true if this event has been commented by the logged in user
     */
    public boolean isCommented() {
        return commented;
    }

    public Comment createComment(String message) {
        if (commented) {
            //TODO: throw exception (falls die beschränkung auf 1 comment pro user existiert)
        }
        long ID = 1; //TODO: getID from server
        User user = AIxLoginModule.getInstance().getLoggedInUser();
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        Comment comment = new Comment(ID, message, now, 0, user, false, false, this);
        commented = true;
        commentCount++;
        //TODO: Add Post to database
        return comment;
    }

    public void removeComment(Comment comment) {
        comment.rawDelete();
        if (comment.getAuthor() == AIxLoginModule.getInstance().getLoggedInUser()) {
            commented = false;
        }
        commentCount--;
        //TODO: commit deletion to database
    }

    @Override
    public void update() {
        super.update();
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
