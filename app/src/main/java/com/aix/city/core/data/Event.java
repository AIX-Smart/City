package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.EditableCommentListing;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Event extends Post implements ListingSource {

    private int locationId;
    private int commentCount;

    //no-argument constructor for JSON
    private Event(){}

    //Parcelable constructor
    public Event(Parcel in){
        super(in);
        this.locationId = in.readInt();
        this.commentCount = in.readInt();
    }

    /**
     * INTERNAL USE ONLY: use instead location.createPostListing().createPost(String message)
     */
    public Event(int postID, String message, long creationTime, int authorId, boolean liked, int likeCount, int locationId, int commentCount) {
        super(postID, message, creationTime, authorId, liked, likeCount);
        this.locationId = locationId;
        this.commentCount = commentCount;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @JsonIgnore
    public Location getLocation(){
        return AIxDataManager.getInstance().getLocation(locationId);
    }

    @Override
    public boolean isComment(){
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return true;//super.isAuthenticated();
    }

    @Override
    public boolean isDeletionAllowed(){
        return super.isDeletionAllowed() || getLocation().isAuthorized();
    }

    @Override
    public EditableCommentListing createPostListing() {
        return new EditableCommentListing(this);
    }

    @Override
    public ListingSourceType getType() {
        return ListingSourceType.EVENT;
    }

    @Override
    public int describeContents() {
        return getType().getParcelDescription();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(getLocationId());
        dest.writeInt(getCommentCount());
    }

    public static final Parcelable.Creator<Event> CREATOR =
            new Parcelable.Creator<Event>(){

                @Override
                public Event createFromParcel(Parcel source) {
                    //read class description
                    source.readInt();
                    return new Event(source);
                }

                @Override
                public Event[] newArray(int size) {
                    return new Event[size];
                }
            };
}
