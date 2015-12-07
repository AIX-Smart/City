package com.aix.city.comm;

import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 07.12.2015.
 */
public class GetLocationRequest extends AIxJsonRequest<Location> {

    private final int locationId;

    public GetLocationRequest(Response.Listener<Location> listener, Response.ErrorListener errorListener, int locationId){
        super(Request.Method.GET, URLFactory.get().createGetLocationURL(locationId), null, Location.class, listener, errorListener, true);
        this.locationId = locationId;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public int getLocationId() {
        return locationId;
    }
}
