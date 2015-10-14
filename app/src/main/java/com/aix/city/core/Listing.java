package com.aix.city.core;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Listing /*extends Observable*/ {

    private List<Post> posts;
    private ListingSource listingSource;
    private DatabaseRequest request;
    private Predicate<Post> filter;
    private boolean finished;

    /**
     * INTERNAL USE ONLY: use instead listingSource.getListing()
     *
     * @param listingSource
     */
    public Listing(ListingSource listingSource) {
        this.listingSource = listingSource;
        posts = new ArrayList<Post>();
        request = listingSource.getDatabaseRequest();
        filter = listingSource.getFilter();
        finished = false;
    }

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
        List<Post> newPosts = new ArrayList<Post>(); //TODO load posts from database
        if (newPosts.isEmpty()) {
            finished = true;
        }
        posts.addAll(newPosts);
        return newPosts;
    }

    //TODO:
    public List<Post> getNextPosts() {
        return getNextPosts(20);
    }

    //TODO:
    public void refresh() {
        return;
    }
}
