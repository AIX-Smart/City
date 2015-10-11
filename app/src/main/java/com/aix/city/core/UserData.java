package com.aix.city.core;

import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class UserData {

    private User user;
    private Set<Location> favorites;
    private List<Location> ownBusinesses;
    private Set<Long> likedPosts;

    public User getUser() {
        return user;
    }

    public Set<Location> getFavorites() {
        return favorites;
    }

    public List<Location> getOwnBusinesses() {
        return ownBusinesses;
    }

    public Set<Long> getLikedPosts() {
        return likedPosts;
    }
}
