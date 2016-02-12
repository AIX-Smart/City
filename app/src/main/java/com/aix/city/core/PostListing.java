package com.aix.city.core;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.core.data.Comment;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Thomas on 11.10.2015.
 */
public class PostListing extends Observable implements Observer, Parcelable {

    /** Defines the default number of posts for a database GET-request */
    public static final int POST_REQUEST_NUM = 20;

    public static final int PARCEL_DESCRIPTION_POST_LISTING = 0;
    public static final int PARCEL_DESCRIPTION_EDITABLE_EVENT_LISTING = 1;
    public static final int PARCEL_DESCRIPTION_EDITABLE_COMMENT_LISTING = 2;

    public static final String OBSERVER_KEY_CHANGED_DATASET = "PostListing.dataSet";
    public static final String OBSERVER_KEY_CHANGED_EDITABILITY = "PostListing.editabilty";
    public static final String OBSERVER_KEY_FINISHED = "PostListing.isFinished";
    public static final String OBSERVER_KEY_CONNECTION_ERROR = "PostListing.error";

    public final Handler requestRetryHandler = new Handler();
    public static final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //TODO:
        }
    };

    private List<Post> posts  = new ArrayList<Post>();
    private ListingSource listingSource;
    private boolean isFinished = false;
    private boolean isWaitingForInit = true;
    private boolean isLoadingNewerPosts = false;
    private boolean isLoadingPosts = false;
    private int postNum = POST_REQUEST_NUM;
    private Order order = Order.NEWEST_FIRST;

    public enum Order{
        NEWEST_FIRST,
        POPULAR_FIRST;
    }

    /**
     * INTERNAL USE ONLY: use instead listingSource.createPostListing()
     *
     * @param listingSource
     */
    public PostListing(ListingSource listingSource) {
        this.listingSource = listingSource;
    }

    public PostListing(Parcel in){
        in.readTypedList(posts, Post.CREATOR);
        listingSource = in.readParcelable(ListingSource.class.getClassLoader());
        isFinished = (in.readInt() != 0);
        isWaitingForInit = (in.readInt() != 0);
    }

    public ListingSource getListingSource() {
        return listingSource;
    }



    public void addPosts(Post[] postArray){
        if (postArray.length > 0) {
            getPosts().addAll(Arrays.asList(postArray));
            for (Post post : postArray) {
                initPost(post);
            }
            setChanged();
            notifyObservers(OBSERVER_KEY_CHANGED_DATASET);
        }
    }

    //TODO: überpfüfung auf vollständigkeit lieber woanders machen, da sonst nicht verständlich
    public void addNewerPosts(Post[] responsePostArray){
        if (responsePostArray.length > 0) {
            final Post firstPost = getNewestPost();

            List<Post> newPostList = new ArrayList<Post>();
            for (Post post : responsePostArray){
                if (post.equals(firstPost)){
                    //post is a duplicate --> all following posts are also duplicates
                    break;
                }
                else {
                    //post is new
                    newPostList.add(post);
                    initPost(post);
                }
            }

            if (newPostList.size() != 0){
                if (newPostList.size() < responsePostArray.length){
                    posts.addAll(0, newPostList);
                    setChanged();
                    notifyObservers(OBSERVER_KEY_CHANGED_DATASET);
                }
                else{
                    //TODO: more new posts have to be loaded from the database
                    refresh();
                }
            }
        }
    }

    protected void initPost(Post post) {
        post.addObserver(this);
    }

    public void addPost(Post post){
        getPosts().add(post);
        post.addObserver(this);
        setChanged();
        notifyObservers(OBSERVER_KEY_CHANGED_DATASET);
    }

    /**
     * INTERNAL USE ONLY: use deletePost(Post post) instead
     * @param post
     */
    public void removePost(Post post){
        posts.remove(post);
        setChanged();
        notifyObservers(OBSERVER_KEY_CHANGED_DATASET);
    }

    /**
     *
     * @return returns a list of posts
     */
    public List<Post> getPosts() {
        return posts;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isWaitingForInit() {
        return isWaitingForInit;
    }

    public void setFinished() {
        if(!isFinished) {
            isFinished = true;
            setChanged();
            notifyObservers(OBSERVER_KEY_FINISHED);
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (this.order != order){
            this.order = order;
            refresh();
        }
    }

    public void loadInitialPosts() {
        if (isWaitingForInit){
            loadPosts();
        }
    }

    public boolean loadPosts() {
        if (!isLoadingPosts){
            isLoadingPosts = true;

            Post oldestPost = null;
            if(posts.isEmpty()){
                isWaitingForInit = false;
            }
            else{
                switch (order){
                    case NEWEST_FIRST:
                        oldestPost = posts.get(posts.size() - 1);
                        break;
                    case POPULAR_FIRST:
                        postNum = postNum * 4;
                        break;
                }
            }

            final Order oldOrder = order;
            Response.Listener<Post[]> listener = new Response.Listener<Post[]>(){
                @Override
                public void onResponse(Post[] response) {
                    if (order == oldOrder){
                        if(response.length < postNum){
                            setFinished();
                        }
                        addPosts(response);
                        isLoadingPosts = false;
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (order == oldOrder){
                        if (posts.isEmpty() && !isFinished()){
                            isWaitingForInit = true;
                        }
                        isLoadingPosts = false;
                        setChanged();
                        notifyObservers(OBSERVER_KEY_CONNECTION_ERROR);
                    }
                }
            };

            //send request to server
            AIxNetworkManager.getInstance().requestPosts(this, listener, errorListener, postNum, oldestPost, listingSource, order);
            return true;
        }
        return false;
    }

    public boolean loadNewerPosts() {
        if (!isEmpty() && !isLoadingNewerPosts) {
            isLoadingNewerPosts = true;

            final Order oldOrder = order;
            Response.Listener<Post[]> listener = new Response.Listener<Post[]>() {
                @Override
                public void onResponse(Post[] response) {
                    if (order == oldOrder) {
                        if (posts.size() != 0) {
                            addNewerPosts(response);
                        }
                        isLoadingNewerPosts = false;
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (order == oldOrder) {
                        isLoadingNewerPosts = false;
                    }
                }
            };

            //send request to server
            AIxNetworkManager.getInstance().requestPosts(this, listener, errorListener, postNum, null, listingSource, order);
            return true;
        }
        return false;
    }

    public void cancelRequests(){
        AIxNetworkManager.getInstance().cancelAllRequests(this);
    }

    public void clear() {
        cancelRequests();
        posts.clear();
        postNum = POST_REQUEST_NUM;
        isLoadingNewerPosts = false;
        isLoadingPosts = false;
        isWaitingForInit = true;
        isFinished = false;

        setChanged();
        notifyObservers(OBSERVER_KEY_CHANGED_DATASET);
    }

    public void refresh() {
        clear();
        loadInitialPosts();
    }

    /**
     *  creates a new Post in this listing and sends it to the server.
     Does nothing if posts cannot be created in this context.
     * @param content content/message of the created Post
     * @param successCommand will be executed if the creation was successful
     *@param errorCommand will be executed if an error occurred
     * @return returns true if postCreation is allowed in this context
     */
    public boolean createPost(String content, Runnable successCommand, Runnable errorCommand){
        return false;
    }

    /**
     *  sends a deletion request for a Post in this listing to the server.
     Does nothing if posts cannot be deleted in this context.
     * @param post post to be deleted
     * @param successCommand will be executed if the deletion was successful
     *@param errorCommand will be executed if an error occurred
     * @return returns true if the deletion is allowed in this context
     */
    public boolean deletePost(Post post, final Runnable successCommand, final Runnable errorCommand){
        if (post.isDeletionAllowed()){
            Response.Listener<Post> listener = new Response.Listener<Post>() {
                @Override
                public void onResponse(Post response) {
                    removePost(response);
                    successCommand.run();
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorCommand.run();
                }
            };
            //send request to server
            AIxNetworkManager.getInstance().requestPostDeletion(this, listener, errorListener, post);

            return true;
        }
        return false;
    }

    public boolean isEditable(){
        return false;
    }

    @JsonIgnore
    public Post getNewestPost(){
        if (posts.size() > 0){
            return posts.get(0);
        }
        return null;
    }

    @JsonIgnore
    public Post getOldestPost(){
        if (posts.size() > 0) {
            return posts.get(posts.size() - 1);
        }
        return null;
    }

    public boolean isEmpty() {
        return posts.isEmpty();
    }

    @Override
    public void update(Observable observable, Object data) {
        setChanged();
        notifyObservers(data);
    }

    @Override
    public int describeContents() {
        return PARCEL_DESCRIPTION_POST_LISTING;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.describeContents());
        dest.writeTypedList(getPosts());
        dest.writeParcelable(getListingSource(), flags);
        dest.writeInt(isFinished() ? 1 : 0);
        dest.writeInt(isWaitingForInit ? 1 : 0);
    }

    public static final Parcelable.Creator<PostListing> CREATOR =
            new Parcelable.Creator<PostListing>(){

                @Override
                public PostListing createFromParcel(Parcel source) {
                    int classDescription = source.readInt();
                    switch(classDescription){
                        case PARCEL_DESCRIPTION_POST_LISTING:
                            return new PostListing(source);
                        case PARCEL_DESCRIPTION_EDITABLE_EVENT_LISTING:
                            return new EditableEventListing(source);
                        case PARCEL_DESCRIPTION_EDITABLE_COMMENT_LISTING:
                            return new EditableCommentListing(source);
                    }
                    return null;
                }

                @Override
                public PostListing[] newArray(int size) {
                    return new PostListing[size];
                }
            };
}
