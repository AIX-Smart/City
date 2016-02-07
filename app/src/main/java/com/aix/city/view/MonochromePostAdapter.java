package com.aix.city.view;

import com.aix.city.PostListingFragment;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;

import java.util.List;

/**
 * Created by Thomas on 01.02.2016.
 */
public class MonochromePostAdapter extends PostAdapter {

    private final int postColor;
    private Location sourceLocation;

    public MonochromePostAdapter(PostListingFragment fragment, List<Post> posts, int postColor) {
        super(fragment, posts);
        this.postColor = postColor;
    }

    public MonochromePostAdapter(PostListingFragment fragment, List<Post> posts, int postColor, Location location) {
        super(fragment, posts);
        this.postColor = postColor;
        this.sourceLocation = location;
    }

    @Override
    public Location getSourceLocation(){
        return sourceLocation;
    }


    @Override
    public void dyePost(Post post) {
        //do nothing
    }

    @Override
    public int getPostColor(Post post) {
        return postColor;
    }

    @Override
    public void putPostColor(Post post, int color) {
        //do nothing
    }
}
