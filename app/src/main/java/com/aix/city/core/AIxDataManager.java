package com.aix.city.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aix.city.core.data.City;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Tag;
import com.aix.city.core.data.User;
import com.aix.city.dummy.DummyContent;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class AIxDataManager extends Observable {

    public static final String OBSERVER_KEY_CHANGED_TAGS = "tags";
    public static final String OBSERVER_KEY_CHANGED_LOCATIONS = "locations";
    public static final String OBSERVER_KEY_CHANGED_CITY = "city";
    public static final Location EMPTY_LOCATION = new Location();

    private static AIxDataManager instance;
    private final Context context;
    private City currentCity;
    private List<Tag> allTags = new ArrayList<Tag>();
    private List<City> allCities = new ArrayList<City>();
    private Map<Integer, Location> storedLocations = new HashMap<Integer, Location>();


    //Singleton methods and constructor
    private AIxDataManager(Context context) {
        this.context = context;
    }

    public static synchronized void createInstance(Context context){
        if(instance == null){
            instance = new AIxDataManager(context);
            //instance.init()
        }
    }

    public void init(){
        allTags.add(DummyContent.BAR_TAG);
        allTags.add(DummyContent.RESTAURANT_TAG);
        allCities.add(DummyContent.AACHEN);

        currentCity = allCities.get(0);
        requestCityLocations(currentCity);
        requestTags();
    }

    /**
     * Singleton getter. createInstance(Context context) must be called before.
     */
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

    public User getCurrentUser(){
        return AIxLoginModule.getInstance().getLoggedInUser();
    }

    @Nullable
    public City getCity(int id){
        for(City city: allCities){
            if(city.getId() == id) return city;
        }
        return null;
    }

    @Nullable
    public Tag getTag(int id){
        for(Tag tag: allTags){
            if(tag.getId() == id) return tag;
        }
        return null;
    }

    @NonNull
    public Location getLocation(int locationId) {
        Location location = storedLocations.get(locationId);
        if(location == null){
            //requestLocation(locationId);
            location = EMPTY_LOCATION;
        }
        return location;
    }

    public List<Location> getCityLocations() {
        return new ArrayList<Location>(storedLocations.values());
    }

    public Set<Location> getFavorites(User user){
        //load stored favorite-list
        return new HashSet<Location>();
    }

    public void requestCities(){
        allCities.add(DummyContent.AACHEN);
    }

    public void requestTags(){
        Response.Listener<Tag[]> listener = new Response.Listener<Tag[]>(){
            @Override
            public void onResponse(Tag[] response) {
                allTags = Arrays.asList(response);
                setChanged();
                notifyObservers(OBSERVER_KEY_CHANGED_TAGS);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        AIxNetworkManager.getInstance().requestTags(listener, errorListener);
    }

    public void requestCityLocations(City city){
        Response.Listener<Location[]> listener = new Response.Listener<Location[]>(){
            @Override
            public void onResponse(Location[] response) {
                for (int i = 0; i < response.length; i++){
                    storedLocations.put(response[i].getId(), response[i]);
                }
                setChanged();
                notifyObservers(OBSERVER_KEY_CHANGED_LOCATIONS);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        AIxNetworkManager.getInstance().requestCityLocations(listener, errorListener, city);
    }

    public void requestLocation(int locationId){
        Response.Listener<Location> listener = new Response.Listener<Location>(){
            @Override
            public void onResponse(Location response) {
                storedLocations.put(response.getId(), response);
                setChanged();
                notifyObservers(OBSERVER_KEY_CHANGED_LOCATIONS);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        AIxNetworkManager.getInstance().requestLocation(listener, errorListener, locationId);
    }

}
