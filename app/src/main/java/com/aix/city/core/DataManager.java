package com.aix.city.core;

import com.aix.city.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by Thomas on 11.10.2015.
 */
public class DataManager {

    //Singleton
    private static final class InstanceHolder {
        static final DataManager INSTANCE = new DataManager();
    }
    private DataManager() {
        allTags.add(DummyContent.BAR_TAG);
        allTags.add(DummyContent.RESTAURANT_TAG);
        allCities.add(DummyContent.AACHEN);
    }
    public static DataManager getInstance() {
        return InstanceHolder.INSTANCE;
    }
    //

    private Set<Tag> allTags = new HashSet<Tag>();
    private Set<City> allCities = new HashSet<City>();
    private Map<Location, LocationData> storedLocationData = new HashMap<Location, LocationData>();
    private Map<User, UserData> storedUserData = new HashMap<User, UserData>();
    private Map<City, CityData> storedCityData = new HashMap<City, CityData>();


    public LocationData createLocationData(Location location, Set<Tag> tags, String description, City city, String address, int likeCount, boolean liked) {
        LocationData data = new LocationData(location, tags, description, city, address, likeCount, liked);
        storedLocationData.put(location, data);
        return data;
    }

    public CityData createCityData(City city, Set<Location> locations) {
        CityData data = new CityData(city, locations);
        storedCityData.put(city, data);
        return data;
    }

    public UserData createUserData(User user, Set<Location> favorites, Set<Location> ownBusinesses, Set<Long> likedPosts, List<Post> writtenPosts) {
        UserData data = new UserData(user, favorites, ownBusinesses, likedPosts, writtenPosts);
        storedUserData.put(user, data);
        return data;
    }

    public Set<City> getCities() {
        return allCities;
    }

    public Set<Tag> getTags() {
        return allTags;
    }

    public LocationData getLocationData(Location location) {
        LocationData data = storedLocationData.get(location.getID());
        if(data == null){
            //TODO: load from database instead
            data = createLocationData(location, allTags, location.getName(), DummyContent.AACHEN, "Irgendwo-Straße 42", 0, false);
        }
        return data;
    }

    public CityData getCityData(City city) {
        CityData data = storedCityData.get(city.getID());
        if(data == null){
            //TODO: load from database instead
            Set<Location> locations = new HashSet<Location>();
            locations.add(DummyContent.GINBAR);
            data = createCityData(city, locations);
        }
        return data;
    }

    public UserData getUserData(User user) {
        UserData data = storedUserData.get(user.getID());
        if(data == null){
            //TODO: load from database instead
            data = createUserData(user, new HashSet<Location>(), new HashSet<Location>(), new HashSet<Long>(), new ArrayList<Post>());
        }
        return data;
    }

    public void reset() {
        return;
    }
}
