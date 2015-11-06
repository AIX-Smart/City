package com.aix.city.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Thomas on 11.10.2015.
 */
public class User {

    private long id;

    //no-argument constructor for JSON
    private User(){}

    public User(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public UserData getData() {
        return AIxDataManager.getInstance().getUserData(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
