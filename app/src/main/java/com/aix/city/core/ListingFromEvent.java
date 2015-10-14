package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 14.10.2015.
 */
public class ListingFromEvent extends Listing {

    private Event event;

    /**
     * INTERNAL USE ONLY: use instead event.getListing()
     *
     * @param listingSource
     */
    public ListingFromEvent(ListingSource listingSource) {
        super(listingSource);
        if (listingSource instanceof Event) {
            event = (Event) getListingSource();
        } else {
            //TODO: neuer Exceptiontyp und fehlermeldung erstellen. Wir brauchen auch noch einen ExceptionHandler.
        }
    }

    public Event getEvent() {
        return event;
    }

    public Comment createComment(String message) {
        long ID = 1; //TODO: getID from server
        User user = AIXLoginModule.getInstance().getLoggedInUser();
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        Comment comment = new Comment(ID, message, now, 0, user, false, event);
        //TODO: event modification?
        getPosts().add(0, comment);
        //TODO: Add Post to database
        return comment;
    }

    public void removeComment(Comment comment) {
        if (comment.getEvent().equals(this)) {
            getPosts().remove(comment);
            //TODO: event modification?
            comment.rawDelete();
            //TODO: commit deletion to database
        }
    }
}
