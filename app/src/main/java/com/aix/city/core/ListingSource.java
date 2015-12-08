package com.aix.city.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.comm.AIxJsonRequest;
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

    int PARCEL_DESCRIPTION_EVENT = Post.PARCEL_DESCRIPTION_EVENT;
    int PARCEL_DESCRIPTION_CITY = PARCEL_DESCRIPTION_EVENT + 1;
    int PARCEL_DESCRIPTION_LOCATION = PARCEL_DESCRIPTION_CITY + 1;
    int PARCEL_DESCRIPTION_TAG = PARCEL_DESCRIPTION_LOCATION + 1;

    @JsonIgnore
    void requestPosts(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost);

    @JsonIgnore
    PostListing createPostListing();

    @JsonIgnore
    ListingSourceType getType();

    Parcelable.Creator<ListingSource> CREATOR =
            new Parcelable.Creator<ListingSource>(){

                @Override
                public ListingSource createFromParcel(Parcel source) {
                    int classDescription = source.readInt();
                    switch(classDescription){
                        case PARCEL_DESCRIPTION_EVENT:
                            return new Event(source);
                        case PARCEL_DESCRIPTION_CITY:
                            return new City(source);
                        case PARCEL_DESCRIPTION_LOCATION:
                            return new Location(source);
                        case PARCEL_DESCRIPTION_TAG:
                            return new Tag(source);
                    }
                    return null;
                }

                @Override
                public ListingSource[] newArray(int size) {
                    return new ListingSource[size];
                }
            };

}
