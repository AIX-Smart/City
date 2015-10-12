package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
public abstract class Post {

    private long postID;
    private String message;
    private Timestamp creationTime;
    private int likeCount;
    private User author;
    private boolean likeStatus;

    protected Post(long postID, String message, Timestamp creationTime, int likeCount, User author, boolean likeStatus) {
        this.postID = postID;
        this.message = message;
        this.creationTime = creationTime;
        this.likeCount = likeCount;
        this.author = author;
        this.likeStatus = likeStatus;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public User getAuthor() {
        return author;
    }

    public long getPostID() {
        return postID;
    }

    public boolean getLikeStatus() {
        return likeStatus;
    }

    public void like() {
        if (!likeStatus) {
            likeStatus = true;
            if (likeCount < Integer.MAX_VALUE) likeCount++;
            //TODO: Server Communication
        }
    }

    public void resetLike() {
        if (likeStatus) {
            likeStatus = false;
            if (likeCount > 0) likeCount--;
            //TODO: Server Communication
        }
    }

    public void update() {
        //TODO: update post from database
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        return postID == post.postID;

    }

    @Override
    public int hashCode() {
        return (int) (postID ^ (postID >>> 32));
    }
}
