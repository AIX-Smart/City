package com.aix.city.core;

import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class UserData {

    private User user;
    private Set<Location> favorites;
    private Set<Location> ownBusinesses;
    private Set<Long> likedPosts;
    private List<Post> writtenPosts;

    /**
     * INTERNAL USE ONLY: use instead DataManager.getInstance().createUserData(...)
     */
    public UserData(User user, Set<Location> favorites, Set<Location> ownBusinesses, Set<Long> likedPosts, List<Post> writtenPosts) {
        this.user = user;
        this.favorites = favorites;
        this.ownBusinesses = ownBusinesses;
        this.likedPosts = likedPosts;
        this.writtenPosts = writtenPosts;
    }

    public User getUser() {
        return user;
    }

    public Set<Location> getFavorites() {
        return favorites;
    }

    public Set<Location> getOwnBusinesses() {
        return ownBusinesses;
    }

    public Set<Long> getLikedPosts() {
        return likedPosts;
    }

    public List<Post> getWrittenPosts() {
        return writtenPosts;
    }
}
