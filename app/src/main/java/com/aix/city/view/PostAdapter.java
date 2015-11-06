package com.aix.city.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.aix.city.R;
import com.aix.city.core.Post;

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
        View rowView = inflater.inflate(R.layout.post, parent, false);

        TextView message = (TextView) rowView.findViewById(R.id.tv1);
        TextView locationName = (TextView) rowView.findViewById(R.id.tv2);
        Button button = (Button) rowView.findViewById(R.id.button);

        message.setText(posts.get(position).getMessage());
        locationName.setText(posts.get(position).getSourceName());

        return rowView;
    }
}


