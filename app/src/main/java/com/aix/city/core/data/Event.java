package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.comm.GetPostsRequest;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.EditableCommentListing;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
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
     * INTERNAL USE ONLY: use instead location.createPostListing().createEvent(String message)
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
    public void requestPosts(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost) {
        AIxNetworkManager.getInstance().requestPosts(listener, errorListener, postNum, lastPost, this);
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
