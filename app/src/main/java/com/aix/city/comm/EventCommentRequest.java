package com.aix.city.comm;

import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Post;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 03.11.2015.
 */
public class EventCommentRequest extends AIxJacksonRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private Event event;

    public EventCommentRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                Event event){
        super(Request.Method.GET, getURL(postNum, lastPost, event), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.event = event;
    }


    private static String getURL(int postNum, Post lastPost, Event event){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.EVENT)
                .addPathSegment(String.valueOf(event.getId()))
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

    public Event getEvent() {
        return event;
    }
}
