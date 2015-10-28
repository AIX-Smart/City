package com.aix.city.core;

import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class CityData {

    private City city;
    private Set<Location> locations;

    /**
     * INTERNAL USE ONLY: use instead city.getData() or DataManager.getInstance().createCityData(...)
     */
    public CityData(City city, Set<Location> locations) {
        this.city = city;
        this.locations = locations;
    }

    public City getCity() {
        return city;
    }

    public Set<Location> getLocations() {
        return locations;
    }
}
