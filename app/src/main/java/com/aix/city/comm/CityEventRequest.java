package com.aix.city.comm;

import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.City;
import com.aix.city.core.Post;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 03.11.2015.
 */
public class CityEventRequest extends JacksonRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private City city;

    public CityEventRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                City city){
        super(Request.Method.GET, getURL(postNum, lastPost, city), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.city = city;
    }


    private static String getURL(int postNum, Post lastPost, City city){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.CITY)
                .addPathSegment(String.valueOf(city.getID()))
                .addPathSegment(String.valueOf(postNum))
                .addPathSegment(String.valueOf(AIxLoginModule.getInstance().getLoggedInUser().getID()));
        if(lastPost != null){
            urlBuilder.addPathSegment(String.valueOf(lastPost.getID()));
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
