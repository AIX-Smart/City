package com.aix.city.core.data;

import android.support.annotation.NonNull;

import com.aix.city.core.AIxDataManager;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class User {

    private int id;
    private List<Integer> ownLocationIds;
    private boolean isAdmin = true;
    private transient Set<Location> favorites = null;

    //no-argument constructor for JSON
    private User(){}

    /**
     * INTERNAL USE ONLY: use instead user.getData() or AIxDataManager.getInstance().createUser(...)
     */
    public User(int id, List<Integer> ownLocationIds, int permission) {
        this.id = id;
        this.ownLocationIds = ownLocationIds;
    }

    public int getId() {
        return id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @NonNull
    public List<Integer> getOwnLocationIds() {
        if (ownLocationIds == null){
            ownLocationIds = new ArrayList<Integer>();
        }
        return ownLocationIds;
    }

    @JsonIgnore
    public boolean isAuthorized(Location location){
        return isAdmin || isOwner(location);
    }

    @JsonIgnore
    public boolean isOwner(Location location){
        return getOwnLocationIds().contains(location.getId());
    }

    @JsonIgnore
    public void addLocation(Location location) {
        getOwnLocationIds().add(location.getId());
    }

    @JsonIgnore
    public List<Location> getOwnLocations(){
        List<Location> list = new ArrayList<Location>();
        for (int locationId : getOwnLocationIds()){
            list.add(AIxDataManager.getInstance().getLocation(locationId));
        }
        return list;
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
