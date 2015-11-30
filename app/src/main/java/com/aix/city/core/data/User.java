package com.aix.city.core.data;

import com.aix.city.core.AIxDataManager;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Thomas on 11.10.2015.
 */
public class User {

    private int id;

    //no-argument constructor for JSON
    private User(){}

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public UserData getData() {
        return AIxDataManager.getInstance().getUserData(id);
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
        return 83*id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
