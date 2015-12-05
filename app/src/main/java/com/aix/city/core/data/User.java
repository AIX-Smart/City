package com.aix.city.core.data;

import com.aix.city.core.AIxDataManager;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class User {

    private int id;
    private List<Location> ownLocations;
    private int permission;
    private transient Set<Location> favorites = null;

    //no-argument constructor for JSON
    private User(){}

    /**
     * INTERNAL USE ONLY: use instead user.getData() or AIxDataManager.getInstance().createUser(...)
     */
    public User(int id, List<Location> ownLocations, int permission) {
        this.id = id;
        this.ownLocations = ownLocations;
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public List<Location> getOwnLocations() {
        return ownLocations;
    }

    public int getPermission() {
        return permission;
    }

    @JsonIgnore
    public Set<Location> getFavorites() {
        return AIxDataManager.getInstance().getFavorites(this);
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
