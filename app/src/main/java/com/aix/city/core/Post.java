package com.aix.city.core;

import java.sql.Timestamp;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public abstract class Post {

    private String message;
    private Timestamp creationTime;
    private int likes;
    private User author;
    private long postID;
    private boolean isLiked;

    public String getMessage() {
        return message;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public int getLikes() {
        return likes;
    }

    public User getAuthor() {
        return author;
    }

    public long getPostID() {
        return postID;
    }

    public boolean isLiked() {
        return isLiked;
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
