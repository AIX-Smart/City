package com.aix.city.comm;

import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 08.02.2016.
 */
public class GetIsAuthorizedRequest extends AIxJsonRequest<Boolean> {

    private Location location;

    public GetIsAuthorizedRequest(Response.Listener<Boolean> listener, Response.ErrorListener errorListener, Location location) {
        super(Request.Method.GET, URLFactory.get().createIsAuthorizedUrl(location), null, Boolean.class, listener, errorListener, true);
        this.location = location;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public Location getLocation() {
        return location;
    }
}
