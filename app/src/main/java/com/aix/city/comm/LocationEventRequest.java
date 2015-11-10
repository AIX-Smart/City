package com.aix.city.comm;

import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 02.11.2015.
 */
public class LocationEventRequest extends JacksonRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private Location location;

    public LocationEventRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                Location location){
        super(Request.Method.GET, getURL(postNum, lastPost, location), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.location = location;
    }


    private static String getURL(int postNum, Post lastPost, Location location){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.LOCATION)
                .addPathSegment(String.valueOf(location.getId()))
                .addPathSegment(String.valueOf(postNum))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getId()));
        }
        return urlBuilder.build().toString();
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
