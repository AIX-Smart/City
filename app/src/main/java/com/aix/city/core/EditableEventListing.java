package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 14.10.2015.
 */
public class EditableEventListing extends PostListing {

    private Location location;

    /**
     * INTERNAL USE ONLY: use instead location.getListing()
     *
     * @param listingSource
     */
    public EditableEventListing(ListingSource listingSource) {
        super(listingSource);
        if (listingSource instanceof Location) {
            location = (Location) getListingSource();
        } else {
            //TODO: neuer Exceptiontyp und fehlermeldung erstellen. ExceptionHandler notwendig.
        }
    }

    public Location getLocation() {
        return location;
    }


    public Event createEvent(String message) {
        long ID = 0; //TODO: getID from server
        User user = AIXLoginModule.getInstance().getLoggedInUser();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Event event = new Event(ID, message, now, 0, user, false, location, 0, false);
        this.addPost(event);
        //TODO: Add Post to database
        return event;
    }

    public void deleteEvent(Event event) {
        if (event.getLocation().equals(location)) {
            this.removePost(event);
            event.rawDelete();
            //TODO: commit deletion to database
        }
    }
}
