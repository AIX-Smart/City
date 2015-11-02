package com.aix.city.core;

import com.android.internal.util.Predicate;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Thomas on 11.10.2015.
 */
public class PostListing extends Observable {

    /** Defines the default number of posts for a database GET-request */
    private static int postRequestCount = 20;

    private List<Post> allStoredPosts  = new ArrayList<Post>();
    private List<Post> posts  = new ArrayList<Post>();
    private ListingSource listingSource;
    private Request request;
    private Predicate<Post> filter;
    private boolean finished;

    /**
     * INTERNAL USE ONLY: use instead listingSource.getPostListing()
     *
     * @param listingSource
     */
    public PostListing(ListingSource listingSource) {
        this.listingSource = listingSource;
        request = listingSource.getRequest();
        filter = listingSource.getFilter();
        finished = false;
    }

    public Request getRequest() {
        return request;
    }

    public Predicate<Post> getFilter() {
        return filter;
    }

    public ListingSource getListingSource() {
        return listingSource;
    }

    /**
     * INTERNAL USE ONLY: use location.getPostListing().createEvent(...) or event.getPostListing().createComment(...) instead
     * @param post
     */
    public void addPost(Post post){
        posts.add(post);
        notifyObservers();
    }

    /**
     * INTERNAL USE ONLY: use location.getPostListing().deleteEvent(...) or event.getPostListing().deleteComment(...) instead
     * @param post
     */
    public void removePost(Post post){
        posts.remove(post);
        notifyObservers();
    }

    /**
     *
     * @return returns a list of posts
     */
    public List<Post> getPosts() {
        return posts;
    }

    public void loadMorePosts(int postCount) {
        List<Post> newPosts = new ArrayList<Post>(); //TODO load posts from database
        if (newPosts.isEmpty()) {
            finished = true;
        }
        posts.addAll(newPosts);

        notifyObservers();
    }

    public void loadMorePosts() {
        loadMorePosts(postRequestCount);
    }

    public void refresh() {
        //TODO:
        notifyObservers();
    }
}
