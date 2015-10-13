package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class AIxLoginModule {

    private static AIxLoginModule instance = new AIxLoginModule();
    private User loggedInUser = new User(0); //Optional

    private AIxLoginModule() {
    }

    public static AIxLoginModule getInstance() {
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void login() {
        return;
    }
}
