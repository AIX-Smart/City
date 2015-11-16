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
    private Map<Integer, LocationData> storedLocationData = new HashMap<Integer, LocationData>();
    private Map<Integer, UserData> storedUserData = new HashMap<Integer, UserData>();
    private Map<Integer, CityData> storedCityData = new HashMap<Integer, CityData>();


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

    public LocationData createLocationData(int locationId, String name, List<Tag> tags, String description, City city, String street, String houseNumber, int likeCount, boolean liked, String gps) {
        LocationData data = new LocationData(new Location(locationId, name), tags, description, city.getId(), street, houseNumber, likeCount, liked, gps);
        storedLocationData.put(locationId, data);
        return data;
    }

    public CityData createCityData(int cityId, String name, List<Location> locations) {
        CityData data = new CityData(new City(cityId, name), locations);
        storedCityData.put(cityId, data);
        return data;
    }

    public UserData createUserData(int userId, Set<Location> favorites, List<Location> ownBusinesses, Set<Integer> likedPosts, List<Post> writtenPosts) {
        UserData data = new UserData(new User(userId), favorites, ownBusinesses);
        storedUserData.put(userId, data);
        return data;
    }

    public LocationData getLocationData(int locationId) {
        LocationData data = storedLocationData.get(locationId);
        if(data == null){
            //TODO: load from database instead
            data = createLocationData(locationId, "GinBar", allTags, "Hier steht eine Beschreibung der Bar", DummyContent.AACHEN, "Irgendwo-Straße", "42", 0, false, "gps");
        }
        return data;
    }

    public CityData getCityData(int cityId) {
        CityData data = storedCityData.get(cityId);
        if(data == null){
            //TODO: load from database instead
            List<Location> locations = new ArrayList<Location>();
            locations.add(DummyContent.GINBAR);
            data = createCityData(cityId, "Aachen", locations);
        }
        return data;
    }

    public UserData getUserData(int userId) {
        UserData data = storedUserData.get(userId);
        if(data == null){
            //TODO: load from database instead
            data = createUserData(userId, new HashSet<Location>(), new ArrayList<Location>(), new HashSet<Integer>(), new ArrayList<Post>());
        }
        return data;
    }

    public void reset() {
        return;
    }
}
