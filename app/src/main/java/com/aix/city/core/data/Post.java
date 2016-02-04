package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.Likeable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by Thomas on 11.10.2015.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Post extends Likeable implements Parcelable {

    public static final int MAX_CONTENT_LENGTH = 140;
    public static final int PARCEL_DESCRIPTION_EVENT = 1;
    public static final int PARCEL_DESCRIPTION_COMMENT = PARCEL_DESCRIPTION_EVENT + 4;

    private int id;
    private String content;
    private long creationTime;
    private int authorId;
    private boolean isAuthenticated;

    //no-argument constructor for JSON
    protected Post(){
    }

    public Post(int id, String content, long creationTime, int authorId, boolean liked, int likeCount) {
        super(liked, likeCount);
        this.id = id;
        this.content = content;
        this.creationTime = creationTime;
        this.authorId = authorId;
    }

    //implements Parcelable
    public Post(Parcel in){
        super(in);
        id = in.readInt();
        content = in.readString();
        creationTime = in.readLong();
        authorId = in.readInt();
    }

    public String getContent() {
        if(content == null){
            content = "";
        }
        return content;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getId() {
        return id;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @JsonIgnore
    public abstract boolean isComment();

    @JsonIgnore
    public boolean isDeletionAllowed(){
        return AIxLoginModule.getInstance().getLoggedInUser().getId() == authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return id == post.id;

    }

    @Override
    public int hashCode() {
        return 67*id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.describeContents());
        super.writeToParcel(dest, flags);
        dest.writeInt(getId());
        dest.writeString(getContent());
        dest.writeLong(getCreationTime());
        dest.writeInt(getAuthorId());
    }

    public static final Parcelable.Creator<Post> CREATOR =
            new Parcelable.Creator<Post>(){

                @Override
                public Post createFromParcel(Parcel source) {
                    int classDescription = source.readInt();
                    switch(classDescription){
                        case PARCEL_DESCRIPTION_EVENT:
                            return new Event(source);
                        case PARCEL_DESCRIPTION_COMMENT:
                            return new Comment(source);
                    }
                    return null;
                }

                @Override
                public Post[] newArray(int size) {
                    return new Post[size];
                }
            };
}
