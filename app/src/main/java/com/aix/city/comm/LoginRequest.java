package com.aix.city.comm;

import com.aix.city.core.data.User;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 05.11.2015.
 */
public class LoginRequest extends AIxJsonRequest<User> {

    private final String deviceId;

    public LoginRequest(Response.Listener<User> listener,
                        Response.ErrorListener errorListener,
                        String deviceId){
        super(Request.Method.GET, URLFactory.get().createLoginURL(deviceId), null, User.class, listener, errorListener, false);
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
