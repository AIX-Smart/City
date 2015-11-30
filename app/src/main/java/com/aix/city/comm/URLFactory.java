package com.aix.city.comm;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.data.City;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 30.11.2015.
 */
public class URLFactory {

    public static final String SCHEME = "http";
    public static final String HOST = "www.citevents.de";
    public static final int PORT = 8080;
    public static final String SERVICE = "service";
    public static final String LOCATION = "location";
    public static final String LOCATIONS = "locations";
    public static final String CITY = "city";
    public static final String TAG = "tag";
    public static final String EVENT = "event";
    public static final String USER = "user";

    //"http://www.citevents.de:8080/service" as HttpUrl
    private HttpUrl serviceUrl;


    private URLFactory() {
        serviceUrl = new HttpUrl.Builder()
                .scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .addPathSegment(SERVICE)
                .build();
    }

    private static class SingletonHolder {
        public static final URLFactory instance = new URLFactory();
    }

    //getInstance()
    public static URLFactory get() {
        return SingletonHolder.instance;
    }


    public HttpUrl getServiceUrl() {
        return serviceUrl;
    }

    public String createCityEventsURL(int postNum, Post lastPost, City city){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(CITY)
                .addPathSegment(String.valueOf(city.getId()))
                .addPathSegment(String.valueOf(postNum))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getId()));
        }
        return urlBuilder.build().toString();
    }

    //TODO:
    public String createCityLocationsURL(City city){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(LOCATIONS)
                .addPathSegment(String.valueOf(city.getId()));
        return urlBuilder.build().toString();
    }

    public String createCommentCreationURL(Event event){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(EVENT)
                .addPathSegment(String.valueOf(event.getId()))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        return urlBuilder.build().toString();
    }

    public String createEventCreationURL(Location location){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(LOCATION)
                .addPathSegment(String.valueOf(location.getId()))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        return urlBuilder.build().toString();
    }

    public String createGetEventCommentsURL(int postNum, Post lastPost, Event event){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(EVENT)
                .addPathSegment(String.valueOf(event.getId()))
                .addPathSegment(String.valueOf(postNum))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getId()));
        }
        return urlBuilder.build().toString();
    }

    public String createGetLocationEventsURL(int postNum, Post lastPost, Location location){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(LOCATION)
                .addPathSegment(String.valueOf(location.getId()))
                .addPathSegment(String.valueOf(postNum))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getId()));
        }
        return urlBuilder.build().toString();
    }

    public String createGetTagEventsURL(int postNum, Post lastPost, Tag tag){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(TAG)
                .addPathSegment(String.valueOf(tag.getId()))
                .addPathSegment(String.valueOf(AIxDataManager.getInstance().getCurrentCity().getId()))
                .addPathSegment(String.valueOf(postNum))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getId()));
        }
        return urlBuilder.build().toString();
    }

    public String createLoginURL(String deviceId){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(USER)
                .addPathSegment(deviceId);
        return urlBuilder.build().toString();
    }

    public String createGetAllTagsURL(){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(TAG);
        return urlBuilder.build().toString();
    }
}
