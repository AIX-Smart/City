package com.aix.city.core;

import com.android.internal.util.Predicate;

/**
 * Created by Thomas on 11.10.2015.
 */
public class City implements ListingSource {

    private String name;
    private long cityID;
    private PostListing listing;

    public City(long cityID, String name) {
        this.name = name;
        this.cityID = cityID;
    }

    public String getName() {
        return name;
    }

    public long getID() {
        return cityID;
    }

    public CityData getData() {
        return DataManager.getInstance().getCityData(this);
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
    public PostListing getPostListing() {
        if (listing == null) {
            listing = new PostListing(this);
        }
        return listing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return cityID == city.cityID;

    }

    @Override
    public int hashCode() {
        return (int) (cityID ^ (cityID >>> 32));
    }
}
