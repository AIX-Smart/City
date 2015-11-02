package com.aix.city.core;

import com.android.internal.util.Predicate;
import com.android.volley.Request;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Group implements ListingSource {

    private GroupType type;

    public GroupType getGroupType() {
        return type;
    }

    public String getName(){
        return type.toString();
    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public Predicate<Post> getFilter() {
        return null;
    }

    @Override
    public PostListing getPostListing() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
