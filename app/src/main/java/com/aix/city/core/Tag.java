package com.aix.city.core;

import com.aix.city.comm.TagEventRequest;
import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Tag implements ListingSource {

    private int ID;
    private String name;
    private transient PostListing listing;

    //no-argument constructor for JSON
    private Tag(){}

    public Tag(int ID, String name) {
        this.ID = ID;
        if(name == null) this.name = "";
        else this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    @Override
    public Request getRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost) {
        return new TagEventRequest(listener, errorListener, ignoreCache, postNum, lastPost, this);
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
        if (!super.equals(o)) return false;

        Tag tag = (Tag) o;

        return name.equals(tag.name);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
