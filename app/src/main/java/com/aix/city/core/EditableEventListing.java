package com.aix.city.core;

import com.aix.city.comm.AIxJacksonRequest;
import com.aix.city.comm.CreateEventRequest;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.User;

/**
 * Created by Thomas on 14.10.2015.
 */
public class EditableEventListing extends EditableListing {

    private Location location;

    /**
     * INTERNAL USE ONLY: use instead location.createPostListing()
     *
     * @param listingSource
     */
    public EditableEventListing(ListingSource listingSource) {
        super(listingSource);
        if (listingSource instanceof Location) {
            location = (Location) getListingSource();
        }
    }

    public Location getLocation() {
        return location;
    }


    public Event createEvent(String content) {
        int ID = 0; //TODO: getId from server
        User user = AIxLoginModule.getInstance().getLoggedInUser();
        long now = System.currentTimeMillis();
        Event event = new Event(ID, content, now, 0, user.getId(), false, location, 0);
        this.addPost(event);

        //Add Post to database
        AIxJacksonRequest request = new CreateEventRequest(this, content);
        AIxNetworkManager.getInstance().addRequest(request);
        return event;
    }

    public boolean deleteEvent(Event event) {
        if (event.getLocation().equals(location)) {
            this.removePost(event);
            event.rawDelete();
            //TODO: commit deletion to database
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean createPost(String content) {
        return createEvent(content) != null;
    }

    @Override
    public boolean deletePost(Post post) {
        if(post instanceof Event){
            return deleteEvent((Event)post);
        }
        else{
            return false;
        }
    }
}
