package com.aix.city.comm;

import com.aix.city.core.Likeable;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 01.12.2015.
 */
public class PutLikeRequest extends AIxJsonRequest<Boolean> {

    private final Likeable likeable;
    private final boolean liked;

    public PutLikeRequest(Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Likeable likeable, boolean liked) {
        super(Request.Method.PUT, URLFactory.get().createLikeChangeURL(likeable), liked, Boolean.class, listener, errorListener, false);
        this.likeable = likeable;
        this.liked = liked;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    public Likeable getLikeable() {
        return likeable;
    }

    public boolean isLiked() {
        return liked;
    }
}