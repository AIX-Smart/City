package com.aix.city.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aix.city.BaseListingActivity;
import com.aix.city.R;
import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 09.12.2015.
 */
public class TagAdapter extends ArrayAdapter<Tag> implements Filterable {
    private final Fragment fragment;
    private List<Tag> allTags;
    private CharSequence filterConstraint = "";

    static class ViewHolder {
        FrameLayout tagLayout;
        TextView tagNameView;
    }

    public TagAdapter(Fragment fragment, List<Tag> tags) {
        super(fragment.getContext(), -1, new ArrayList<Tag>(tags));
        this.fragment = fragment;
        this.allTags = tags;
    }

    public List<Tag> getAllTags() {
        return allTags;
    }

    public void setAllTags(List<Tag> allTags) {
        this.allTags = allTags;
    }

    public CharSequence getFilterConstraint() {
        return filterConstraint;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null || !(convertView.getTag() instanceof TagAdapter.ViewHolder)) {
            LayoutInflater inflater = (LayoutInflater) fragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_element_tag, parent, false);

            holder = new ViewHolder();
            holder.tagNameView = (TextView) convertView.findViewById(R.id.tagElementName);
            holder.tagLayout = (FrameLayout) convertView.findViewById(R.id.tagElementLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (allTags.size() > 0){
            final Tag tag = getItem(position);

            if (tag != null) {
                updateTagLayout(holder.tagLayout, tag);
                updateTagNameView(holder.tagNameView, tag);
            }
            return convertView;
        }
        else{
            return null;
        }
    }

    private void updateTagLayout(FrameLayout tagLayout, final Tag tag) {
        tagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBaseListingActivity(tag);
            }
        });
    }

    private void updateTagNameView(TextView tagNameView, final Tag tag) {
        tagNameView.setText(tag.getName());
    }

    public void startBaseListingActivity(Tag tag){
        BaseListingActivity activity = (BaseListingActivity) fragment.getActivity();
        activity.startBaseListingActivity(tag);
    }

    public void filter(CharSequence constraint){
        if (constraint == null) constraint = "";
        filterConstraint = constraint;
        getFilter().filter(constraint);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Tag> filteredResult = getFilteredResults(charSequence);

                FilterResults results = new FilterResults();
                results.values = filteredResult;
                results.count = filteredResult.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();
                @SuppressWarnings("unchecked")
                List<Tag> filteredList = (List<Tag>) filterResults.values;
                addAll(filteredList);
            }


            private List<Tag> getFilteredResults(CharSequence constraint){
                if (constraint == null) constraint = "";
                ArrayList<Tag> listResult = new ArrayList<Tag>();
                for (Tag tag : allTags){
                    if (tag.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        listResult.add(tag);
                    }
                }
                return listResult;
            }
        };
    }
}
