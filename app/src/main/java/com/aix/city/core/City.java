package com.aix.city.core;

import com.android.internal.util.Predicate;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class City implements ListingSource {

    private String name;
    private long cityID;
    private Listing listing;

    public City(String name, long cityID) {
        this.name = name;
        this.cityID = cityID;
    }

    public String getName() {
        return name;
    }

    public long getCityID() {
        return cityID;
    }

    public CityData getCityData() {
        return null;
    }

    @Override
    public DatabaseRequest getDatabaseRequest() {
        return null;
    }

    @Override
    public Predicate<Post> getFilter() {
        return null;
    }

    @Override
    public Listing getListing() {
        if (listing == null) {
            listing = new Listing(this);
        }
        return listing;
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
