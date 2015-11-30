package com.aix.city.comm;

import com.aix.city.core.EditableEventListing;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Thomas on 25.11.2015.
 */
public class CreateEventRequest extends AIxRequest<String> {

    public CreateEventRequest(final EditableEventListing eventListing, String content){
        super(Request.Method.POST,
                URLFactory.get().createEventCreationURL(eventListing.getLocation()),
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
}
