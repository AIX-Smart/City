package com.aix.city.core;

import android.os.Parcelable;

import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 11.10.2015.
 */
public interface ListingSource extends Parcelable {

    Request getRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost);

    Predicate<Post> getFilter();

    PostListing getPostListing();

    ListingSourceType getType();

}
