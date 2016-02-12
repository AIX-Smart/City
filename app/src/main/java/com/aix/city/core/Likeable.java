package com.aix.city.core;

import android.os.Parcel;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Observable;

/**
 * Created by Thomas on 05.12.2015.
 */
public abstract class Likeable extends Observable {

    public static final String OBSERVER_KEY_CHANGED_LIKESTATUS = "likeStatus";
    public static final String OBSERVER_KEY_LIKE_ERROR = "like.error";

    private boolean isLiked; //current user has already liked this post
    private int likeCount;

    public Likeable(){

    }

    public Likeable(boolean isLiked, int likeCount) {
        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }

    //implements Parcelable
    public Likeable(Parcel in){
        isLiked = (in.readInt() != 0);
        likeCount = in.readInt();
    }

    public boolean isLiked() {
        return isLiked;
    }

    /**Internal use only!*/
    public void rawSetIsLiked(boolean liked) {
        if(this.isLiked != liked){
            this.isLiked = liked;
            setChanged();
        }
    }

    public int getLikeCount() {
        return likeCount;
    }

    /**Internal use only!*/
    protected void rawSetLikeCount(int likeCount) {
        if(this.likeCount != likeCount){
            this.likeCount = likeCount;
            setChanged();
        }
    }

    public abstract int getId();

    public void setLike() {
        if (!isLiked()) {
            final int futureLikeCount = getLikeCount() + 1;
            Response.Listener<Boolean> listener = new Response.Listener<Boolean>(){
                @Override
                public void onResponse(Boolean response) {
                    if (!isLiked()) rawSetLikeCount(futureLikeCount);
                    rawSetIsLiked(true);
                    notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
                }
            };

            AIxNetworkManager.getInstance().requestLikeChange(this, listener, errorListener, this, true);
        }
    }

    public void resetLike() {
        if (isLiked()) {
            final int futureLikeCount = getLikeCount() - 1;
            Response.Listener<Boolean> listener = new Response.Listener<Boolean>(){
                @Override
                public void onResponse(Boolean response) {
                    if (isLiked()) rawSetLikeCount(futureLikeCount);
                    rawSetIsLiked(false);
                    notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    setChanged();
                    notifyObservers(OBSERVER_KEY_LIKE_ERROR);
                }
            };

            AIxNetworkManager.getInstance().requestLikeChange(this, listener, errorListener, this, false);
        }
    }

    public void updateLikeable(){
        Response.Listener<Boolean> likeStatusListener = new Response.Listener<Boolean>(){
            @Override
            public void onResponse(Boolean response) {
                rawSetIsLiked(response);
                notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
            }
        };
        Response.Listener<Integer> likeCountListener = new Response.Listener<Integer>(){
            @Override
            public void onResponse(Integer response) {
                rawSetLikeCount(response);
                notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                setChanged();
                notifyObservers(OBSERVER_KEY_LIKE_ERROR);
            }
        };

        AIxNetworkManager.getInstance().requestLikeStatus(this, likeStatusListener, errorListener, this);
        AIxNetworkManager.getInstance().requestLikeCount(this, likeCountListener, errorListener, this);
    }

    //implements Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isLiked() ? 1 : 0);
        dest.writeInt(getLikeCount());
    }
}
