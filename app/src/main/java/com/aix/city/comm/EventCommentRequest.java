package com.aix.city.comm;

import com.aix.city.core.data.Event;
import com.aix.city.core.data.Post;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 03.11.2015.
 */
public class EventCommentRequest extends AIxRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private Event event;

    public EventCommentRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                Event event){
        super(Request.Method.GET, URLFactory.get().createGetEventCommentsURL(postNum, lastPost, event), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.event = event;
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
