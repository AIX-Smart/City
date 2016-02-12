package com.aix.city.core;

import android.os.Parcel;
import android.os.Parcelable;

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


    @Override
    public int describeContents() {
        return PostListing.PARCEL_DESCRIPTION_EDITABLE_EVENT_LISTING;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Parcelable.Creator<EditableEventListing> CREATOR =
            new Parcelable.Creator<EditableEventListing>(){

                @Override
                public EditableEventListing createFromParcel(Parcel source) {
                    return new EditableEventListing(source);
                }

                @Override
                public EditableEventListing[] newArray(int size) {
                    return new EditableEventListing[size];
                }
            };
}
