package com.aix.city.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.core.data.City;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;
import com.android.volley.Response;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Thomas on 11.10.2015.
 */
public interface ListingSource extends Parcelable {

    @JsonIgnore
    void requestPosts(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost);

    @JsonIgnore
    PostListing createPostListing();

    @JsonIgnore
    ListingSourceType getType();

    int getId();

    Parcelable.Creator<ListingSource> CREATOR =
            new Parcelable.Creator<ListingSource>(){

                @Override
                public ListingSource createFromParcel(Parcel source) {
                    return ListingSourceType.createListingSource(source);
                }

                @Override
                public ListingSource[] newArray(int size) {
                    return new ListingSource[size];
                }
            };

}
