package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Tag;
import com.aix.city.dummy.DummyContent;

/**
 * Created by Thomas on 17.11.2015.
 */
public class EventFragment extends ListingSourceFragment {

    public final static String ARG_EVENT = "event";

    private Event event;
    private TextView eventNameView;

    public static EventFragment newInstance(Event event) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    public EventFragment() {
    }

    @Override
    public ListingSource getListingSource() {
        return event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        eventNameView = (TextView) view.findViewById(R.id.sourceEvent);
        eventNameView.setText(event.getContent());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_EVENT);
            if (obj != null && obj instanceof Event) {
                event = ((Event) obj);
            }
        }
    }
}