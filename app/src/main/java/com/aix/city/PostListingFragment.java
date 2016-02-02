package com.aix.city;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.EditableCommentListing;
import com.aix.city.core.Likeable;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Post;
import com.aix.city.view.MonochromePostAdapter;
import com.aix.city.view.PostAdapter;
import com.android.volley.Response;

import java.util.Observable;
import java.util.Observer;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class PostListingFragment extends ListFragment implements Observer, AbsListView.OnScrollListener {

    //bundle argument key for creation
    public static final String ARG_POST_LISTING = "PostListingFragment.PostListing";
    //optional bundle argument key for creation
    public static final String ARG_POST_COLOR = "PostListingFragment.color";
    public static final int DEFAULT_COLOR_VALUE = 0xffffffff;
    //bundle key
    public static final String STATE_KEY_INITIALIZED = "PostListingFragment.INITIALIZED";
    public static final String INTERACTION_KEY_CHANGED_EDITABILITY = "PostListingFragment.editabilty";
    //timer delay for update requests in milliseconds
    public static final int UPDATE_DELAY_MS = 8000;
    //handler for timed updates
    private final Handler mUpdateTaskHandler = new Handler();
    private final Runnable mUpdateTask = new Runnable() {
        @Override
        public void run() {
            update();
            mUpdateTaskHandler.postDelayed(this, UPDATE_DELAY_MS);
        }
    };

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
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String key);
    }

    /**
     * The PostListing-object which contains the posts. It is observed by this fragment.
     */
    private PostListing mPostListing;
    private int postColor;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    private TextView mEmptyView;

    private View mLoadingPanel;

    //true if fragment waits for a response of the server
    private boolean mIsLoading = false;


    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private PostAdapter mAdapter;

    public static PostListingFragment newInstance(PostListing postListing) {
        return newInstance(postListing, DEFAULT_COLOR_VALUE);
    }

    public static PostListingFragment newInstance(PostListing postListing, int postColor) {
        PostListingFragment fragment = new PostListingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST_LISTING, postListing);
        args.putInt(ARG_POST_COLOR, postColor);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostListingFragment() {
    }

    public PostListing getPostListing() {
        return mPostListing;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener = (OnFragmentInteractionListener) getActivity();

        //get instance data
        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_POST_LISTING);
            postColor = getArguments().getInt(ARG_POST_COLOR, DEFAULT_COLOR_VALUE);
            if(obj != null && obj instanceof PostListing){
                mPostListing = (PostListing)obj;
            }
            else{
                throw new IllegalStateException();
            }
        }
        else{
            throw new IllegalStateException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mEmptyView = (TextView)view.findViewById(R.id.emptyText);
        mLoadingPanel = inflater.inflate(R.layout.list_footer_loading, mListView, false);

        //create postview-adapter
        if (postColor == DEFAULT_COLOR_VALUE){
            mAdapter = new PostAdapter(this, mPostListing.getPosts());
            mListView.setDivider(null);
            mListView.setDividerHeight(0);
        }
        else{
            mAdapter = new MonochromePostAdapter(this, mPostListing.getPosts(), postColor);
            mListView.setBackgroundColor(postColor);
        }

        mListView.addFooterView(mLoadingPanel);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
        mEmptyView.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_POST_LISTING, mPostListing);
    }

    public void createPost(String postContent) {
        Runnable successCommand = new Runnable() {
            @Override
            public void run() {
                final String message = getResources().getString(R.string.postCreationMessage_successful);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (mPostListing instanceof EditableCommentListing){
                    final Event event = ((EditableCommentListing)mPostListing).getEvent();
                    event.setCommentCount(event.getCommentCount() + 1);
                }
            }
        };
        Runnable errorCommand = new Runnable() {
            @Override
            public void run() {
                final String message = getResources().getString(R.string.postCreationMessage_failed);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };

        boolean isCreationAllowed = mPostListing.createPost(postContent, successCommand, errorCommand);

        if (!isCreationAllowed){
            final String message = getResources().getString(R.string.postCreationMessage_not_allowed);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePost(Post post) {
        Runnable successCommand = new Runnable() {
            @Override
            public void run() {
                final String message = getResources().getString(R.string.postDeletionMessage_successful);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (mPostListing instanceof EditableCommentListing){
                    final Event event = ((EditableCommentListing)mPostListing).getEvent();
                    event.setCommentCount(event.getCommentCount() - 1);
                }
            }
        };
        Runnable errorCommand = new Runnable() {
            @Override
            public void run() {
                final String message = getResources().getString(R.string.postDeletionMessage_failed);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };

        boolean isDeletionAllowed = mPostListing.deletePost(post, successCommand, errorCommand);

        if (!isDeletionAllowed){
            final String message = getResources().getString(R.string.postDeletionMessage_not_allowed);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        mPostListing.addObserver(this);
        AIxDataManager.getInstance().addObserver(this);

        super.onStart();

        setLoading(mPostListing.isWaitingForInit());
        setFinished(mPostListing.isFinished());

        mUpdateTaskHandler.post(mUpdateTask);
    }

    @Override
    public void onStop() {
        mUpdateTaskHandler.removeCallbacks(mUpdateTask);
        super.onStop();

        mPostListing.deleteObserver(this);
        AIxDataManager.getInstance().deleteObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.updateVisibleViews();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setOrder(PostListing.Order order) {
        mPostListing.setOrder(order);
        setFinished(false);
        setLoading(true);
    }

    public void setLoading(boolean isLoading){
        this.mIsLoading = isLoading;
    }

    public void setFinished(boolean finished){
        if (finished){
            mListView.removeFooterView(mLoadingPanel);
            if (mPostListing.getPosts().isEmpty()){
                mEmptyView.setVisibility(View.VISIBLE);
            }
        }
        else{
            if (mListView.getFooterViewsCount() == 0){
                mListView.addFooterView(mLoadingPanel);
            }
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public boolean isLoading(){
        return mIsLoading;
    }

    public void refresh(){
        if (!isLoading()){
            setLoading(true);
            setFinished(false);
            mPostListing.refresh();
        }
    }

    public void loadNewerPosts(){
        mPostListing.loadNewerPosts();
    }

    public void loadOlderPosts(){
        setLoading(true);
        mPostListing.loadPosts();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null) {
            String key = data.toString();

            switch (key) {
                case PostListing.OBSERVER_KEY_CHANGED_DATASET:
                    mAdapter.notifyDataSetChanged();
                    setLoading(false);
                    if (!mPostListing.getPosts().isEmpty()) {
                        mEmptyView.setVisibility(View.GONE);
                    }
                    break;
                case INTERACTION_KEY_CHANGED_EDITABILITY:
                    mListener.onFragmentInteraction(PostListing.OBSERVER_KEY_CHANGED_EDITABILITY);
                    break;
                case Likeable.OBSERVER_KEY_CHANGED_LIKESTATUS:
                    mAdapter.updateVisibleViews();
                    break;
                case AIxDataManager.OBSERVER_KEY_CHANGED_LOCATIONS:
                    mAdapter.updateVisibleViews();
                    break;
                case PostListing.OBSERVER_KEY_FINISHED:
                    setLoading(false);
                    setFinished(true);
                    break;
            }
        }
    }

    public void update(){
        if (mPostListing.isWaitingForInit()){
            setLoading(true);
            mPostListing.loadInitialPosts();
        }
        else{
            if (!mPostListing.isEmpty()){
                PostListing.Order order = mPostListing.getOrder();
                if (order == null){
                    order = PostListing.Order.NEWEST_FIRST;
                }
                switch(order){
                    case NEWEST_FIRST:
                        Response.Listener<Boolean> upToDateListener = new Response.Listener<Boolean>(){
                            @Override
                            public void onResponse(Boolean response) {
                                if (!response){
                                    loadNewerPosts();
                                }
                            }
                        };
                        AIxNetworkManager.getInstance().requestIsUpToDate(upToDateListener, mPostListing);
                        break;
                    case POPULAR_FIRST:
                        break;
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(!isLoading() && !mPostListing.isFinished() && visibleItemCount != 0 && !mPostListing.isEmpty()){
            final int lastVisibleItem = firstVisibleItem + visibleItemCount;
            if(lastVisibleItem == totalItemCount) {
                loadOlderPosts();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}
