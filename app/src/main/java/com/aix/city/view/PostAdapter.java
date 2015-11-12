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

    public PostAdapter(Context context, List<Post> posts) {
        super(context, -1, posts);
        this.context = context;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PostView postView = (PostView) inflater.inflate(R.layout.post, parent, false);
        final Post post = posts.get(position);

        TextView contentView = (TextView) postView.findViewById(R.id.content);
        TextView locationNameView = (TextView) postView.findViewById(R.id.locationName);
        Button buttonView = (Button) postView.findViewById(R.id.button);

        contentView.setText(post.getContent());
        locationNameView.setText(post.getSourceName());

        locationNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post instanceof Event) {
                    Intent intent = new Intent(getContext(), BaseListingActivity.class);
                    intent.putExtra(BaseListingActivity.EXTRAS_LISTING_SOURCE, ((Event) post).getLocation());
                    getContext().startActivity(intent);
                }
            }
        });

        return postView;
    }

}


