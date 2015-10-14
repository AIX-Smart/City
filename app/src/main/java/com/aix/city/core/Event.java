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
    private ListingFromEvent listing;
    //private Timestamp eventStartTime;
    //private Timestamp eventEndTime;


    /*public static Event create(String message, Location location) {
        long ID = 0; //TODO: getID from server
        User user = AIXLoginModule.getInstance().getLoggedInUser();
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        Event event = new Event(ID, message, now, 0, user, false, location, 0, false);
        //TODO: Add Post to database
        return event;
    }*/

    /**
     * INTERNAL USE ONLY: use instead location.getListing().createEvent(String message)
     */
    public Event(long postID, String message, Timestamp creationTime, int likeCount, User author, boolean likeStatus, Location location, int commentCount, boolean commented) {
        super(postID, message, creationTime, likeCount, author, likeStatus);
        this.location = location;
        this.commentCount = commentCount;
        this.commented = commented;
    }

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

    /*public Comment createComment(String message) {
        if (commented) {
            //TODO: throw exception (falls die beschränkung auf 1 comment pro user existiert)
        }
        long ID = 1; //TODO: getID from server
        User user = AIXLoginModule.getInstance().getLoggedInUser();
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        Comment comment = new Comment(ID, message, now, 0, user, false, this);
        commented = true;
        commentCount++;
        //TODO: Add Post to database
        return comment;
    }*/

    /*public void removeComment(Comment comment) {
        if(comment.getEvent().equals(this)) {
            comment.rawDelete();
            if (comment.getAuthor() == AIXLoginModule.getInstance().getLoggedInUser()) {
                commented = false;
            }
            commentCount--;
            //TODO: commit deletion to database
        }
    }*/

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
    public ListingFromEvent getListing() {
        if (listing == null) {
            Listing listing = new ListingFromEvent(this);
        }
        return listing;
    }
}
