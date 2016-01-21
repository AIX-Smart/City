package com.aix.city.comm;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.EditableListing;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.City;
import com.aix.city.core.data.Comment;
import com.aix.city.core.data.Event;
import com.aix.city.core.Likeable;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 30.11.2015.
 */
public class URLFactory {

    public static final String SCHEME = "http";
    public static final String HOST = "www.citevents.de";
    public static final int PORT = 8080;
    public static final String SERVICE = "service";
    public static final String LOCATION = ListingSourceType.LOCATION.name().toLowerCase(); //"location"
    public static final String CITY = ListingSourceType.CITY.name().toLowerCase(); //"city"
    public static final String TAG = ListingSourceType.TAG.name().toLowerCase(); //"tag"
    public static final String EVENT = ListingSourceType.EVENT.name().toLowerCase(); //"event"
    public static final String COMMENT = "comment";
    public static final String USER = "user";
    public static final String ALL = "all";
    public static final String POPULAR = "popular";
    public static final String NEW = "new";

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

    public String createGetPostsURL(int postNum, Post lastPost, ListingSource listingSource, PostListing.Order order){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder();
        urlBuilder.addPathSegment(listingSource.getType().name().toLowerCase());
        if(listingSource.getType() == ListingSourceType.TAG){
            urlBuilder.addPathSegment(String.valueOf(AIxDataManager.getInstance().getCurrentCity().getId()));
        }
        urlBuilder.addPathSegment(String.valueOf(listingSource.getId()));
        urlBuilder.addPathSegment(String.valueOf(postNum));
        urlBuilder.addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getId()));
        }
        if (order != null){
            switch(order){
                case NEWEST_FIRST:
                    urlBuilder.addPathSegment(NEW);
                    break;
                case POPULAR_FIRST:
                    urlBuilder.addPathSegment(POPULAR);
                    break;
            }
        }
        return urlBuilder.build().toString();
    }

    public String createGetPopularPostsURL(int postNum, Post lastPost, ListingSource listingSource){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder();
        urlBuilder.addPathSegment(listingSource.getType().name().toLowerCase());
        if(listingSource.getType() == ListingSourceType.TAG){
            urlBuilder.addPathSegment(String.valueOf(AIxDataManager.getInstance().getCurrentCity().getId()));
        }
        urlBuilder.addPathSegment(String.valueOf(listingSource.getId()));
        urlBuilder.addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        urlBuilder.addPathSegment(String.valueOf(postNum));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getId()));
        }
        urlBuilder.addPathSegment(POPULAR);
        return urlBuilder.build().toString();
    }

    public String createUpToDateURL(Post newestPost, ListingSource listingSource) {
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder();
        urlBuilder.addPathSegment(listingSource.getType().name().toLowerCase());
        if(listingSource.getType() == ListingSourceType.TAG){
            urlBuilder.addPathSegment(String.valueOf(AIxDataManager.getInstance().getCurrentCity().getId()));
        }
        urlBuilder.addPathSegment(String.valueOf(listingSource.getId()));
        urlBuilder.addPathSegment(String.valueOf(newestPost.getId()));
        return urlBuilder.build().toString();
    }

    /*public String createGetLocationEventsURL(int postNum, Post lastPost, Location location){
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
    }*/

    public String createGetCityLocationsURL(City city){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder()
                .addPathSegment(CITY)
                .addPathSegment(String.valueOf(city.getId()))
                .addPathSegment(LOCATION);
        return urlBuilder.build().toString();
    }

    public String createGetLocationURL(int locationId) {
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder();
        urlBuilder.addPathSegment(LOCATION);
        urlBuilder.addPathSegment(String.valueOf(locationId));
        return urlBuilder.build().toString();
    }

    public String createPostCreationURL(EditableListing postListing){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder();
        urlBuilder.addPathSegment(postListing.getListingSource().getType().name().toLowerCase());
        urlBuilder.addPathSegment(String.valueOf(postListing.getListingSource().getId()));
        urlBuilder.addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
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
                .addPathSegment(TAG)
                .addPathSegment(ALL);
        return urlBuilder.build().toString();
    }

    public String createLikeChangeURL(Likeable likeable){
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder();
        if(likeable instanceof Event){
            urlBuilder.addPathSegment(EVENT);
        }
        else if(likeable instanceof Comment){
            urlBuilder.addPathSegment(COMMENT);
        }
        else if(likeable instanceof Location){
            urlBuilder.addPathSegment(LOCATION);
        }
        urlBuilder.addPathSegment(String.valueOf(likeable.getId()));
        urlBuilder.addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        return urlBuilder.build().toString();
    }

    public String createDeletePostURL(Post post) {
        HttpUrl.Builder urlBuilder = serviceUrl.newBuilder();
        if (post instanceof Event){
            urlBuilder.addPathSegment(EVENT);
        }
        else if (post instanceof Comment){
            urlBuilder.addPathSegment(COMMENT);
        }
        urlBuilder.addPathSegment(String.valueOf(post.getId()));
        urlBuilder.addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser()));
        return urlBuilder.build().toString();
    }
}
