package com.aix.city.comm;

import com.aix.city.core.data.City;
import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 06.11.2015.
 */
public class GetCityLocationsRequest extends AIxJsonRequest<Location[]> {

    private final City city;

    public GetCityLocationsRequest(Response.Listener<Location[]> listener,
                                   Response.ErrorListener errorListener,
                                   City city){
        super(Request.Method.GET, URLFactory.get().createGetCityLocationsURL(city), null, Location[].class, listener, errorListener, false);
        this.city = city;
    }

    public City getCity() {
        return city;
    }
}
