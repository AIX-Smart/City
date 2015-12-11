package com.aix.city.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.core.data.Post;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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

    public static final String OBSERVER_KEY_CHANGED_DATASET = "dataSet";
    public static final String OBSERVER_KEY_CHANGED_EDITABILITY = "editabilty";
    public static final String OBSERVER_KEY_CHANGED_LIKESTATUS = "likeStatus";

    private List<Post> posts  = new ArrayList<Post>();
    private ListingSource listingSource;
    private boolean finished = false;

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
        finished = (in.readInt() != 0);
    }

    public ListingSource getListingSource() {
        return listingSource;
    }

    /**
     * INTERNAL USE ONLY: use location.createPostListing().createEvent(...) or event.createPostListing().createComment(...) instead
     * @param posts
     */
    public void addPosts(Post[] posts){
        getPosts().addAll(Arrays.asList(posts));
        for(Post post : posts){
            post.addObserver(this);
        }
        setChanged();
        notifyObservers(OBSERVER_KEY_CHANGED_DATASET);
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
        return finished;
    }

    public void loadMorePosts(final int postNum) {

        Post lastPost = null;
        if(!posts.isEmpty()){
            lastPost = posts.get(posts.size() - 1);
        }

        Response.Listener<Post[]> listener = new Response.Listener<Post[]>(){
            @Override
            public void onResponse(Post[] response) {
                if(response.length < postNum){
                    finished = true;
                }
                addPosts(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:
            }
        };

        //send request to server
        listingSource.requestPosts(listener, errorListener, postNum, lastPost);
    }

    public void loadMorePosts() {
        loadMorePosts(POST_REQUEST_NUM);
    }

    public void refresh() {
        posts.clear();
        loadMorePosts();
    }

    /**
     *  creates a new Post in this listing and sends it to the server.
        Does nothing if posts cannot be created in this context.
     * @param content content/message of the created Post
     * @return returns true if the post was successfully created
     */
    public boolean createPost(String content){
        return false;
    }

    public boolean deletePost(Post post){
        return false;
    }

    public boolean isEditable(){
        return false;
    }

    @Override
    public void update(Observable observable, Object data) {
        setChanged();
        notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
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
