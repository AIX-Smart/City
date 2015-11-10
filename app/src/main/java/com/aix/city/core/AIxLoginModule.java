package com.aix.city.core;

import android.content.Context;
import android.provider.Settings;

import com.aix.city.comm.LoginRequest;
import com.aix.city.core.data.User;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIxLoginModule {

    private static AIxLoginModule instance;
    private final Context context;
    private User loggedInUser;

    //Singleton methods and constructor
    private AIxLoginModule(Context context) {
        this.context = context;
    }

    public static synchronized void initInstance(Context context){
        if(instance == null){
            instance = new AIxLoginModule(context);
            instance.getLoggedInUser();
        }
    }

    public static AIxLoginModule getInstance() {
        return instance;
    }
    //

    public User getLoggedInUser() {
        if(loggedInUser == null){
            // set default user till login
            loggedInUser = new User(0);
            //login over request in different thread
            login();
        }
        return loggedInUser;
    }

    public void login() {
        //create login request
        Response.Listener listener = new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                loggedInUser = response;
                //login confirmation
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //login failure
            }
        };
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Request<User> request = new LoginRequest(listener, errorListener, deviceId);

        //add login request to request queue
        AIxNetworkManager.getInstance().addRequest(request);
    }
}
