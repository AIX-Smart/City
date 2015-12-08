package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Comment extends Post {

    int eventId;

    //no-argument constructor for JSON
    private Comment(){}

    /**
     * INTERNAL USE ONLY: use instead event.createPostListing().createPost(String message)
     */
    public Comment(int postID, String message, long creationTime, int authorId, boolean liked, int likeCount, int eventId) {
        super(postID, message, creationTime, authorId, liked, likeCount);
        this.eventId = eventId;
    }

    public Comment(Parcel in){
        super(in);
        eventId = in.readInt();
    }

    public int getEventId() {
        return eventId;
    }

    @Override
    public boolean isComment(){
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(eventId);
    }

    @Override
    public int describeContents() {
        return Post.PARCEL_DESCRIPTION_COMMENT;
    }

    public static final Parcelable.Creator<Comment> CREATOR =
            new Parcelable.Creator<Comment>(){

                @Override
                public Comment createFromParcel(Parcel source) {
                    //read class description
                    source.readInt();
                    return new Comment(source);
                }

                @Override
                public Comment[] newArray(int size) {
                    return new Comment[size];
                }
            };
}
