package com.aix.city.core;

import android.os.Parcelable;

import com.aix.city.comm.AIxJacksonRequest;
import com.aix.city.core.data.Post;
import com.android.volley.Response;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Thomas on 11.10.2015.
 */
public interface ListingSource extends Parcelable {

    @JsonIgnore
    AIxJacksonRequest createRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost);

    @JsonIgnore
    PostListing createPostListing();

    @JsonIgnore
    ListingSourceType getType();

}
