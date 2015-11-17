package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.comm.LocationEventRequest;
import com.aix.city.core.AIxDataManager;
import com.aix.city.core.EditableEventListing;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.android.volley.Request;
import com.android.volley.Response;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Location implements ListingSource {

    private int id;
    private String name;

    //no-argument constructor for JSON
    private Location(){}

    //Parcelable constructor
    public Location(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
    }

    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public LocationData getData() {
        return AIxDataManager.getInstance().getLocationData(id);
    }


    @Override
    public EditableEventListing createPostListing() {
        return new EditableEventListing(this);
    }

    @Override
    public Request createRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost) {
        return new LocationEventRequest(listener, errorListener, ignoreCache, postNum, lastPost, this);
    }

    @Override
    public ListingSourceType getType() {
        return ListingSourceType.LOCATION;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return id == location.id;

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
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Location> CREATOR =
            new Parcelable.Creator<Location>(){

                @Override
                public Location createFromParcel(Parcel source) {
                    return new Location(source);
                }

                @Override
                public Location[] newArray(int size) {
                    return new Location[size];
                }
            };
}
