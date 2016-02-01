package com.aix.city.comm;

import com.aix.city.core.Likeable;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 28.01.2016.
 */
public class GetLikeStatusRequest extends AIxJsonRequest<Boolean> {

    private final Likeable likeable;

    public GetLikeStatusRequest(Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Likeable likeable) {
        super(Request.Method.GET, URLFactory.get().createGetLikeStatusURL(likeable), null, Boolean.class, listener, errorListener, false);
        this.likeable = likeable;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public Likeable getLikeable() {
        return likeable;
    }
}