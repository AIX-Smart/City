package com.aix.city.core;

import com.aix.city.comm.CityEventRequest;
import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 11.10.2015.
 */
public class City implements ListingSource {

    private String name;
    private long cityID;
    private transient PostListing listing;

    //no-argument constructor for JSON
    private City(){}

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
        return AIxDataManager.getInstance().getCityData(this);
    }

    @Override
    public Request getRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost) {
        return new CityEventRequest(listener, errorListener, ignoreCache, postNum, lastPost, this);
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
