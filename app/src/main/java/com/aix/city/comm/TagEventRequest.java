package com.aix.city.comm;

import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 03.11.2015.
 */
public class TagEventRequest extends AIxRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private Tag tag;

    public TagEventRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                Tag tag){
        super(Request.Method.GET, URLFactory.get().createGetTagEventsURL(postNum, lastPost, tag), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.tag = tag;
    }

    public int getPostNum() {
        return postNum;
    }

    public Post getLastPost() {
        return lastPost;
    }

    public Tag getAIxTag(){
        return  tag;
    }
}
