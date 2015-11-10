package com.aix.city.comm;

import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.data.User;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 05.11.2015.
 */
public class LoginRequest extends JacksonRequest<User>{

    private String deviceId;

    public LoginRequest(Response.Listener<User> listener,
                        Response.ErrorListener errorListener,
                        String deviceId){
        super(Request.Method.GET, getURL(deviceId), null, User.class, listener, errorListener, false);
        this.deviceId = deviceId;
    }


    private static String getURL(String deviceId){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.USER)
                /*.addPathSegment(deviceId)*/;
        return urlBuilder.build().toString();
    }
}
