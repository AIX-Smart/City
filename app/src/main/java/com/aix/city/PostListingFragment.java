package com.aix.city;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.aix.city.core.ListingSource;
import com.aix.city.core.PostListing;
import com.aix.city.dummy.DummyContent;
import com.aix.city.view.PostAdapter;

import java.util.Observable;
import java.util.Observer;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link PostListingFragmentListener}
 * interface.
 */
public class PostListingFragment extends ListFragment implements AbsListView.OnItemClickListener, Observer {
    
    public final static String ARG_LISTING_SOURCE = "listingSource";

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface PostListingFragmentListener {
        public void changePostCreationVisibility(boolean isEditable);
    }

    /**
     * The PostListing-object which contains the posts. It is observed by this fragment.
     */
    private PostListing postListing;

    private PostListingFragmentListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private PostAdapter mAdapter;

    public static PostListingFragment newInstance(ListingSource listingSource) {
        PostListingFragment fragment = new PostListingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LISTING_SOURCE, listingSource);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostListingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener = (PostListingFragmentListener) getActivity();

        //get instance data
        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_LISTING_SOURCE);
            if(obj != null && obj instanceof ListingSource){
                postListing = ((ListingSource)obj).createPostListing();
            }
        }

        if(postListing == null){
            postListing = DummyContent.AACHEN_LISTING;
        }

        //create postview-adapter
        mAdapter = new PostAdapter(getActivity(), postListing.getPosts());
        postListing.addObserver(this);

        /*mListener.changePostCreationVisibility(postListing.isEditable());*/

        //load posts
        postListing.loadMorePosts();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mListView.setEmptyView(view.findViewById(android.R.id.empty));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.changePostCreationVisibility(postListing.isEditable());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PostListingFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PostListingFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        mAdapter.notifyDataSetChanged();
    }
}
