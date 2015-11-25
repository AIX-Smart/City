package com.aix.city.comm;

import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.data.City;
import com.aix.city.core.data.Post;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 03.11.2015.
 */
public class CityEventRequest extends AIxJacksonRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private City city;

    public CityEventRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                City city){
        super(Request.Method.GET, createURL(postNum, lastPost, city), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.city = city;
    }


    private static String createURL(int postNum, Post lastPost, City city){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.CITY)
                .addPathSegment(String.valueOf(city.getId()))
                .addPathSegment(String.valueOf(postNum))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getId()));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getId()));
        }
        return urlBuilder.build().toString();
    }

    public int getPostNum() {
        return postNum;
    }

    public Post getLastPost() {
        return lastPost;
    }

    public City getCity() {
        return city;
    }
}
