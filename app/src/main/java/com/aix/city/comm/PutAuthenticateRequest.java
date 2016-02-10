package com.aix.city.comm;

import com.aix.city.core.data.Location;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 08.02.2016.
 */
public class PutAuthenticateRequest extends AIxJsonRequest<Boolean[]> {

    public PutAuthenticateRequest(Response.Listener<Boolean[]> listener, Response.ErrorListener errorListener, Location location, String mail, String password) {
        super(Request.Method.PUT, URLFactory.get().createAuthenticateUrl(location, mail), hash(password), Boolean[].class, listener, errorListener, false);
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    private static String hash(String pass){
        return pass;
    }
}
