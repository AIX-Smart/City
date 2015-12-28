package com.aix.city.core;

import android.os.Parcel;

import com.aix.city.comm.AIxJsonRequest;
import com.aix.city.comm.PostCreationRequest;
import com.aix.city.core.data.Comment;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.User;

/**
 * Created by Thomas on 14.10.2015.
 */
public class EditableCommentListing extends EditableListing {

    private Event event;

    /**
     * INTERNAL USE ONLY: use instead event.createPostListing()
     *
     * @param listingSource listingSource must be an instance of Event
     */
    public EditableCommentListing(ListingSource listingSource) {
        super(listingSource);
        event = (Event) getListingSource();
    }

    public EditableCommentListing(Parcel in){
        super(in);
        event = (Event) getListingSource();
    }

    public Event getEvent() {
        return event;
    }


    @Override
    public int describeContents() {
        return PostListing.PARCEL_DESCRIPTION_EDITABLE_COMMENT_LISTING;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
