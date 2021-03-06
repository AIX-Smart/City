package com.aix.city;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.Likeable;
import com.aix.city.core.ListingSource;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.view.PostView;
import com.aix.city.view.PostViewContext;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Thomas on 17.11.2015.
 */
public class EventFragment extends ListingSourceFragment implements PostViewContext, Observer {

    public static final String ARG_POST_COLOR = "EventFragment.color";

    private AIxMainActivity activity;
    private PostView eventView;
    private Event event;

    public static EventFragment newInstance(Event event) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LISTING_SOURCE, event);
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
        eventView = (PostView) view.findViewById(R.id.post);
        final LinearLayout commentLayout = (LinearLayout) view.findViewById(R.id.fragment_event_comments);

        view.setBackgroundColor(event.getColor());
        eventView.init(this);
        eventView.setPost(event);
        eventView.setCommentLayout(commentLayout);

        eventView.update();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_LISTING_SOURCE);
            if (obj != null && obj instanceof Event) {
                event = ((Event) obj);
            }
            else{
                throw new IllegalStateException();
            }
        }
        else{
            throw new IllegalStateException();
        }

        if (getActivity() instanceof AIxMainActivity){
            activity = (AIxMainActivity) getActivity();
        }
        else{
            throw new IllegalStateException();
        }
    }

    @Override
    public void onStart() {
        AIxDataManager.getInstance().addObserver(this);
        event.addObserver(this);
        activity.getPostListing().addObserver(this);
        eventView.update();
        super.onStart();
    }

    @Override
    public void onStop() {
        AIxDataManager.getInstance().deleteObserver(this);
        event.deleteObserver(this);
        activity.getPostListing().deleteObserver(this);
        super.onStop();
    }

    @Override
    public int getPostColor(Post post) {
        return post.getColor();
    }

    @Override
    public void startActivity(ListingSource listingSource) {
        //do nothing
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null){
            String key = data.toString();

            switch(key){
                case PostListing.OBSERVER_KEY_CHANGED_DATASET:
                    if (activity.getPostListing().isFinished()){
                        event.setCommentCount(activity.getPostListing().getPosts().size());
                    }
                    eventView.update();
                    break;
                case Likeable.OBSERVER_KEY_CHANGED_LIKESTATUS:
                    eventView.update();
                    break;
                case AIxDataManager.OBSERVER_KEY_CHANGED_LOCATIONS:
                    eventView.update();
                    break;
            }
        }
    }

    public void refresh(){
        event.updateLikeable();
    }
}