package com.aix.city.core;

import com.aix.city.core.data.Post;

/**
 * Created by Thomas on 16.11.2015.
 */
public abstract class EditableListing extends PostListing {

    public EditableListing(ListingSource listingSource) {
        super(listingSource);
    }

    @Override
    public abstract boolean createPost(String content);

    @Override
    public abstract boolean deletePost(Post post);

    @Override
    public boolean isEditable() {
        return true;
    }
}
