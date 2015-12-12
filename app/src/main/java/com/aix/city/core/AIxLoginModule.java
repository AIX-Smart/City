package com.aix.city.core;

import android.content.Context;
import android.provider.Settings;

import com.aix.city.core.data.User;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Observable;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIxLoginModule extends Observable {

    public static final String OBSERVER_KEY_LOGIN_SUCCESS = "success";
    public static final String OBSERVER_KEY_LOGIN_FAILURE = "failure";

    private static AIxLoginModule instance;
    private final Context context;
    private User loggedInUser;


    //Singleton methods and constructor
    private AIxLoginModule(Context context) {
        this.context = context;
    }

    public static synchronized void createInstance(Context context){
        if(instance == null){
            instance = new AIxLoginModule(context);
            //instance.login()
        }
    }

    public static AIxLoginModule getInstance() {
        return instance;
    }
    //

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void login() {
        Response.Listener<User> listener = new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                loggedInUser = response;
                setChanged();
                notifyObservers(OBSERVER_KEY_LOGIN_SUCCESS);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setChanged();
                notifyObservers(OBSERVER_KEY_LOGIN_FAILURE);
            }
        };
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        //send request to server
        AIxNetworkManager.getInstance().requestLogin(listener, errorListener, deviceId);
    }
}
