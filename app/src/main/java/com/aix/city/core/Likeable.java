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

    /**Internal use only!*/
    public void rawSetLiked(boolean liked) {
        if(this.liked != liked){
            this.liked = liked;
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
                    rawSetLiked(true);
                    notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
                }
            };

            AIxNetworkManager.getInstance().requestLikeChange(listener, errorListener, this, true);
        }
    }

    public void resetLike() {
        if (isLiked()) {
            final int futureLikeCount = getLikeCount() - 1;
            Response.Listener<Boolean> listener = new Response.Listener<Boolean>(){
                @Override
                public void onResponse(Boolean response) {
                    if (isLiked()) rawSetLikeCount(futureLikeCount);
                    rawSetLiked(false);
                    notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
                }
            };

            AIxNetworkManager.getInstance().requestLikeChange(listener, errorListener, this, false);
        }
    }

    public void updateLikeable(){
        Response.Listener<Boolean> likeStatusListener = new Response.Listener<Boolean>(){
            @Override
            public void onResponse(Boolean response) {
                rawSetLiked(response);
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
                notifyObservers(OBSERVER_KEY_CHANGED_LIKESTATUS);
            }
        };

        AIxNetworkManager.getInstance().requestLikeStatus(likeStatusListener, errorListener, this);
        AIxNetworkManager.getInstance().requestLikeCount(likeCountListener, errorListener, this);
    }

    //implements Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isLiked() ? 1 : 0);
        dest.writeInt(getLikeCount());
    }
}
