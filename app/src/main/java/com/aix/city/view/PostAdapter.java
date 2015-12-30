package com.aix.city.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.aix.city.PostListingFragment;
import com.aix.city.R;
import com.aix.city.core.data.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 17.10.2015.
 */
public class PostAdapter extends ArrayAdapter<Post>{
    private final PostListingFragment fragment;
    private final List<Post> posts;
    private final List<PostView> viewList = new ArrayList<PostView>();


    static class ViewHolder {
        TextView contentView;
        TextView locationNameView;
        TextView commentCounterView;
        Button likeButton;
    }

    public PostAdapter(PostListingFragment fragment, List<Post> posts) {
        super(fragment.getContext(), -1, posts);
        this.fragment = fragment;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        PostView postView = (PostView) convertView;

        if(postView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            postView = (PostView) inflater.inflate(R.layout.post, parent, false);

            holder = new ViewHolder();
            holder.contentView = (TextView) postView.findViewById(R.id.content);
            holder.locationNameView = (TextView) postView.findViewById(R.id.locationName);
            holder.commentCounterView = (TextView) postView.findViewById(R.id.commentCounter);
            holder.likeButton = (Button) postView.findViewById(R.id.likeButton);
            postView.setTag(holder);
            postView.init(this);
            viewList.add(postView);
        }

        final Post post = posts.get(position);
        postView.setPost(post);
        postView.update();

        return postView;
    }

    public void updateVisibleViews(){
        for (PostView v : viewList){
            v.update();
        }
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void deletePost(Post post){
        fragment.deletePost(post);
    }
}


