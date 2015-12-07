package com.aix.city.comm;

import com.aix.city.core.data.Tag;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 06.11.2015.
 */
public class GetTagsRequest extends AIxJsonRequest<Tag[]> {

    public GetTagsRequest(Response.Listener<Tag[]> listener, Response.ErrorListener errorListener){
        super(Request.Method.GET, URLFactory.get().createGetAllTagsURL(), null, Tag[].class, listener, errorListener, true);
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }
}
