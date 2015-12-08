package com.aix.city.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.core.AIxNetworkManager;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Observable;

/**
 * Created by Thomas on 05.12.2015.
 */
public abstract class Likeable extends Observable {

    private boolean liked; //current user has already liked this post
    private int likeCount;

    public Likeable(){

    }

    public Likeable(boolean liked, int likeCount) {
        this.liked = liked;
        this.likeCount = likeCount;
    }

    //implements Parcelable
    public Likeable(Parcel in){
        liked = (in.readInt() != 0);
        likeCount = in.readInt();
    }

    public boolean isLiked() {
        return liked;
    }

    protected void setLiked(boolean liked) {
        if(this.liked != liked){
            this.liked = liked;
            setChanged();
        }
    }

    public int getLikeCount() {
        return likeCount;
    }

    protected void setLikeCount(int likeCount) {
        if(likeCount != likeCount){
            this.likeCount = likeCount;
            setChanged();
        }
    }

    public abstract int getId();

    public void like() {
        if (isLiked()) {
            Response.Listener<String> listener = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    setLiked(true);
                    if (getLikeCount() < Integer.MAX_VALUE) setLikeCount(getLikeCount() + 1);
                    notifyObservers();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    notifyObservers();
                }
            };

            AIxNetworkManager.getInstance().requestLikeChange(listener, errorListener, this, true);
        }
    }

    public void resetLike() {
        if (isLiked()) {
            Response.Listener<String> listener = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    setLiked(false);
                    if (getLikeCount() > 0) setLikeCount(getLikeCount() - 1);
                    notifyObservers();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    notifyObservers();
                }
            };

            AIxNetworkManager.getInstance().requestLikeChange(listener, errorListener, this, false);
        }
    }

    //implements Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isLiked() ? 1 : 0);
        dest.writeInt(getLikeCount());
    }
}