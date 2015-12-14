package com.aix.city.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aix.city.BaseListingActivity;
import com.aix.city.R;
import com.aix.city.core.data.Tag;

import java.util.List;

/**
 * Created by Thomas on 09.12.2015.
 */
public class TagAdapter extends ArrayAdapter<Tag> {
    private final Context context;
    private final List<Tag> tags;

    static class ViewHolder {
        FrameLayout tagLayout;
        TextView tagNameView;
    }

    public TagAdapter(Context context, List<Tag> tags) {
        super(context, -1, tags);
        this.context = context;
        this.tags = tags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null || !(convertView.getTag() instanceof TagAdapter.ViewHolder)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_element_tag, parent, false);

            holder = new ViewHolder();
            holder.tagNameView = (TextView) convertView.findViewById(R.id.tagElementName);
            holder.tagLayout = (FrameLayout) convertView.findViewById(R.id.tagElementLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Tag tag = tags.get(position);

        if (tag != null) {
            updateTagLayout(holder.tagLayout, tag);
            updateTagNameView(holder.tagNameView, tag);
        }

        return convertView;
    }

    private void updateTagLayout(FrameLayout tagLayout, final Tag tag) {
        tagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BaseListingActivity.class);
                intent.putExtra(BaseListingActivity.EXTRAS_LISTING_SOURCE, tag);
                getContext().startActivity(intent);
            }
        });
    }

    private void updateTagNameView(TextView tagNameView, final Tag tag) {
        tagNameView.setText(tag.getName());
    }
}
