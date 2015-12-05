package com.aix.city.core;

import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import com.aix.city.R;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.User;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIxLoginModule {

    public static final int LOGIN_TIMEOUT_MS = 2000;

    private static AIxLoginModule instance;
    private final Context context;
    private User loggedInUser;

    //special volley listener for synchronous behaviour (waiting for response)
    private RequestFuture<User> loginFuture;


    //Singleton methods and constructor
    private AIxLoginModule(Context context) {
        this.context = context;
    }

    public static synchronized void initInstance(Context context){
        if(instance == null){
            instance = new AIxLoginModule(context);
            instance.login();
        }
    }

    public static AIxLoginModule getInstance() {
        return instance;
    }
    //

    public User getLoggedInUser() {
        if(loggedInUser == null){
            if(loginFuture == null) login();
            try{
                loggedInUser = loginFuture.get(LOGIN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            }
            catch (Exception e){
                Toast.makeText(context, context.getResources().getString(R.string.loginFailure), Toast.LENGTH_SHORT).show();
                loggedInUser = new User(0, new ArrayList<Location>(), 0);
            }
            loginFuture = null;
        }
        return loggedInUser;
    }

    public void login() {
        loginFuture = RequestFuture.newFuture();
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        //send request to server
        AIxNetworkManager.getInstance().requestLogin(loginFuture, loginFuture, deviceId);
    }
}
