package com.aix.city.comm;

import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.EditableCommentListing;
import com.aix.city.core.data.Event;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 25.11.2015.
 */
public class CreateCommentRequest extends AIxJacksonRequest<String> {

    public CreateCommentRequest(final EditableCommentListing commentListing, String content){
        super(Request.Method.POST,
                createURL(commentListing.getEvent()),
                content,
                String.class,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        commentListing.refresh();
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


    private static String createURL(Event event){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.EVENT)
                .addPathSegment(String.valueOf(event.getId()))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        return urlBuilder.build().toString();
    }

}
