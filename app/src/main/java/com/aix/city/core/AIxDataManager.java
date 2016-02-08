package com.aix.city.core;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aix.city.core.data.City;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Tag;
import com.aix.city.core.data.User;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

    public static City AACHEN = new City(1, "Aachen");

    public static final String OBSERVER_KEY_CHANGED_TAGS = "tags";
    public static final String OBSERVER_KEY_CHANGED_LOCATIONS = "locations";
    public static final String OBSERVER_KEY_CHANGED_CITY = "city";
    public static final Location EMPTY_LOCATION = new Location();
    public static final City EMPTY_CITY = new City(0, "");
    private static final int REQUEST_RETRY_DELAY = 2000;
    public static final String HUNGRIG = "Hungrig";
    public static final String DURSTIG = "Durstig";
    public static final String PARTY = "Party";
    public static final int ALLES_ANDERE_ID = -1;

    private static AIxDataManager instance;
    private final Context context;
    private final Handler requestRetryHandler = new Handler();
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
        allCities.add(AACHEN);
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

    @NonNull
    public City getCurrentCity() {
        if (currentCity == null){
            currentCity = AACHEN;
        }
        return currentCity;
    }

    public User getCurrentUser(){
        return AIxLoginModule.getInstance().getLoggedInUser();
    }

    @NonNull
    public City getCity(int id){
        for(City city: allCities){
            if(city.getId() == id) return city;
        }
        return EMPTY_CITY;
    }

    @Nullable
    public Tag getTag(int id){
        for(Tag tag: allTags){
            if(tag.getId() == id) return tag;
        }
        return null;
    }

    @Nullable
    public Tag getTag(String name){
        for(Tag tag: allTags){
            if(tag.getName().startsWith(name)) return tag;
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

    public Collection<Location> getCityLocations() {
        return storedLocations.values();
    }

    public Set<Location> getFavorites(User user){
        //load stored favorite-list
        return new HashSet<Location>();
    }

    public void requestCities(){
        allCities.add(AACHEN);
    }

    public void requestTags(){

        final Runnable requestRetry = new Runnable() {
            @Override
            public void run() {
                requestTags();
            }
        };
        requestRetryHandler.removeCallbacks(requestRetry);

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
                requestRetryHandler.postDelayed(requestRetry, REQUEST_RETRY_DELAY);
            }
        };

        AIxNetworkManager.getInstance().requestTags(this, listener, errorListener);
    }

    public void requestCityLocations(City city){

        final Runnable requestRetry = new Runnable() {
            @Override
            public void run() {
                requestCityLocations(getCurrentCity());
            }
        };
        requestRetryHandler.removeCallbacks(requestRetry);

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
                requestRetryHandler.postDelayed(requestRetry, REQUEST_RETRY_DELAY);
            }
        };

        AIxNetworkManager.getInstance().requestCityLocations(this, listener, errorListener, city);
    }

    public void requestLocation(final int locationId){

        final Runnable requestRetry = new Runnable() {
            @Override
            public void run() {
                requestLocation(locationId);
            }
        };
        requestRetryHandler.removeCallbacks(requestRetry);

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
                requestRetryHandler.postDelayed(requestRetry, REQUEST_RETRY_DELAY);
            }
        };

        AIxNetworkManager.getInstance().requestLocation(this, listener, errorListener, locationId);
    }

    public void refreshData() {
        AIxNetworkManager.getInstance().invalidateTagsCache();
        AIxNetworkManager.getInstance().invalidateCityLocationsCache(currentCity);
        requestTags();
        requestCityLocations(currentCity);
    }
}
