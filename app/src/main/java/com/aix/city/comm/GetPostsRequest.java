package com.aix.city.comm;

import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Post;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 01.12.2015.
 */
public class GetPostsRequest extends AIxJsonRequest<Post[]> {

    private final int postNum;
    private final Post lastPost;
    private final ListingSource listingSource;

    public GetPostsRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost, ListingSource listingSource){
        super(Request.Method.GET, URLFactory.get().createGetPostsURL(postNum, lastPost, listingSource), null, Post[].class, listener, errorListener, false);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.listingSource = listingSource;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    public int getPostNum() {
        return postNum;
    }

    public Post getLastPost() {
        return lastPost;
    }

    public ListingSource getListingSource() {
        return listingSource;
    }
}