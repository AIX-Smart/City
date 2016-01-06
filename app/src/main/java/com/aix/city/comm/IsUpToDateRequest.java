package com.aix.city.comm;

import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Post;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 03.01.2016.
 */
public class IsUpToDateRequest extends AIxJsonRequest<Boolean> {

    private final Post newestPost;
    private final ListingSource listingSource;

    public IsUpToDateRequest(Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Post newestPost, ListingSource listingSource){
        super(Request.Method.GET, URLFactory.get().createUpToDateURL(newestPost, listingSource), null, Boolean.class, listener, errorListener, false);
        this.newestPost = newestPost;
        this.listingSource = listingSource;
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    public ListingSource getListingSource() {
        return listingSource;
    }

    public Post getNewestPost() {
        return newestPost;
    }

}
