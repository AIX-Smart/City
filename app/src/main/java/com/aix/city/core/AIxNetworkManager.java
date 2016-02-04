package com.aix.city.core;

import android.content.Context;
import android.support.annotation.Nullable;

import com.aix.city.comm.DeletePostRequest;
import com.aix.city.comm.GetCityLocationsRequest;
import com.aix.city.comm.GetLikeCountRequest;
import com.aix.city.comm.GetLikeStatusRequest;
import com.aix.city.comm.GetLocationRequest;
import com.aix.city.comm.GetPostsRequest;
import com.aix.city.comm.GetTagsRequest;
import com.aix.city.comm.IsUpToDateRequest;
import com.aix.city.comm.PutLikeRequest;
import com.aix.city.comm.LoginRequest;
import com.aix.city.comm.OkHttpStack;
import com.aix.city.comm.PostCreationRequest;
import com.aix.city.comm.URLFactory;
import com.aix.city.core.data.City;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;
import com.aix.city.core.data.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

//import com.android.volley.toolbox.ImageLoader;


/**
 * Created by Thomas on 31.10.2015.
 */
public class AIxNetworkManager {

    public static final String TAG_GET_LIKE_COUNT = "GetLikeCountRequest";
    public static final String TAG_GET_LIKE_STATUS = "GetLikeStatusRequest";
    public static final String TAG_PUT_LIKE_CHANGE = "PutLikeRequest";
    public static final String TAG_GET_POSTS = "GetPostsRequest";
    public static final String TAG_UP_TO_DATE = "IsUpToDateRequest";

    public static final Response.ErrorListener DEFAULT_ERROR_LISTENER = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // do nothing
        }
    };

    private static AIxNetworkManager instance;
    private final Context context;
    private RequestQueue requestQueue;


    //Singleton methods and constructor
    private AIxNetworkManager(Context context) {
        this.context = context;
    }

    public static synchronized void createInstance(Context context){
        if(instance == null){
            instance = new AIxNetworkManager(context);
            //instance.start();
        }
    }

    public static AIxNetworkManager getInstance() {
        return instance;
    }
    //

    public void start(){
        getRequestQueue().start();
    }

    protected RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext(), new OkHttpStack(new OkHttpClient()));
        }

        return requestQueue;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Adds a request to the Volley request queue with a given tag
     *
     * @param request is the request to be added
     * @param tag is the tag identifying the request
     */
    public void addRequest(Request<?> request, String tag) {
        request.setTag(tag);
        addRequest(request);
    }

    /**
     * Adds a request to the Volley request queue
     *
     * @param request is the request to add to the Volley queue
     */
    public void addRequest(Request<?> request) {
        getRequestQueue().add(request);
    }

    /**
     * Cancels all the request in the Volley queue for a given tag
     *
     * @param tag associated with the Volley requests to be cancelled
     */
    public void cancelAllRequests(String tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    public void clearCache(){
        getRequestQueue().getCache().clear();
    }

    public void clearCityLocationsCache(City city){
        getRequestQueue().getCache().remove(URLFactory.get().createGetCityLocationsURL(city));
    }

    public void clearTagsCache(){
        getRequestQueue().getCache().remove(URLFactory.get().createGetAllTagsURL());
    }

    public Request requestPosts(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost, ListingSource listingSource, PostListing.Order order){
        GetPostsRequest request = new GetPostsRequest(listener, errorListener, postNum, lastPost, listingSource, order);
        addRequest(request, TAG_GET_POSTS);
        return request;
    }

    public Request requestPostCreation(Response.Listener<Post> listener, Response.ErrorListener errorListener, EditableListing postListing, String content){
        PostCreationRequest request = new PostCreationRequest(listener, errorListener, postListing, content);
        addRequest(request);
        return request;
    }

    public Request requestLikeChange(Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Likeable likeable, boolean liked){
        PutLikeRequest request = new PutLikeRequest(listener, errorListener, likeable, liked);
        addRequest(request, TAG_PUT_LIKE_CHANGE);
        return request;
    }

    public Request requestLogin(Response.Listener<User> listener, Response.ErrorListener errorListener, String deviceId){
        LoginRequest request = new LoginRequest(listener, errorListener, deviceId);
        addRequest(request);
        return request;
    }

    public Request requestTags(Response.Listener<Tag[]> listener, Response.ErrorListener errorListener){
        GetTagsRequest request = new GetTagsRequest(listener, errorListener);
        addRequest(request);
        return request;
    }

    public Request requestCityLocations(Response.Listener<Location[]> listener, Response.ErrorListener errorListener, City city){
        GetCityLocationsRequest request = new GetCityLocationsRequest(listener, errorListener, city);
        addRequest(request);
        return request;
    }

    public Request requestLocation(Response.Listener<Location> listener, Response.ErrorListener errorListener, int locationId){
        GetLocationRequest request = new GetLocationRequest(listener, errorListener, locationId);
        addRequest(request);
        return request;
    }

    public Request requestPostDeletion(Response.Listener<Post> listener, Response.ErrorListener errorListener, Post post) {
        DeletePostRequest request = new DeletePostRequest(listener, errorListener, post);
        addRequest(request);
        return request;
    }

    @Nullable
    public Request requestIsUpToDate(Response.Listener<Boolean> listener, PostListing postListing) {
        IsUpToDateRequest request = null;
        final Post newestPost = postListing.getNewestPost();
        if (newestPost == null){
            listener.onResponse(false);
        }
        else{
            request = new IsUpToDateRequest(listener, DEFAULT_ERROR_LISTENER, newestPost, postListing.getListingSource());
            addRequest(request, TAG_UP_TO_DATE);
        }
        return request;
    }

    public Request requestLikeStatus(Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Likeable likeable){
        GetLikeStatusRequest request = new GetLikeStatusRequest(listener, errorListener, likeable);
        addRequest(request, TAG_GET_LIKE_STATUS);
        return request;
    }

    public Request requestLikeCount(Response.Listener<Integer> listener, Response.ErrorListener errorListener, Likeable likeable){
        GetLikeCountRequest request = new GetLikeCountRequest(listener, errorListener, likeable);
        addRequest(request, TAG_GET_LIKE_COUNT);
        return request;
    }
}
