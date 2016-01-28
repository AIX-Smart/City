package com.aix.city.comm;

import com.aix.city.core.Likeable;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 28.01.2016.
 */
public class GetLikeCountRequest extends AIxJsonRequest<Integer> {

    private final Likeable likeable;

    public GetLikeCountRequest(Response.Listener<Integer> listener, Response.ErrorListener errorListener, Likeable likeable) {
        super(Request.Method.GET, URLFactory.get().createGetLikeCountURL(likeable), null, Integer.class, listener, errorListener, false);
        this.likeable = likeable;
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    public Likeable getLikeable() {
        return likeable;
    }
}
