package com.aix.city.comm;

import com.aix.city.core.AIXLoginModule;
import com.aix.city.core.Event;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 02.11.2015.
 */
public class LocationEventRequest extends JacksonRequest<Event> {

    private int postNum;
    private Event lastPost;


    public LocationEventRequest(Response.Listener<Event> listener, Response.ErrorListener errorListener, int postNum){
        this(listener, errorListener, postNum, null);
    }

    public LocationEventRequest(Response.Listener<Event> listener, Response.ErrorListener errorListener, int postNum, Event lastPost){
        this(listener, errorListener, postNum, lastPost, false);
    }

    public LocationEventRequest(Response.Listener<Event> listener, Response.ErrorListener errorListener, int postNum, Event lastPost, boolean ignoreCache){
        super(Request.Method.GET, getURL(postNum, lastPost), null, Event.class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
    }


    public static String getURL(int postNum, Event lastPost){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getUrl().newBuilder()
                .addPathSegment(URLSegments.LOCATION)
                .addPathSegment(AIXLoginModule.getInstance().getLoggedInUser().toString())
                .addPathSegment(String.valueOf(postNum));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getID()));
        }
        return urlBuilder.build().toString();
    }

    public int getPostNum() {
        return postNum;
    }

    public Event getLastPost() {
        return lastPost;
    }

}
