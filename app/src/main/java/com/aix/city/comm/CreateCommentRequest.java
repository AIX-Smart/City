package com.aix.city.comm;

import com.aix.city.core.EditableCommentListing;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Thomas on 25.11.2015.
 */
public class CreateCommentRequest extends AIxRequest<String> {

    public CreateCommentRequest(final EditableCommentListing commentListing, String content){
        super(Request.Method.POST,
                URLFactory.get().createCommentCreationURL(commentListing.getEvent()),
                content,
                String.class,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        commentListing.refresh();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO:
                    }
                },
                false);
    }

}
