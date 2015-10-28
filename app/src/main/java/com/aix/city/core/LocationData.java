package com.aix.city.core;

import java.util.Set;
import java.util.SortedSet;

/**
 * Created by Thomas on 11.10.2015.
 */
public class LocationData {

    private Location location;
    private Set<Tag> tags;
    private String fullName;
    private City city;
    private String address;
    private android.location.Location gps;
    private int likeCount;
    private boolean liked;
    //TODO: öffnungszeiten
    //TODO: Bilder


    /**
     * INTERNAL USE ONLY: use instead DataManager.getInstance().createLocationData(...)
     */
    public LocationData(Location location, Set<Tag> tags, String fullName, City city, String address, int likeCount, boolean liked) {
        this.location = location;
        this.tags = tags;
        this.fullName = fullName;
        this.city = city;
        this.address = address;
        this.likeCount = likeCount;
        this.liked = liked;
    }

    public Location getLocation() {
        return location;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public City getCity() {
        return city;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public android.location.Location getGps() {
        return gps;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void addTag(Tag tag){
        tags.add(tag);
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
    }

    public void like() {
        if (!liked) {
            liked = true;
            if (likeCount < Integer.MAX_VALUE) likeCount++;
            //TODO: Server Communication
        }
    }

    public void resetLike() {
        if (liked) {
            liked = false;
            if (likeCount > 0) likeCount--;
            //TODO: Server Communication
        }
    }
}
