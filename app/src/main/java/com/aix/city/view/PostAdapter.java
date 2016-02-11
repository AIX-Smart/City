package com.aix.city.view;

import android.content.Context;
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
    private static final List<Integer> POST_COLORS = new ArrayList<Integer>();

    private final PostListingFragment fragment;
    private final List<Post> posts;
    private final Map<Post, Integer> postColorMap_newest = new HashMap<Post, Integer>();
    private final Map<Post, Integer> postColorMap_popular = new HashMap<Post, Integer>();
    private Map<Post, Integer> postColorMap = postColorMap_newest;
    private final Set<PostView> createdViews = new HashSet<PostView>();

    private Post firstColoredPost = null;
    private Post lastColoredPost = null;

    public PostAdapter(PostListingFragment fragment, List<Post> posts) {
        this(fragment, posts, null, PostListingFragment.DEFAULT_COLOR_VALUE);
    }

    public PostAdapter(PostListingFragment fragment, List<Post> posts, Post linkedPost, int postColor) {
        super(fragment.getContext(), -1, posts);
        this.fragment = fragment;
        this.posts = posts;
        if (linkedPost != null){
            postColorMap.put(linkedPost, postColor);
            firstColoredPost = linkedPost;
            lastColoredPost = linkedPost;
        }

        if (POST_COLORS.isEmpty()){
            POST_COLORS.add(fragment.getContext().getResources().getColor(R.color.post_color_blue));
            POST_COLORS.add(fragment.getContext().getResources().getColor(R.color.post_color_yellow));
            POST_COLORS.add(fragment.getContext().getResources().getColor(R.color.post_color_green));
            POST_COLORS.add(fragment.getContext().getResources().getColor(R.color.post_color_red));
        }
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

    public void dyePost(final Post post){
        if (!postColorMap.containsKey(post)) {

            if (!posts.contains(post)){
                throw new IllegalArgumentException();
            }
            if (firstColoredPost != null && !posts.contains(firstColoredPost)){
                postColorMap.clear();
                firstColoredPost = null;
                lastColoredPost = null;
            }

            int colorIndex;
            int color;

            if (postColorMap.isEmpty()) {
                colorIndex = Math.abs(new Random().nextInt()) % POST_COLORS.size();
                color = POST_COLORS.get(colorIndex);
                firstColoredPost = posts.get(0);
                lastColoredPost = firstColoredPost;
                postColorMap.put(firstColoredPost, color);
            }

            int firstCPostIndex = posts.indexOf(firstColoredPost);
            int lastCPostIndex = posts.indexOf(lastColoredPost);

            colorIndex = POST_COLORS.indexOf(postColorMap.get(firstColoredPost));
            for (int i = firstCPostIndex - 1; i >= 0; i--) {
                colorIndex--;
                if (colorIndex == -1) {
                    colorIndex = POST_COLORS.size() - 1;
                }

                color = POST_COLORS.get(colorIndex);
                postColorMap.put(posts.get(i), color);
            }

            colorIndex = POST_COLORS.indexOf(postColorMap.get(lastColoredPost));
            for (int i = lastCPostIndex + 1; i < posts.size(); i++) {
                colorIndex++;
                if (colorIndex == POST_COLORS.size()) {
                    colorIndex = 0;
                }

                color = POST_COLORS.get(colorIndex);
                postColorMap.put(posts.get(i), color);
            }

        }
    }

    @Override
    public int getPostColor(Post post){
        int index = post.hashCode() % POST_COLORS.size();
        return POST_COLORS.get(index);
        //return postColorMap.get(post);
    }

    @Override
    public void putPostColor(Post post, int color){
        postColorMap.put(post, color);
    }

    @Override
    public AIxMainActivity getActivity() {
        return (AIxMainActivity) fragment.getActivity();
    }

    @Override
    public void startActivity(ListingSource listingSource){
        fragment.startActivity(listingSource, PostListingFragment.DEFAULT_COLOR_VALUE, null);
    }

    @Override
    public void startActivity(ListingSource listingSource, int postColor, Post linkedPost) {
        fragment.startActivity(listingSource, postColor, linkedPost);
    }

    @Override
    public Location getSourceLocation() {
        return null;
    }

    public List<Post> getPosts() {
        return posts;
    }

    @Override
    public void notifyDataSetChanged() {
        postColorMap_popular.clear();
        super.notifyDataSetChanged();
    }

    public void setOrder(PostListing.Order order){
        switch (order){
            case NEWEST_FIRST:
                postColorMap = postColorMap_newest;
                break;
            case POPULAR_FIRST:
                postColorMap = postColorMap_popular;
                break;
        }
    }

}


