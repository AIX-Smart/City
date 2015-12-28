package com.aix.city.comm;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.data.Post;
import com.android.volley.Response;

/**
 * Created by Thomas on 28.12.2015.
 */
public class DeletePostRequest extends AIxJsonRequest {


    public DeletePostRequest(Response.Listener<Post> listener, Response.ErrorListener errorListener, Post post) {
        super(Method.DELETE, URLFactory.get().createDeletePostURL(post), null, Post.class, listener, errorListener, false);
    }
}
