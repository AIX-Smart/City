package com.aix.city.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.aix.city.BaseListingActivity;
import com.aix.city.PostListingFragment;
import com.aix.city.R;
import com.aix.city.core.ListingSource;
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
    private Map<Post, Integer> postColorMap = new HashMap<Post, Integer>();
    private final Set<PostView> createdViews = new HashSet<PostView>();

    private Post firstColoredPost = null;
    private Post lastColoredPost = null;

    public PostAdapter(PostListingFragment fragment, List<Post> posts) {
        super(fragment.getContext(), -1, posts);
        this.fragment = fragment;
        this.posts = posts;

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
        dyePost(post);
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
        return postColorMap.get(post);
    }

    @Override
    public void putPostColor(Post post, int color){
        postColorMap.put(post, color);
    }

    @Override
    public BaseListingActivity getActivity() {
        return (BaseListingActivity) fragment.getActivity();
    }

    @Override
    public void startActivity(ListingSource listingSource){
        getActivity().startActivity(listingSource);
    }

    @Override
    public void startActivity(ListingSource listingSource, int postColor){
        getActivity().startActivity(listingSource, postColor);
    }

    public List<Post> getPosts() {
        return posts;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}


