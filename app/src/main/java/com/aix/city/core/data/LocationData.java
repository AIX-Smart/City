package com.aix.city.core.data;

import java.util.List;

/**
 * Created by Thomas on 11.10.2015.
 */
public class LocationData {

    private Location location;
    private List<Tag> tags;
    private String description;
    private int cityId;
    private String street;
    private String houseNumber;
    private String phoneNumber;
    private String gps;
    private int likeCount;
    private boolean liked;
    //TODO: öffnungszeiten
    //TODO: Bilder

    //no-argument constructor for JSON
    private LocationData(){}

    /**
     * INTERNAL USE ONLY: use instead location.getData() or AIxDataManager.getInstance().createLocation(...)
     */
    public LocationData(Location location, List<Tag> tags, String description, int cityId, String street, String houseNumber, int likeCount, boolean liked, String gps) {
        this.location = location;
        this.tags = tags;
        this.description = description;
        this.cityId = cityId;
        this.street = street;
        this.houseNumber = houseNumber;
        this.likeCount = likeCount;
        this.liked = liked;
        this.gps = gps;
    }


    public Location getLocation() {
        return location;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public int getCityId() {
        return cityId;
    }

    public String getName(){
        return location.getName();
    }

    public String getDescription() {
        return description;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGps() {
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
