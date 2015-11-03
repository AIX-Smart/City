package com.aix.city.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.comm.CityEventRequest;
import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 11.10.2015.
 */
public class City implements ListingSource {

    private long id;
    private String name;

    //no-argument constructor for JSON
    private City(){}

    //Parcelable constructor
    public City(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
    }

    public City(long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getID() {
        return id;
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
        return new PostListing(this);
    }

    @Override
    public ListingSourceType getType() {
        return ListingSourceType.CITY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return id == city.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<City> CREATOR =
            new Parcelable.Creator<City>(){

                @Override
                public City createFromParcel(Parcel source) {
                    return new City(source);
                }

                @Override
                public City[] newArray(int size) {
                    return new City[size];
                }
            };
}
