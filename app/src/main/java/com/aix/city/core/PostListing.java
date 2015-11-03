package com.aix.city.core;

import com.aix.city.comm.AIxNetworkManager;
import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Created by Thomas on 11.10.2015.
 */
public class PostListing extends Observable {

    private List<Post> allStoredPosts  = new ArrayList<Post>();
    private List<Post> posts  = new ArrayList<Post>();
    private ListingSource listingSource;
    private boolean finished = false;

    /** Defines the default number of posts for a database GET-request */
    private int postRequestNum = 20;

    /**
     * INTERNAL USE ONLY: use instead listingSource.getPostListing()
     *
     * @param listingSource
     */
    public PostListing(ListingSource listingSource) {
        this.listingSource = listingSource;
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

    public void loadMorePosts(int postNum) {

        Post lastPost = null;
        if(!posts.isEmpty()){
            lastPost = posts.get(posts.size() - 1);
        }

        Response.Listener<Post[]> listener = new Response.Listener<Post[]>(){
            @Override
            public void onResponse(Post[] response) {
                getPosts().addAll(Arrays.asList(response));
                notifyObservers();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:
            }
        };

        Request<Post> request = listingSource.getRequest(listener, errorListener, false, postNum, lastPost);
        AIxNetworkManager.getInstance().addRequest(request);
        AIxNetworkManager.getInstance().getRequestQueue().start();
    }

    public void loadMorePosts() {
        loadMorePosts(postRequestNum);
    }

    public void refresh() {
        //TODO:
        notifyObservers();
    }
}
