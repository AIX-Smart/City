package com.aix.city.comm;

import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.EditableEventListing;
import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 25.11.2015.
 */
public class CreateEventRequest extends AIxJacksonRequest<String> {

    public CreateEventRequest(final EditableEventListing eventListing, String content){
        super(Request.Method.POST,
                createURL(eventListing.getLocation()),
                content,
                String.class,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        eventListing.refresh();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO:
                    }
                },
                false);
    }


    private static String createURL(Location location){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.LOCATION)
                .addPathSegment(String.valueOf(location.getId()))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        return urlBuilder.build().toString();
    }
}
