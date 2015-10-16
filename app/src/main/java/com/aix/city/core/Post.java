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
    private boolean liked; //current user has already liked this post
    private boolean deleted;

    public Post(long postID, String message, Timestamp creationTime, int likeCount, User author, boolean liked) {
        this.postID = postID;
        this.message = message;
        this.creationTime = creationTime;
        this.likeCount = likeCount;
        this.author = author;
        this.liked = liked;
        this.deleted = false;
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
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        return postID == post.postID;

    }

    @Override
    public int hashCode() {
        return (int) (postID ^ (postID >>> 32));
    }
}
