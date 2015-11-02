package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
public class User {

    private long userID;

    //no-argument constructor for JSON
    private User(){}

    public User(long userID) {
        this.userID = userID;
    }

    public long getID() {
        return userID;
    }

    public UserData getData() {
        return AIxDataManager.getInstance().getUserData(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userID == user.userID;

    }

    @Override
    public int hashCode() {
        return (int) (userID ^ (userID >>> 32));
    }

    @Override
    public String toString() {
        return String.valueOf(userID);
    }
}
