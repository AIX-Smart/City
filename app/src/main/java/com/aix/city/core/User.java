package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
public class User {

    private long userID;

    public User(long userID) {
        this.userID = userID;
    }

    public long getID() {
        return userID;
    }

    public UserData getData() {
        return DataManager.getInstance().getUserData(this);
    }

}
