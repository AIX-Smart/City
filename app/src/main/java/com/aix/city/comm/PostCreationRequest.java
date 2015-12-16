package com.aix.city.comm;

import com.aix.city.core.EditableEventListing;
import com.aix.city.core.EditableListing;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.Location;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Thomas on 01.12.2015.
 */
public class PostCreationRequest extends AIxJsonRequest<String> {

    private final String content;
    private final EditableListing postListing;

    public PostCreationRequest(Response.Listener<String> listener, Response.ErrorListener errorListener, EditableListing postListing, String content){
        super(Request.Method.POST, URLFactory.get().createPostCreationURL(postListing), content, String.class, listener, errorListener, false);
        this.content = content;
        this.postListing = postListing;
    }

    @Override
    public Priority getPriority() {
        return Priority.IMMEDIATE;
    }

    public String getContent() {
        return content;
    }

    public EditableListing getPostListing() {
        return postListing;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success("", null);
        }
        catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}