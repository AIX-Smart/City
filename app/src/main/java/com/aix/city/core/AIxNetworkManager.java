package com.aix.city.core;

import android.content.Context;

import com.aix.city.comm.GetCityLocationsRequest;
import com.aix.city.comm.GetLocationRequest;
import com.aix.city.comm.GetPostsRequest;
import com.aix.city.comm.GetTagsRequest;
import com.aix.city.comm.PutLikeRequest;
import com.aix.city.comm.LoginRequest;
import com.aix.city.comm.OkHttpStack;
import com.aix.city.comm.PostCreationRequest;
import com.aix.city.core.data.City;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;
import com.aix.city.core.data.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

//import com.android.volley.toolbox.ImageLoader;


/**
 * Created by Thomas on 31.10.2015.
 */
public class AIxNetworkManager {

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
            //instance.init();
        }
    }

    public static AIxNetworkManager getInstance() {
        return instance;
    }
    //

    public void init(){
        getRequestQueue().start();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));
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
        if (getRequestQueue() != null) {
            getRequestQueue().cancelAll(tag);
        }
    }

    public void requestPosts(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost, ListingSource listingSource){
        GetPostsRequest request = new GetPostsRequest(listener, errorListener, postNum, lastPost, listingSource);
        addRequest(request);
    }

    public void requestPostCreation(Response.Listener<String> listener, Response.ErrorListener errorListener, EditableListing postListing, String content){
        PostCreationRequest request = new PostCreationRequest(listener, errorListener, postListing, content);
        addRequest(request);
    }

    public void requestLikeChange(Response.Listener<String> listener, Response.ErrorListener errorListener, Likeable likeable, boolean liked){
        PutLikeRequest request = new PutLikeRequest(listener, errorListener, likeable, liked);
        addRequest(request);
    }

    public void requestLogin(Response.Listener<User> listener, Response.ErrorListener errorListener, String deviceId){
        LoginRequest request = new LoginRequest(listener, errorListener, deviceId);
        addRequest(request);
    }

    public void requestTags(Response.Listener<Tag[]> listener, Response.ErrorListener errorListener){
        GetTagsRequest request = new GetTagsRequest(listener, errorListener);
        addRequest(request);
    }

    public void requestCityLocations(Response.Listener<Location[]> listener, Response.ErrorListener errorListener, City city){
        GetCityLocationsRequest request = new GetCityLocationsRequest(listener, errorListener, city);
        addRequest(request);
    }

    public void requestLocation(Response.Listener<Location> listener, Response.ErrorListener errorListener, int locationId){
        GetLocationRequest request = new GetLocationRequest(listener, errorListener, locationId);
        addRequest(request);
    }
}
