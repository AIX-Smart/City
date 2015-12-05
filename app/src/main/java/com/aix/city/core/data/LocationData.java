package com.aix.city.core.data;

import com.aix.city.core.Likeable;

import java.util.List;

/**
 * Created by Thomas on 11.10.2015.
 */
public class LocationData extends Likeable {

    private Location location;
    private List<Tag> tags;
    private String description;
    private int cityId;
    private String street;
    private String houseNumber;
    private String phoneNumber;
    private String gps;
    //TODO: öffnungszeiten
    //TODO: Bilder

    //no-argument constructor for JSON
    private LocationData(){}

    /**
     * INTERNAL USE ONLY: use instead location.getData() or AIxDataManager.getInstance().createLocation(...)
     */
    public LocationData(Location location, List<Tag> tags, String description, int cityId, String street, String houseNumber, String phoneNumber, int likeCount, boolean liked, String gps) {
        this.location = location;
        this.tags = tags;
        this.description = description;
        this.cityId = cityId;
        this.street = street;
        this.houseNumber = houseNumber;
        this.phoneNumber = phoneNumber;
        this.gps = gps;
        setLiked(liked);
        setLikeCount(likeCount);
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

    public int getId(){
        return location.getId();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGps() {
        return gps;
    }

    public void addTag(Tag tag){
        tags.add(tag);
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
    }
}
