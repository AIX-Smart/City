package com.aix.city.core.data;

import android.os.Parcel;

import com.aix.city.core.Likeable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by Thomas on 11.10.2015.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Post extends Likeable {

    public static final int MAX_CONTENT_LENGTH = 140;

    private int id;
    private String content;
    private long creationTime;
    private int authorId;
    private transient boolean deleted = false;

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

    @JsonIgnore
    public abstract boolean isComment();

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * INTERNAL USE ONLY:
     */
    public void rawDelete() {
        deleted = true;
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

    //implements Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(getId());
        dest.writeString(getContent());
        dest.writeLong(getCreationTime());
        dest.writeInt(getAuthorId());
    }
}
