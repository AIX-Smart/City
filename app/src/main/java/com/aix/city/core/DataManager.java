package com.aix.city.core;

import java.util.Map;
import java.util.SortedSet;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class DataManager {

    private Map<Long, LocationData> storedLocationData;
    private SortedSet<Tag> allTags;
    private SortedSet<City> allCities;
    private Map<Long, UserData> storedUserData;
    private Map<Long, CityData> storedCityData;

    public LocationData createLocationData() {
        return null;
    }

    public CityData createCityData() {
        return null;
    }

    public UserData createUserData() {
        return null;
    }

    public SortedSet<City> getCities() {
        return allCities;
    }

    public SortedSet<Tag> getTags() {
        return allTags;
    }

    public LocationData getLocationData(long ID) {
        return null;
    }

    public CityData getCityData(long ID) {
        return null;
    }

    public UserData getUserData(long ID) {
        return null;
    }

    public void reset() {
        return;
    }
}
