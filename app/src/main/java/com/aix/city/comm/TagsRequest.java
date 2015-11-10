package com.aix.city.comm;

import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 06.11.2015.
 */
public class TagsRequest extends JacksonRequest<Location[]> {

    public TagsRequest(Response.Listener<Location[]> listener,
                               Response.ErrorListener errorListener){
        super(Request.Method.GET, getURL(), null, Location[].class, listener, errorListener, false);
    }


    private static String getURL(){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.TAG);
        return urlBuilder.build().toString();
    }
}
