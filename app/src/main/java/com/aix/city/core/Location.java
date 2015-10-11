package com.aix.city.core;

import com.android.internal.util.Predicate;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Location implements ListingSource {

    private long locationID;
    private String locationName;
    private City city;

    public long getLocationID() {
        return locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public City getCity() {
        return city;
    }

    public LocationData getLocationData() {
        return null;
    }


    @Override
    public Predicate<Post> getFilter() {
        return null;
    }

    @Override
    public Listing getListing() {
        return null;
    }

    @Override
    public DatabaseRequest getDatabaseRequest() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
