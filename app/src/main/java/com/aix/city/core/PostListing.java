package com.aix.city.core;

import com.aix.city.core.data.Post;
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
    private int postRequestNum = 1;

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
     * @param posts
     */
    public void addPosts(Post[] posts){
        getPosts().addAll(Arrays.asList(posts));
        setChanged();
        notifyObservers();
    }

    public void addPost(Post post){
        getPosts().add(post);
        setChanged();
        notifyObservers();
    }

    /**
     * INTERNAL USE ONLY: use location.getPostListing().deleteEvent(...) or event.getPostListing().deleteComment(...) instead
     * @param post
     */
    public void removePost(Post post){
        posts.remove(post);
        setChanged();
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
                addPosts(response);
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
    }

    public void loadMorePosts() {
        loadMorePosts(postRequestNum);
    }

    public void refresh() {
        //TODO:
        notifyObservers();
    }
}
