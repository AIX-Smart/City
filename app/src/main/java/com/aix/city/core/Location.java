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
    private ListingFromLocation listing;

    public Location(long locationID, String locationName, City city) {
        this.locationID = locationID;
        this.locationName = locationName;
        this.city = city;
    }

    public long getID() {
        return locationID;
    }

    public String getName() {
        return locationName;
    }

    public City getCity() {
        return city;
    }

    public LocationData getData() {
        return null;
    }


    @Override
    public Predicate<Post> getFilter() {
        return null;
    }

    @Override
    public ListingFromLocation getListing() {
        if (listing == null) {
            listing = new ListingFromLocation(this);
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
