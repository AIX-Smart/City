package com.aix.city.core.data;

import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.User;

import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class UserData {

    private User user;
    private Set<Location> favorites;
    private Set<Location> ownBusinesses;
    private Set<Integer> likedPosts;
    private List<Post> writtenPosts;

    //no-argument constructor for JSON
    private UserData(){}

    /**
     * INTERNAL USE ONLY: use instead user.getData() or AIxDataManager.getInstance().createUserData(...)
     */
    public UserData(User user, Set<Location> favorites, Set<Location> ownBusinesses, Set<Integer> likedPosts, List<Post> writtenPosts) {
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

    public Set<Integer> getLikedPosts() {
        return likedPosts;
    }

    public List<Post> getWrittenPosts() {
        return writtenPosts;
    }
}
