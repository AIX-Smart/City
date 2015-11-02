package com.aix.city.comm;

import android.content.Context;

import com.aix.city.core.AIXLoginModule;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;


/**
 * Created by Thomas on 31.10.2015.
 */
public class AIxNetworkManager {

    private final String SCHEME = "https";
    private final String HOST = "www.citevent.de";
    private final int PORT = 8080;

    private static AIxNetworkManager instance;
    private final Context context;
    private RequestQueue requestQueue;


    //Singleton methods and constructor
    private AIxNetworkManager(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }
    public static synchronized void initInstance(Context context){
        if(instance == null){
            instance = new AIxNetworkManager(context);
        }
    }
    public static AIxNetworkManager getInstance() {
        return instance;
    }
    //

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));
        }

        return requestQueue;
    }

    public String getSCHEME() {
        return SCHEME;
    }

    public String getHOST() {
        return HOST;
    }

    public int getPORT() {
        return PORT;
    }

    public Context getContext() {
        return context;
    }

    public HttpUrl.Builder getUrlBuilder(){
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
        urlBuilder.scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .username(AIXLoginModule.getInstance().getLoggedInUser().toString())
                .addPathSegment(URLNames.SERVICE_SEGMENT);
        return urlBuilder;
    }

    /**
     * Adds a request to the Volley request queue with a given tag
     *
     * @param request is the request to be added
     * @param tag is the tag identifying the request
     */
    public void addRequest(Request<?> request, String tag)
    {
        request.setTag(tag);
        addRequest(request);
    }

    /**
     * Adds a request to the Volley request queue
     *
     * @param request is the request to add to the Volley queue
     */
    public void addRequest(Request<?> request)
    {
        getRequestQueue().add(request);
    }

    /**
     * Cancels all the request in the Volley queue for a given tag
     *
     * @param tag associated with the Volley requests to be cancelled
     */
    public void cancelAllRequests(String tag)
    {
        if (getRequestQueue() != null)
        {
            getRequestQueue().cancelAll(tag);
        }
    }

}