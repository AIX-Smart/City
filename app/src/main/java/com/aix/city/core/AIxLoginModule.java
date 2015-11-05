package com.aix.city.core;

import android.content.Context;
import android.provider.Settings;

import com.aix.city.comm.AIxNetworkManager;
import com.aix.city.comm.LoginRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIXLoginModule {

    private static AIXLoginModule instance;
    private final Context context;
    private User loggedInUser;

    //Singleton methods and constructor
    private AIXLoginModule(Context context) {
        this.context = context;
    }
    public static synchronized void initInstance(Context context){
        if(instance == null){
            instance = new AIXLoginModule(context);
            instance.getLoggedInUser();
        }
    }
    public static AIXLoginModule getInstance() {
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

    public void test(){
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loggedInUser = new User((long) Integer.parseInt(response));
                //login confirmation
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //login failure
            }
        };
        Request<String> request = new StringRequest("http://178.254.32.8:8080/service/city", listener, errorListener);
        AIxNetworkManager.getInstance().addRequest(request);
    }
}
