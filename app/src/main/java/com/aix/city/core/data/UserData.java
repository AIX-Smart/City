package com.aix.city.core.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class UserData {

    private User user;
    private transient Set<Location> favorites;
    private List<Location> ownLocations;
    private int permission;

    //no-argument constructor for JSON
    private UserData(){}

    /**
     * INTERNAL USE ONLY: use instead user.getData() or AIxDataManager.getInstance().createUser(...)
     */
    public UserData(User user, Set<Location> favorites, List<Location> ownLocations) {
        this.user = user;
        this.favorites = favorites;
        this.ownLocations = ownLocations;
    }

    public User getUser() {
        return user;
    }

    @JsonIgnore
    public Set<Location> getFavorites() {
        return favorites;
    }

    public List<Location> getOwnLocations() {
        return ownLocations;
    }

    public int getPermission() {
        return permission;
    }
}
