package com.aix.city.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.aix.city.BaseListingActivity;
import com.aix.city.R;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Post;

import java.util.List;

/**
 * Created by Thomas on 17.10.2015.
 */
public class PostAdapter extends ArrayAdapter<Post> {
    private final Context context;
    private final List<Post> posts;

    static class ViewHolder {
        TextView contentView;
        TextView locationNameView;
        TextView commentCounterView;
        Button likeButton;
    }

    public PostAdapter(Context context, List<Post> posts) {
        super(context, -1, posts);
        this.context = context;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (PostView) inflater.inflate(R.layout.post, parent, false);

            holder = new ViewHolder();
            holder.contentView = (TextView) convertView.findViewById(R.id.content);
            holder.locationNameView = (TextView) convertView.findViewById(R.id.locationName);
            holder.commentCounterView = (TextView) convertView.findViewById(R.id.commentCounter);
            holder.likeButton = (Button) convertView.findViewById(R.id.likeButton);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        final Post post = posts.get(position);

        if(post != null){
            updateCommentCounterView(holder.commentCounterView, post);
            updateContentView(holder.contentView, post);
            updateLikeButton(holder.likeButton, post);
            updateLocationNameView(holder.locationNameView, post);
        }

        return convertView;
    }

    public void updateLikeButton(Button likeButton, final Post post){
        if (post instanceof Event) {
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    post.like();
                }
            });
        }
        else{
            likeButton.setVisibility(View.GONE);
        }
    }

    public void updateLocationNameView(TextView locationNameView, final Post post){
        if (post instanceof Event){
            final Event event = (Event) post;

            locationNameView.setText(post.getSourceName());
            locationNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), BaseListingActivity.class);
                    intent.putExtra(BaseListingActivity.EXTRAS_LISTING_SOURCE, event.getLocation());
                    getContext().startActivity(intent);
                }
            });
        }
        else{
            locationNameView.setVisibility(View.GONE);
        }
    }

    public void updateCommentCounterView(TextView commentCounterView, final Post post){
        if (post instanceof Event){
            final Event event = (Event) post;

            commentCounterView.setText(String.valueOf(event.getCommentCount()));
            commentCounterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), BaseListingActivity.class);
                    intent.putExtra(BaseListingActivity.EXTRAS_LISTING_SOURCE, event);
                    getContext().startActivity(intent);
                }
            });
        }
        else{
            commentCounterView.setVisibility(View.GONE);
        }
    }

    public void updateContentView(TextView contentView, final Post post){
        contentView.setText(post.getContent());
    }
}


