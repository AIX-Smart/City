package com.aix.city.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.aix.city.AIxMainActivity;
import com.aix.city.PostListingFragment;
import com.aix.city.R;
import com.aix.city.core.ListingSource;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Thomas on 17.10.2015.
 */
public class PostAdapter extends ArrayAdapter<Post> implements PostViewContext {

    private final PostListingFragment fragment;
    private final List<Post> posts;
    private final Set<PostView> createdViews = new HashSet<PostView>();


    public PostAdapter(PostListingFragment fragment, List<Post> posts) {
        super(fragment.getContext(), -1, posts);
        this.fragment = fragment;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PostView postView = (PostView) convertView;

        if(postView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            postView = (PostView) inflater.inflate(R.layout.post, parent, false);

            postView.init(this);
            if (parent == fragment.getListView()){
                createdViews.add(postView);
            }
        }

        final Post post = getItem(position);
        postView.setPost(post);
        //dyePost(post);
        postView.update();

        return postView;
    }

    public void updateVisibleViews(){
        for (PostView v : createdViews){
            v.update();
        }
    }

    public PostView getVisibleView(Post post){
        for (PostView v : createdViews){
            if (v.getPost().equals(post)){
                return v;
            }
        }
        return null;
    }

    @Override
    public int getPostColor(Post post){
        return post.getColor();
    }

    @Override
    public void startActivity(ListingSource listingSource){
        fragment.startActivity(listingSource);
    }


    public List<Post> getPosts() {
        return posts;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}


