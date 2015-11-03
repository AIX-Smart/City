package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
public abstract class Post{

    private long id;
    private String message;
    private long creationTime;
    private int likeCount;
    private long authorId;
    private boolean liked; //current user has already liked this post
    private transient boolean deleted;

    //no-argument constructor for JSON
    protected Post(){
        deleted = false;
    }

    public Post(long id, String message, long creationTime, int likeCount, long authorId, boolean liked) {
        this.id = id;
        this.message = message;
        this.creationTime = creationTime;
        this.likeCount = likeCount;
        this.authorId = authorId;
        this.liked = liked;
        this.deleted = false;
    }

    public String getMessage() {
        return message;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public long getAuthorId() {
        return authorId;
    }

    public long getID() {
        return id;
    }

    public abstract Location getLocation();

    public abstract boolean isComment();

    public boolean isLiked() {
        return liked;
    }

    public boolean isDeleted() {
        return deleted;
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
        return (int) (id ^ (id >>> 32));
    }
}
