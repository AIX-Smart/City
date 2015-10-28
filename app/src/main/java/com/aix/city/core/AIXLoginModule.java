package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class AIXLoginModule {

    //Singleton
    private static final class InstanceHolder {
        static final AIXLoginModule INSTANCE = new AIXLoginModule();
    }
    private AIXLoginModule() {
    }
    public static AIXLoginModule getInstance() {
        return InstanceHolder.INSTANCE;
    }
    //

    private User loggedInUser = new User(0); //Optional

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void login() {
        return;
    }
}
