package com.aix.city.core.data;

import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 11.10.2015.
 */
public class CityData {

    private City city;
    private List<Location> locations;

    //no-argument constructor for JSON
    private CityData(){}

    /**
     * INTERNAL USE ONLY: use instead city.getData() or AIxDataManager.getInstance().createCity(...)
     */
    public CityData(City city, List<Location> locations) {
        this.city = city;
        this.locations = locations;
    }

    public City getCity() {
        return city;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
