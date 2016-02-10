package com.aix.city.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.aix.city.comm.DeletePostRequest;
import com.aix.city.comm.GetCityLocationsRequest;
import com.aix.city.comm.GetIsAuthorizedRequest;
import com.aix.city.comm.GetLikeCountRequest;
import com.aix.city.comm.GetLikeStatusRequest;
import com.aix.city.comm.GetLocationRequest;
import com.aix.city.comm.GetPostsRequest;
import com.aix.city.comm.GetTagsRequest;
import com.aix.city.comm.GetIsUpToDateRequest;
import com.aix.city.comm.PutAuthenticateRequest;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

//import com.android.volley.toolbox.ImageLoader;


/**
 * Created by Thomas on 31.10.2015.
 */
public class AIxNetworkManager {

    public static final Response.ErrorListener DEFAULT_ERROR_LISTENER = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // do nothing
        }
    };

    private static AIxNetworkManager instance;
    private final Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


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

    public ImageLoader getImageLoader() {
        if (imageLoader == null){
            imageLoader = new ImageLoader(getRequestQueue(), new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(16);
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });
        }
        return imageLoader;
    }

    /**
     * Adds a request to the Volley request queue with a given tag
     *
     * @param request is the request to be added
     * @param tag is the tag identifying the request
     */
    public void addRequest(Request<?> request, Object tag) {
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
    public void cancelAllRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    public void clearCache(){
        getRequestQueue().getCache().clear();
    }

    public void invalidateCityLocationsCache(City city){
        getRequestQueue().getCache().invalidate(URLFactory.get().createGetCityLocationsURL(city), true);
    }

    public void invalidateTagsCache(){
        getRequestQueue().getCache().invalidate(URLFactory.get().createGetAllTagsURL(), true);
    }

    protected Request requestLogin(Object tag, Response.Listener<User> listener, Response.ErrorListener errorListener, String deviceId){
        LoginRequest request = new LoginRequest(listener, errorListener, deviceId);
        addRequest(request, tag);
        return request;
    }

    protected Request requestAuthentication(Object tag, Response.Listener<Boolean[]> listener, Response.ErrorListener errorListener, Location location, String mail, String password){
        PutAuthenticateRequest request = new PutAuthenticateRequest(listener, errorListener, location, mail, password);
        addRequest(request, tag);
        return request;
    }

    protected Request requestIsAuthorized(Object tag, Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Location location, String mail, String password){
        GetIsAuthorizedRequest request = new GetIsAuthorizedRequest(listener, errorListener, location);
        addRequest(request, tag);
        return request;
    }

    public Request requestPosts(Object tag, Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost, ListingSource listingSource, PostListing.Order order){
        GetPostsRequest request = new GetPostsRequest(listener, errorListener, postNum, lastPost, listingSource, order);
        addRequest(request, tag);
        return request;
    }

    public Request requestPostCreation(Object tag, Response.Listener<Post> listener, Response.ErrorListener errorListener, EditableListing postListing, String content){
        PostCreationRequest request = new PostCreationRequest(listener, errorListener, postListing, content);
        addRequest(request, tag);
        return request;
    }

    public Request requestLikeChange(Object tag, Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Likeable likeable, boolean liked){
        PutLikeRequest request = new PutLikeRequest(listener, errorListener, likeable, liked);
        addRequest(request, tag);
        return request;
    }

    public Request requestTags(Object tag, Response.Listener<Tag[]> listener, Response.ErrorListener errorListener){
        GetTagsRequest request = new GetTagsRequest(listener, errorListener);
        addRequest(request, tag);
        return request;
    }

    public Request requestCityLocations(Object tag, Response.Listener<Location[]> listener, Response.ErrorListener errorListener, City city){
        GetCityLocationsRequest request = new GetCityLocationsRequest(listener, errorListener, city);
        addRequest(request, tag);
        return request;
    }

    public Request requestLocation(Object tag, Response.Listener<Location> listener, Response.ErrorListener errorListener, int locationId){
        GetLocationRequest request = new GetLocationRequest(listener, errorListener, locationId);
        addRequest(request, tag);
        return request;
    }

    public Request requestPostDeletion(Object tag, Response.Listener<Post> listener, Response.ErrorListener errorListener, Post post) {
        DeletePostRequest request = new DeletePostRequest(listener, errorListener, post);
        addRequest(request, tag);
        return request;
    }

    @Nullable
    public Request requestIsUpToDate(Object tag, Response.Listener<Boolean> listener, PostListing postListing) {
        GetIsUpToDateRequest request = null;
        final Post newestPost = postListing.getNewestPost();
        if (newestPost == null){
            listener.onResponse(false);
        }
        else{
            request = new GetIsUpToDateRequest(listener, DEFAULT_ERROR_LISTENER, newestPost, postListing.getListingSource());
            addRequest(request, tag);
        }
        return request;
    }

    public Request requestLikeStatus(Object tag, Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Likeable likeable){
        GetLikeStatusRequest request = new GetLikeStatusRequest(listener, errorListener, likeable);
        addRequest(request, tag);
        return request;
    }

    public Request requestLikeCount(Object tag, Response.Listener<Integer> listener, Response.ErrorListener errorListener, Likeable likeable){
        GetLikeCountRequest request = new GetLikeCountRequest(listener, errorListener, likeable);
        addRequest(request, tag);
        return request;
    }
}
