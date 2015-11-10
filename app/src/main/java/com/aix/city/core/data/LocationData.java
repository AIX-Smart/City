package com.aix.city.core.data;

import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class LocationData {

    private Location location;
    private Set<Tag> tags;
    private String description;
    private City city;
    private String address;
    private android.location.Location gps;
    private int likeCount;
    private boolean liked;
    //TODO: öffnungszeiten
    //TODO: Bilder

    //no-argument constructor for JSON
    private LocationData(){}

    /**
     * INTERNAL USE ONLY: use instead location.getData() or AIxDataManager.getInstance().createLocationData(...)
     */
    public LocationData(Location location, Set<Tag> tags, String description, City city, String address, int likeCount, boolean liked) {
        this.location = location;
        this.tags = tags;
        this.description = description;
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

    public String getName(){
        return location.getName();
    }

    public String getDescription() {
        return description;
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
