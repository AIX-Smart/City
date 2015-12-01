package com.aix.city.core.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by Thomas on 11.10.2015.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Post{

    public static final int MAX_CONTENT_LENGTH = 140;

    private int id;
    private String content;
    private long creationTime;
    private int likeCount;
    private int authorId;
    private boolean liked; //current user has already liked this post
    private transient boolean deleted = false;
    private transient boolean locallyStored = false;

    //no-argument constructor for JSON
    protected Post(){
    }

    public Post(int id, String content, long creationTime, int likeCount, int authorId, boolean liked) {
        this.id = id;
        this.content = content;
        this.creationTime = creationTime;
        this.likeCount = likeCount;
        this.authorId = authorId;
        this.liked = liked;
        this.locallyStored = true;
    }

    public String getContent() {
        return content;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public abstract String getSourceName();

    @JsonIgnore
    public abstract boolean isComment();

    public boolean isLiked() {
        return liked;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    @JsonIgnore
    public boolean isLocallyStored() {
        return locallyStored;
    }

    public void like() {
        if (!liked) {
            liked = true;
            if (likeCount < Integer.MAX_VALUE) likeCount++;
            //TODO: Server Communication
        }
    }

    public void resetLike() {
        if (liked) {
            liked = false;
            if (likeCount > 0) likeCount--;
            //TODO: Server Communication
        }
    }

    public void update() {
        //TODO: update post from database
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
}
