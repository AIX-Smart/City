package com.aix.city.comm;

import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 06.11.2015.
 */
public class TagsRequest extends AIxRequest<Location[]> {

    public TagsRequest(Response.Listener<Location[]> listener,
                               Response.ErrorListener errorListener){
        super(Request.Method.GET, URLFactory.get().createGetAllTagsURL(), null, Location[].class, listener, errorListener, false);
    }
}
