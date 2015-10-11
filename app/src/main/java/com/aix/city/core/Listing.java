package com.aix.city.core;

import com.android.internal.util.Predicate;

import java.util.List;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Listing {

    private List<Post> posts;
    private ListingSource listingSource;
    private DatabaseRequest request;
    private Predicate<Post> filter;

    public List<Post> getPosts() {
        return posts;
    }

    public ListingSource getListingSource() {
        return listingSource;
    }

    public DatabaseRequest getRequest() {
        return request;
    }

    public Predicate<Post> getFilter() {
        return filter;
    }

    public List<Post> getNextPosts(int postCount) {
        return posts;
    }

    public void refresh() {
        return;
    }

}
