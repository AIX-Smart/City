package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.comm.EventCommentRequest;
import com.aix.city.core.EditableCommentListing;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.android.internal.util.Predicate;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Event extends Post implements ListingSource {

    private Location location;
    private int commentCount;

    //no-argument constructor for JSON
    private Event(){}

    //Parcelable constructor
    public Event(Parcel in){
        super(in.readInt(), in.readString(), in.readLong(), in.readInt(), in.readInt(), (in.readInt() != 0));
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.commentCount = in.readInt();
    }

    /**
     * INTERNAL USE ONLY: use instead location.getPostListing().createEvent(String message)
     */
    public Event(int postID, String message, long creationTime, int likeCount, int authorId, boolean likeStatus, Location location, int commentCount) {
        super(postID, message, creationTime, likeCount, authorId, likeStatus);
        this.location = location;
        this.commentCount = commentCount;
    }

    public Location getLocation() {
        return location;
    }

    public int getCommentCount() {
        return commentCount;
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
    public String getSourceName() {
        return location.getName();
    }

    @Override
    public Request getRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost) {
        return new EventCommentRequest(listener, errorListener, ignoreCache, postNum, lastPost, this);
    }

    @Override
    public Predicate<Post> getFilter() {
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
        dest.writeInt(getId());
        dest.writeString(getContent());
        dest.writeLong(getCreationTime());
        dest.writeInt(getLikeCount());
        dest.writeInt(getAuthorId());
        dest.writeInt(isLiked() ? 1 : 0);
        dest.writeParcelable(getLocation(), flags);
        dest.writeInt(getCommentCount());
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
