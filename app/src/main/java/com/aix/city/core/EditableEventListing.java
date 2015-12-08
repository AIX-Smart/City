package com.aix.city.core;

import android.os.Parcel;

import com.aix.city.comm.AIxJsonRequest;
import com.aix.city.comm.PostCreationRequest;
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
     * @param listingSource listingSource must be an instance of Location
     */
    public EditableEventListing(ListingSource listingSource) {
        super(listingSource);
        location = (Location) getListingSource();
    }

    public EditableEventListing(Parcel in){
        super(in);
        location = (Location) getListingSource();
    }

    public Location getLocation() {
        return location;
    }


    /*
    public Event createEvent(String content) {
        int id = 0;
        User user = AIxLoginModule.getInstance().getLoggedInUser();
        long now = System.currentTimeMillis();
        Event event = new Event(Post.LOCALE_ID, content, now, 0, user.getId(), false, location, 0);
        this.addPost(event);

        //Add Post to database
        AIxJsonRequest request = new PostCreationRequest(this, content);
        AIxNetworkManager.getInstance().addRequest(request);
        return event;
    }*/

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

    /*@Override
    public boolean createPost(String content) {
        return createEvent(content) != null;
    }*/

    @Override
    public boolean deletePost(Post post) {
        if(post instanceof Event){
            return deleteEvent((Event)post);
        }
        else{
            return false;
        }
    }

    @Override
    public int describeContents() {
        return PostListing.PARCEL_DESCRIPTION_EDITABLE_EVENT_LISTING;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
