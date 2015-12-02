package com.aix.city.comm;

import com.aix.city.core.data.Post;
import com.aix.city.core.data.User;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 01.12.2015.
 */
public class LikeRequest extends AIxJsonRequest<String> {

    private final Post post;
    private final boolean like;

    public LikeRequest(Response.Listener<String> listener, Response.ErrorListener errorListener, Post post, boolean like) {
        super(Request.Method.PUT, URLFactory.get().createLikeURL(post), like, String.class, listener, errorListener, false);
        this.post = post;
        this.like = like;
    }

    public Post getPost() {
        return post;
    }

    public boolean isLike() {
        return like;
    }
}