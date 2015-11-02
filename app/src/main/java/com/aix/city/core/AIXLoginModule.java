package com.aix.city.core;

import android.content.Context;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIXLoginModule {

    private static AIXLoginModule instance;
    private final Context context;
    private User loggedInUser = new User(0); //Optional

    //Singleton methods and constructor
    private AIXLoginModule(Context context) {
        this.context = context;
    }
    public static synchronized void initInstance(Context context){
        if(instance == null){
            instance = new AIXLoginModule(context);
        }
    }
    public static AIXLoginModule getInstance() {
        return instance;
    }
    //

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void login() {
        return;
    }
}
