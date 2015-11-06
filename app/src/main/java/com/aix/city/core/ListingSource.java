package com.aix.city.core;

import android.os.Parcelable;

import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Thomas on 11.10.2015.
 */
public interface ListingSource extends Parcelable {

    @JsonIgnore
    Request getRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost);

    @JsonIgnore
    Predicate<Post> getFilter();

    @JsonIgnore
    PostListing getPostListing();

    @JsonIgnore
    ListingSourceType getType();

}
