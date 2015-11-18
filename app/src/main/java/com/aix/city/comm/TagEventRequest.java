package com.aix.city.comm;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.okhttp.HttpUrl;

/**
 * Created by Thomas on 03.11.2015.
 */
public class TagEventRequest extends AIxJacksonRequest<Post[]> {

    private int postNum;
    private Post lastPost;
    private Tag tag;

    public TagEventRequest(Response.Listener<Post[]> listener,
                                Response.ErrorListener errorListener,
                                boolean ignoreCache,
                                int postNum,
                                Post lastPost,
                                Tag tag){
        super(Request.Method.GET, getURL(postNum, lastPost, tag), null, Post[].class, listener, errorListener, ignoreCache);
        this.postNum = postNum;
        this.lastPost = lastPost;
        this.tag = tag;
    }


    private static String getURL(int postNum, Post lastPost, Tag tag){
        HttpUrl.Builder urlBuilder = AIxNetworkManager.getInstance().getServiceUrl().newBuilder()
                .addPathSegment(URLSegments.TAG)
                .addPathSegment(String.valueOf(tag.getId()))
                .addPathSegment(String.valueOf(AIxDataManager.getInstance().getCurrentCity().getId()))
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

    public Tag getAIxTag(){
        return  tag;
    }
}
