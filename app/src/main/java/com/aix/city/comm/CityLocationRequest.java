package com.aix.city.comm;

import com.aix.city.core.data.City;
import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 06.11.2015.
 */
public class CityLocationRequest extends AIxRequest<Location[]> {

    private City city;

    public CityLocationRequest(Response.Listener<Location[]> listener,
                            Response.ErrorListener errorListener,
                            City city){
        super(Request.Method.GET, URLFactory.get().createCityLocationsURL(city), null, Location[].class, listener, errorListener, false);
        this.city = city;
    }

    public City getCity() {
        return city;
    }
}
