package com.aix.city.core.data;

import android.location.*;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.aix.city.comm.GetPostsRequest;
import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.aix.city.core.PostListing;
import com.android.volley.Response;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.10.2015.
 */
public class City implements ListingSource {

    private int id;
    private String name;

    //no-argument constructor for JSON
    private City(){}

    //Parcelable constructor
    public City(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
    }

    public City(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        if(name == null){
            name = "";
        }
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public void requestPosts(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost) {
        AIxNetworkManager.getInstance().requestPosts(listener, errorListener, postNum, lastPost, this);
    }

    @Override
    public PostListing createPostListing() {
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
        return 43*id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
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
