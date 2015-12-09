package com.aix.city;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Tag;
import com.aix.city.dummy.DummyContent;

/**
 * Created by Thomas on 17.11.2015.
 */
public class TagFragment extends ListingSourceFragment {

    public final static String ARG_TAG = "tag";

    private Tag tag = DummyContent.BAR_TAG;
    private TextView tagNameView;

    public static TagFragment newInstance(Tag tag) {
        TagFragment fragment = new TagFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TAG, tag);
        fragment.setArguments(args);
        return fragment;
    }

    public TagFragment() {
    }

    @Override
    public ListingSource getListingSource() {
        return tag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag, container, false);
        tagNameView = (TextView) view.findViewById(R.id.tagElementName);
        tagNameView.setText(tag.getName());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_TAG);
            if (obj != null && obj instanceof Tag) {
                tag = ((Tag) obj);
            }
        }
    }
}
