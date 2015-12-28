package com.aix.city.core;

import android.os.Parcel;

import com.aix.city.comm.AIxJsonRequest;
import com.aix.city.comm.PostCreationRequest;
import com.aix.city.core.data.Post;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Thomas on 16.11.2015.
 */
public abstract class EditableListing extends PostListing {

    //true if creation and deletion of posts is enabled; false otherwise
    private boolean editable = true;

    public EditableListing(ListingSource listingSource) {
        super(listingSource);
    }

    public EditableListing(Parcel in){
        super(in);
    }

    @Override
    public boolean createPost(String content){
        if(isEditable()){
            Response.Listener listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    refresh();
                    setEditable(true);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    setEditable(true);
                }
            };

            //send request to server
            AIxNetworkManager.getInstance().requestPostCreation(listener, errorListener, this, content);

            //waiting for response
            setEditable(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePost(Post post){
        if(isEditable()){
            Response.Listener listener = new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    refresh();
                    setEditable(true);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    setEditable(true);
                }
            };
            //send request to server
            AIxNetworkManager.getInstance().requestPostDeletion(listener, errorListener, post);

            //waiting for response
            setEditable(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable){
        if(this.editable != editable){
            this.editable = editable;
            setChanged();
            notifyObservers(OBSERVER_KEY_CHANGED_EDITABILITY);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
