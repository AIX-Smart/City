package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class AIxLoginModule {

    private static AIxLoginModule instance = new AIxLoginModule();
    private User loggedUser; //Optional

    private AIxLoginModule() {
    }

    public static AIxLoginModule getInstance() {
        return instance;
    }
    public User getLoggedUser() {
        return loggedUser;
    }

    public void login() {
        return;
    }
}
