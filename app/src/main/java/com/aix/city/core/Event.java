package com.aix.city.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.comm.EventCommentRequest;
import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Event extends Post implements ListingSource {

    private Location location;
    private int commentCount;
    private boolean commented;

    //no-argument constructor for JSON
    private Event(){}

    //Parcelable constructor
    public Event(Parcel in){
        super(in.readLong(), in.readString(), in.readLong(), in.readInt(), in.readLong(), (in.readInt() != 0));
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.commentCount = in.readInt();
        this.commented = (in.readInt() != 0);
    }

    /**
     * INTERNAL USE ONLY: use instead location.getPostListing().createEvent(String message)
     */
    public Event(long postID, String message, long creationTime, int likeCount, long authorId, boolean likeStatus, Location location, int commentCount, boolean commented) {
        super(postID, message, creationTime, likeCount, authorId, likeStatus);
        this.location = location;
        this.commentCount = commentCount;
        this.commented = commented;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public int getCommentCount() {
        return commentCount;
    }

    /**
     * @return returns true if this event has been commented by the logged in user
     */
    public boolean isCommented() {
        return commented;
    }

    public void setCommented(boolean commented) {
        this.commented = commented;
    }

    @Override
    public void update() {
        super.update();
        //TODO: update post from database
    }

    @Override
    public boolean isComment(){
        return false;
    }

    @Override
    public Request getRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost) {
        return new EventCommentRequest(listener, errorListener, ignoreCache, postNum, lastPost, this);
    }

    @Override
    public Predicate<Post> getFilter() {
        //TODO: Implementation
        return null;
    }

    @Override
    public EditableCommentListing getPostListing() {
        return new EditableCommentListing(this);
    }

    @Override
    public ListingSourceType getType() {
        return ListingSourceType.EVENT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getID());
        dest.writeString(getMessage());
        dest.writeLong(getCreationTime());
        dest.writeInt(getLikeCount());
        dest.writeLong(getAuthorId());
        dest.writeInt(isLiked() ? 1 : 0);
        dest.writeParcelable(getLocation(), flags);
        dest.writeInt(getCommentCount());
        dest.writeInt(isCommented() ? 1 : 0);
    }

    public static final Parcelable.Creator<Event> CREATOR =
            new Parcelable.Creator<Event>(){

                @Override
                public Event createFromParcel(Parcel source) {
                    return new Event(source);
                }

                @Override
                public Event[] newArray(int size) {
                    return new Event[size];
                }
            };
}
