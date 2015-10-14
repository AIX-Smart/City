package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class AIXLoginModule {

    private static AIXLoginModule instance = new AIXLoginModule();
    private User loggedInUser = new User(0); //Optional

    private AIXLoginModule() {
    }

    public static AIXLoginModule getInstance() {
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void login() {
        return;
    }
}
