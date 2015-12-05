package com.aix.city.comm;

import com.aix.city.core.Likeable;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 01.12.2015.
 */
public class LikeChangeRequest extends AIxJsonRequest<String> {

    private final Likeable likeable;
    private final boolean like;

    public LikeChangeRequest(Response.Listener<String> listener, Response.ErrorListener errorListener, Likeable likeable, boolean like) {
        super(Request.Method.PUT, URLFactory.get().createLikeChangeURL(likeable), like, String.class, listener, errorListener, false);
        this.likeable = likeable;
        this.like = like;
    }

    public Likeable getLikeable() {
        return likeable;
    }

    public boolean isLike() {
        return like;
    }
}