package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 14.10.2015.
 */
public class EditableCommentListing extends PostListing {

    private Event event;

    /**
     * INTERNAL USE ONLY: use instead event.getListing()
     *
     * @param listingSource
     */
    public EditableCommentListing(ListingSource listingSource) {
        super(listingSource);
        if (listingSource instanceof Event) {
            event = (Event) getListingSource();
        } else {
            //TODO: neuer Exceptiontyp und fehlermeldung erstellen. ExceptionHandler notwendig.
        }
    }

    public Event getEvent() {
        return event;
    }

    public Comment createComment(String message) {
        long ID = 1; //TODO: getID from server
        User user = AIXLoginModule.getInstance().getLoggedInUser();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Comment comment = new Comment(ID, message, now, 0, user, false, event);
        //TODO: event modification?
        this.addPost(comment);
        //TODO: Add Post to database
        return comment;
    }

    public void deleteComment(Comment comment) {
        if (comment.getEvent().equals(event)) {
            this.removePost(comment);
            comment.rawDelete();
            //TODO: commit deletion to database
        }
    }
}
