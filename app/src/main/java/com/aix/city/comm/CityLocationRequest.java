package com.aix.city.comm;

import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.data.City;
import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 06.11.2015.
 */
public class CityLocationRequest extends AIxJacksonRequest<Location[]> {

    private City city;

    public CityLocationRequest(Response.Listener<Location[]> listener,
                            Response.ErrorListener errorListener,
                            City city){
        super(Request.Method.GET, createURL(city), null, Location[].class, listener, errorListener, false);
        this.city = city;
    }


    private static String createURL(City city){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.LOCATIONS)
                .addPathSegment(String.valueOf(city.getId()));
        return urlBuilder.build().toString();
    }

    public City getCity() {
        return city;
    }
}
