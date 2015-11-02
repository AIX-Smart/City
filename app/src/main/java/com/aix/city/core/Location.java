package com.aix.city.core;

import com.aix.city.comm.LocationEventRequest;
import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Location implements ListingSource {

    private long locationID;
    private String locationName;
    private transient EditableEventListing listing;

    //no-argument constructor for JSON
    private Location(){}

    public Location(long locationID, String locationName) {
        this.locationID = locationID;
        this.locationName = locationName;
    }

    public long getID() {
        return locationID;
    }

    public String getName() {
        return locationName;
    }

    public LocationData getData() {
        return AIxDataManager.getInstance().getLocationData(this);
    }


    @Override
    public Predicate<Post> getFilter() {
        return null;
    }

    @Override
    public EditableEventListing getPostListing() {
        if (listing == null) {
            listing = new EditableEventListing(this);
        }
        return listing;
    }

    @Override
    public Request getRequest(Response.Listener<Event> listener, Response.ErrorListener errorListener, int postNum, Event lastPost, boolean ignoreCache) {
        return new LocationEventRequest(listener, errorListener, postNum, lastPost, ignoreCache);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return locationID == location.locationID;

    }

    @Override
    public int hashCode() {
        return (int) (locationID ^ (locationID >>> 32));
    }
}
