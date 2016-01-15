package com.aix.city.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aix.city.BaseListingActivity;
import com.aix.city.PostListingFragment;
import com.aix.city.R;
import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Post;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 17.10.2015.
 */
public class PostAdapter extends ArrayAdapter<Post>{
    private final PostListingFragment fragment;
    private final List<Post> posts;
    private final Set<PostView> createdViews = new HashSet<PostView>();


    static class ViewHolder {
        TextView contentView;
        TextView locationNameView;
        TextView commentCounterView;
        ImageButton likeButton;
        TextView likeCounter;
        TextView creationTime;
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
            holder.contentView = (TextView) postView.findViewById(R.id.post_content);
            holder.locationNameView = (TextView) postView.findViewById(R.id.post_location);
            holder.commentCounterView = (TextView) postView.findViewById(R.id.post_comments_counter);
            holder.likeButton = (ImageButton) postView.findViewById(R.id.post_like_btn);
            holder.likeCounter = (TextView) postView.findViewById(R.id.post_like_counter);
            holder.creationTime = (TextView) postView.findViewById(R.id.post_time);
            postView.setTag(holder);
            postView.init(this);
            if (!createdViews.contains(postView)){
                createdViews.add(postView);
            }
        }

        final Post post = getItem(position);
        postView.setPost(post);
        postView.update();

        return postView;
    }

    public void updateVisibleViews(){
        for (PostView v : createdViews){
            v.update();
        }
    }

    public void startBaseListingActivity(ListingSource listingSource){
        BaseListingActivity activity = (BaseListingActivity) fragment.getActivity();
        activity.startBaseListingActivity(listingSource);
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void deletePost(Post post){
        fragment.deletePost(post);
    }
}


