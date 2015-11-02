package com.aix.city.comm;

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


    public LocationEventRequest(int postNum, Response.Listener<Event> listener, Response.ErrorListener errorListener){
        this(postNum, listener, errorListener, null);
    }

    public LocationEventRequest(int postNum, Response.Listener<Event> listener, Response.ErrorListener errorListener, Event lastPost){
        super(Request.Method.GET, getURL(postNum, lastPost), null, Event.class, listener, errorListener);
        this.postNum = postNum;
        this.lastPost = lastPost;
    }


    private static String getURL(int postNum, Event lastPost){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getUrlBuilder()
                .addPathSegment(URLNames.LOCATION_PATH_SEGMENT)
                .addQueryParameter(URLNames.POST_NUMBER_PARAM, String.valueOf(postNum));
        if(lastPost != null){
            urlBuilder.addQueryParameter(URLNames.LAST_POST_PARAM, String.valueOf(lastPost.getID()));
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
