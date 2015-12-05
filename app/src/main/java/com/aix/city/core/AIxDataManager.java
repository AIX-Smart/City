package com.aix.city.core;

import android.content.Context;
import android.util.SparseArray;

import com.aix.city.core.data.City;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.LocationData;
import com.aix.city.core.data.Tag;
import com.aix.city.core.data.User;
import com.aix.city.dummy.DummyContent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIxDataManager {

    private static AIxDataManager instance;
    private final Context context;
    private City currentCity;
    private List<Tag> allTags = new ArrayList<Tag>();
    private List<City> allCities = new ArrayList<City>();
    private SparseArray<LocationData> storedLocationData = new SparseArray<LocationData>();


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

    public List<City> getCities() {
        return allCities;
    }

    public List<Tag> getTags() {
        return allTags;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public City getCity(int id){
        for(City city: allCities){
            if(city.getId() == id) return city;
        }
        return null;
    }

    public LocationData createLocation(int locationId, String name, List<Tag> tags, String description, City city, String street, String houseNumber, String phoneNumber, int likeCount, boolean liked, String gps) {
        LocationData data = new LocationData(new Location(locationId, name), tags, description, city.getId(), street, phoneNumber, houseNumber, likeCount, liked, gps);
        storedLocationData.put(locationId, data);
        return data;
    }

    public LocationData getLocationData(int locationId) {
        LocationData data = storedLocationData.get(locationId);
        if(data == null){
            //TODO: load from database instead
            data = createLocation(locationId, "GinBar", allTags, "Hier steht eine Beschreibung der Bar", DummyContent.AACHEN, "Irgendwo-Straße", "42", "0240/123456789", 0, false, "gps");
        }
        return data;
    }

    public Set<Location> getFavorites(User user){
        //load stored favorite-list
        return new HashSet<Location>();
    }

    public void reset() {
        return;
    }
}
