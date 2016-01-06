package com.aix.city;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.Likeable;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.Post;
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
    //timer delay for update requests in milliseconds
    public static final int UPDATE_DELAY_MS = 5000;
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

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    private TextView mEmptyView;

    private ProgressBar mLoadingPanel;

    private View mPostCreationView;

    //true if fragment waits for a response of the server
    private boolean mIsLoading = true;


    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private PostAdapter mAdapter;

    public static PostListingFragment newInstance(PostListing postListing) {
        PostListingFragment fragment = new PostListingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST_LISTING, postListing);
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
            if(obj != null && obj instanceof PostListing){
                mPostListing = (PostListing)obj;
            }
        }

        //create postview-adapter
        mAdapter = new PostAdapter(this, mPostListing.getPosts());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mEmptyView = (TextView)view.findViewById(R.id.emptyText);
        mLoadingPanel = (ProgressBar) view.findViewById(R.id.loadingPanel);

        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
        mEmptyView.setVisibility(View.GONE);

        //initialize post creation view (text field)
        mPostCreationView = view.findViewById(R.id.postCreationLayout);
        setPostCreationVisibility(mPostListing.isEditable());

        final InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final EditText editText = (EditText)view.findViewById(R.id.postCreationTextField);
        editText.setHorizontallyScrolling(false);
        editText.setMaxLines(5);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Post.MAX_CONTENT_LENGTH)});
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                createPost(editText.getText().toString());
                editText.setText("");
                inputManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_POST_LISTING, mPostListing);
        super.onSaveInstanceState(outState);
    }

    public void createPost(String postContent) {
        Runnable successCommand = new Runnable() {
            @Override
            public void run() {
                final String message = getResources().getString(R.string.postCreationMessage_successful);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
        super.onStart();

        mPostListing.addObserver(this);
        AIxDataManager.getInstance().addObserver(this);

        //load posts
        if(mPostListing.getPosts().isEmpty()){
            mPostListing.loadPosts();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostListing.deleteObserver(this);
        AIxDataManager.getInstance().deleteObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.updateVisibleViews();
        mUpdateTaskHandler.postDelayed(mUpdateTask, UPDATE_DELAY_MS);
    }

    @Override
    public void onPause() {
        super.onPause();
        mUpdateTaskHandler.removeCallbacks(mUpdateTask);
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

    public void setPostCreationVisibility(boolean isVisible){
        if(isVisible){
            mPostCreationView.setVisibility(View.VISIBLE);
        }
        else{
            mPostCreationView.setVisibility(View.GONE);
        }
    }

    public void setLoading(boolean isLoading){
        this.mIsLoading = isLoading;
        if (isLoading){
            /*mLoadingPanel.setVisibility(View.VISIBLE);*/
        }
        else{
            mLoadingPanel.setVisibility(View.GONE);
        }
    }

    public boolean isLoading(){
        return mIsLoading;
    }

    public void refresh(){
        setLoading(true);
        mPostListing.refresh();
    }

    public void loadNewerPosts(){
        mPostListing.loadNewerPosts();
    }

    public void loadOlderPosts(){
        setLoading(true);
        mPostListing.loadOlderPosts();
    }

    @Override
    public void update(Observable observable, Object data) {
        String key;
        if(data == null) key = "";
        else key = data.toString();

        switch(key){
            case PostListing.OBSERVER_KEY_CHANGED_DATASET:
                mAdapter.notifyDataSetChanged();
                setLoading(false);
                if (!mPostListing.getPosts().isEmpty()){
                    mEmptyView.setVisibility(View.GONE);
                }
                break;
            case PostListing.OBSERVER_KEY_CHANGED_EDITABILITY:
                setPostCreationVisibility(mPostListing.isEditable());
                break;
            case Likeable.OBSERVER_KEY_CHANGED_LIKESTATUS:
                mAdapter.updateVisibleViews();
                break;
            case AIxDataManager.OBSERVER_KEY_CHANGED_LOCATIONS:
                mAdapter.updateVisibleViews();
                break;
            case PostListing.OBSERVER_KEY_FINISHED:
                setLoading(false);
                if (mPostListing.getPosts().isEmpty()){
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                break;
            case PostListing.OBSERVER_KEY_UPTODATE:
                setLoading(false);
                break;
        }
    }

    public void update(){
        Response.Listener<Boolean> upToDateListener = new Response.Listener<Boolean>(){
            @Override
            public void onResponse(Boolean response) {
                if (!response){
                    loadNewerPosts();
                }
            }
        };
        AIxNetworkManager.getInstance().requestIsUpToDate(upToDateListener, mPostListing);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(!isLoading() && !mPostListing.isFinished() && visibleItemCount != 0){
            final int lastVisibleItem = firstVisibleItem + visibleItemCount;
            if(lastVisibleItem == totalItemCount) {
                loadOlderPosts();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /*final int currentFirstVisibleItem = mListView.getFirstVisiblePosition();

        if(currentFirstVisibleItem > lastFirstVisibleItem) {
            isScrollingUp = false;
        }
        else if(currentFirstVisibleItem < lastFirstVisibleItem) {
            isScrollingUp = true;
        }

        this.scrollState = scrollState;
        lastFirstVisibleItem = currentFirstVisibleItem;*/
    }
}
