package com.aix.city.core;

import com.aix.city.dummy.DummyContent;

import java.util.ArrayList;
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
    }
    public static DataManager getInstance() {
        return InstanceHolder.INSTANCE;
    }
    //

    private Set<Tag> allTags;
    private Set<City> allCities;
    private Map<Long, LocationData> storedLocationData;
    private Map<Long, UserData> storedUserData;
    private Map<Long, CityData> storedCityData;


    public LocationData createLocationData(Location location, Set<Tag> tags, String fullName, City city, String address, int likeCount, boolean liked) {
        LocationData data = new LocationData(location, tags, fullName, city, address, likeCount, liked);
        storedLocationData.put(location.getID(), data);
        return data;
    }

    public CityData createCityData(City city, Set<Location> locations) {
        CityData data = new CityData(city, locations);
        storedCityData.put(city.getID(), data);
        return data;
    }

    public UserData createUserData(User user, Set<Location> favorites, Set<Location> ownBusinesses, Set<Long> likedPosts, List<Post> writtenPosts) {
        UserData data = new UserData(user, favorites, ownBusinesses, likedPosts, writtenPosts);
        storedUserData.put(user.getID(), data);
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
            data = createLocationData(location, new HashSet<Tag>(), location.getName(), DummyContent.AACHEN, "Irgendwo-Straße 42", 0, false);
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
