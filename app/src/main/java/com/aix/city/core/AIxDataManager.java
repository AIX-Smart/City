package com.aix.city.core;

import android.content.Context;

import com.aix.city.core.data.City;
import com.aix.city.core.data.CityData;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.LocationData;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;
import com.aix.city.core.data.User;
import com.aix.city.core.data.UserData;
import com.aix.city.dummy.DummyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIxDataManager {

    private static AIxDataManager instance;
    private final Context context;
    private City currentCity;
    private List<Tag> allTags = new ArrayList<Tag>();
    private Set<City> allCities = new HashSet<>();
    private Map<Location, LocationData> storedLocationData = new HashMap<Location, LocationData>();
    private Map<User, UserData> storedUserData = new HashMap<User, UserData>();
    private Map<City, CityData> storedCityData = new HashMap<City, CityData>();


    //Singleton methods and constructor
    private AIxDataManager(Context context) {
        this.context = context;
    }

    public static synchronized void initInstance(Context context){
        if(instance == null){
            instance = new AIxDataManager(context);
            instance.allTags.add(DummyContent.BAR_TAG);
            instance.allTags.add(DummyContent.RESTAURANT_TAG);
            instance.allCities.add(DummyContent.AACHEN);
            instance.currentCity = DummyContent.AACHEN;
        }
    }

    public static AIxDataManager getInstance() {
        return instance;
    }
    //

    public Set<City> getCities() {
        return allCities;
    }

    public List<Tag> getTags() {
        return allTags;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public LocationData createLocationData(Location location, List<Tag> tags, String description, City city, String street, String houseNumber, int likeCount, boolean liked, String gps) {
        LocationData data = new LocationData(location, tags, description, city.getId(), street, houseNumber, likeCount, liked, gps);
        storedLocationData.put(location, data);
        return data;
    }

    public CityData createCityData(City city, List<Location> locations) {
        CityData data = new CityData(city, locations);
        storedCityData.put(city, data);
        return data;
    }

    public UserData createUserData(User user, Set<Location> favorites, List<Location> ownBusinesses, Set<Integer> likedPosts, List<Post> writtenPosts) {
        UserData data = new UserData(user, favorites, ownBusinesses);
        storedUserData.put(user, data);
        return data;
    }

    public LocationData getLocationData(Location location) {
        LocationData data = storedLocationData.get(location);
        if(data == null){
            //TODO: load from database instead
            data = createLocationData(location, allTags, "Hier steht eine Beschreibung der Bar", DummyContent.AACHEN, "Irgendwo-Straße", "42", 0, false, "gps");
        }
        return data;
    }

    public CityData getCityData(City city) {
        CityData data = storedCityData.get(city);
        if(data == null){
            //TODO: load from database instead
            List<Location> locations = new ArrayList<Location>();
            locations.add(DummyContent.GINBAR);
            data = createCityData(city, locations);
        }
        return data;
    }

    public UserData getUserData(User user) {
        UserData data = storedUserData.get(user);
        if(data == null){
            //TODO: load from database instead
            data = createUserData(user, new HashSet<Location>(), new ArrayList<Location>(), new HashSet<Integer>(), new ArrayList<Post>());
        }
        return data;
    }

    public void reset() {
        return;
    }
}
