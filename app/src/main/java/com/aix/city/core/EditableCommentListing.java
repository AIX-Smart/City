package com.aix.city.core;

import com.aix.city.core.data.Comment;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.User;

/**
 * Created by Thomas on 14.10.2015.
 */
public class EditableCommentListing extends EditableListing {

    private Event event;

    /**
     * INTERNAL USE ONLY: use instead event.createPostListing()
     *
     * @param listingSource
     */
    public EditableCommentListing(ListingSource listingSource) {
        super(listingSource);
        if (listingSource instanceof Event) {
            event = (Event) getListingSource();
        }
    }

    public Event getEvent() {
        return event;
    }

    public Comment createComment(String message) {
        int ID = 1; //TODO: getId from server
        User user = AIxLoginModule.getInstance().getLoggedInUser();
        long now = System.currentTimeMillis();
        Comment comment = new Comment(ID, message, now, 0, user.getId(), false, event.getId());
        //TODO: event modification?
        this.addPost(comment);
        //TODO: Add Post to database
        return comment;
    }

    public boolean deleteComment(Comment comment) {
        if (comment.getEventId() == event.getId()) {
            this.removePost(comment);
            comment.rawDelete();
            //TODO: commit deletion to database
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Post createPost(String content) {
        return createComment(content);
    }

    @Override
    public boolean deletePost(Post post) {
        if(post instanceof Comment){
            return deleteComment((Comment) post);
        }
        else{
            return false;
        }
    }
}
