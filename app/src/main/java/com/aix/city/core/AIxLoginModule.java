package com.aix.city.core;

import android.content.Context;
import android.provider.Settings;

import com.aix.city.comm.URLFactory;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.User;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Observable;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIxLoginModule extends Observable {

    public static final String OBSERVER_KEY_LOGIN_SUCCESS = "login.success";
    public static final String OBSERVER_KEY_LOGIN_FAILURE = "login.failure";
    public static final String OBSERVER_KEY_AUTHENTICATE_SUCCESS = "authenticate.success";
    public static final String OBSERVER_KEY_AUTHENTICATE_INCORRECT_INPUT = "authenticate.incorrect";
    public static final String OBSERVER_KEY_AUTHENTICATE_FAILURE = "authenticate.failure";

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
        AIxNetworkManager.getInstance().requestLogin(this, listener, errorListener, deviceId);
    }

    public void authenticate(final Location location, String mail, String password){

        Response.Listener<Boolean[]> listener = new Response.Listener<Boolean[]>() {
            @Override
            public void onResponse(Boolean[] response) {
                setChanged();
                boolean isOwner = response[0];
                boolean isAdmin = response[1];
                if (isOwner || isAdmin){
                    if (isOwner){
                        loggedInUser.addLocation(location);
                        invalidateUserCache();
                    }
                    else{
                        loggedInUser.setIsAdmin(true);
                    }
                    notifyObservers(OBSERVER_KEY_AUTHENTICATE_SUCCESS);
                }
                else{
                    notifyObservers(OBSERVER_KEY_AUTHENTICATE_INCORRECT_INPUT);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setChanged();
                notifyObservers(OBSERVER_KEY_AUTHENTICATE_FAILURE);
            }
        };

        AIxNetworkManager.getInstance().requestAuthentication(this, listener, errorListener, location, mail, password);
    }

    public void invalidateUserCache(){
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        AIxNetworkManager.getInstance().getRequestQueue().getCache().invalidate(URLFactory.get().createLoginURL(deviceId), true);
    }
}
