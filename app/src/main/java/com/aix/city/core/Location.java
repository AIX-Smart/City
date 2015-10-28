package com.aix.city.core;

import com.android.internal.util.Predicate;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Location implements ListingSource {

    private long locationID;
    private String locationName;
    private EditableEventListing listing;

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
        return DataManager.getInstance().getLocationData(this);
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
    public DatabaseRequest getDatabaseRequest() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        return locationID == location.locationID;

    }

    @Override
    public int hashCode() {
        return (int) (locationID ^ (locationID >>> 32));
    }
}
