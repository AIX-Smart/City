package com.aix.city.comm;

import com.aix.city.core.data.City;
import com.aix.city.core.data.Post;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 03.11.2015.
 */
public class CityEventRequest extends AIxRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private City city;

    public CityEventRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                City city){
        super(Request.Method.GET, URLFactory.get().createCityEventsURL(postNum, lastPost, city), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.city = city;
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
