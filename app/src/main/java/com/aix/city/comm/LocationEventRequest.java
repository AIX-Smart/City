package com.aix.city.comm;

import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 02.11.2015.
 */
public class LocationEventRequest extends AIxRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private Location location;

    public LocationEventRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                Location location){
        super(Request.Method.GET, URLFactory.get().createGetLocationEventsURL(postNum, lastPost, location), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.location = location;
    }

    public int getPostNum() {
        return postNum;
    }

    public Post getLastPost() {
        return lastPost;
    }

    public Location getLocation() {
        return location;
    }
}
