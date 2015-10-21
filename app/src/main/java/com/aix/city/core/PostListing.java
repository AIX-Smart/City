package com.aix.city.core;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class PostListing /*extends Observable*/ {

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
    public PostListing(ListingSource listingSource) {
        this.listingSource = listingSource;
        posts = new ArrayList<Post>();
        request = listingSource.getDatabaseRequest();
        filter = listingSource.getFilter();
        finished = false;
    }

    public DatabaseRequest getRequest() {
        return request;
    }

    public Predicate<Post> getFilter() {
        return filter;
    }

    public ListingSource getListingSource() {
        return listingSource;
    }

    /**
     * INTERNAL USE ONLY: use location.getListing().createEvent(...) or event.getListing().createComment(...) instead
     * @param post
     */
    public void addPost(Post post){
        posts.add(post);
    }

    /**
     * INTERNAL USE ONLY: use location.getListing().deleteEvent(...) or event.getListing().deleteComment(...) instead
     * @param post
     */
    public void removePost(Post post){
        posts.remove(post);
    }

    public Post[] getStoredPosts() {
        return posts.toArray(new Post[posts.size()]);
    }

    public Post[] getNextPosts(int postCount) {
        List<Post> newPosts = new ArrayList<Post>(); //TODO load posts from database
        if (newPosts.isEmpty()) {
            finished = true;
        }
        posts.addAll(newPosts);
        return newPosts.toArray(new Post[newPosts.size()]);
    }

    //TODO:
    public Post[] getNextPosts() {
        return getNextPosts(20);
    }

    //TODO:
    public void refresh() {
        return;
    }
}
