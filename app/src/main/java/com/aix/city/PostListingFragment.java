package com.aix.city;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.EditableCommentListing;
import com.aix.city.core.Likeable;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Post;
import com.aix.city.view.PostAdapter;
import com.aix.city.view.PostView;
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

    //bundle argument key for creation
    public static final String ARG_POST_LISTING = "PostListingFragment.postListing";
    public static final String INTERACTION_KEY_CHANGED_EDITABILITY = "PostListingFragment.editabilty";
    //timer delay for update requests in milliseconds
    public static final int UPDATE_DELAY_MS = 8000;
    //handler for timed updates
    private final Handler updateTaskHandler = new Handler();
    private final Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            update();
            updateTaskHandler.postDelayed(this, UPDATE_DELAY_MS);
        }
    };

    /**
     * The PostListing-object which contains the posts. It is observed by this fragment.
     */
    private PostListing postListing;

    private OnFragmentInteractionListener listener;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView listView;

    private TextView emptyView;

    private View loadingPanel;

    //true if fragment waits for a response of the server
    private boolean isLoading = false;
    private boolean isErrorOccured = false;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private PostAdapter adapter;



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
        return postListing;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener = (OnFragmentInteractionListener) getActivity();

        //get instance data
        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_POST_LISTING);
            if(obj != null && obj instanceof PostListing){
                postListing = (PostListing)obj;
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
        listView = (ListView) view.findViewById(android.R.id.list);
        emptyView = (TextView)view.findViewById(R.id.emptyText);
        loadingPanel = inflater.inflate(R.layout.list_footer_loading, listView, false);

        //create postview-adapter
        adapter = new PostAdapter(this, postListing.getPosts());
        //listView.setDivider(null);
        //listView.setDividerHeight(0);

        listView.addFooterView(loadingPanel);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        emptyView.setVisibility(View.GONE);
        registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_POST_LISTING, postListing);
    }

    public void createPost(String postContent) {
        Runnable successCommand = new Runnable() {
            @Override
            public void run() {
                final String message = getResources().getString(R.string.postCreationMessage_successful);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (postListing instanceof EditableCommentListing){
                    final Event event = ((EditableCommentListing) postListing).getEvent();
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

        boolean isCreationAllowed = postListing.createPost(postContent, successCommand, errorCommand);

        if (!isCreationAllowed){
            final String message = getResources().getString(R.string.postCreationMessage_not_allowed);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePost(final Post post) {
        Runnable successCommand = new Runnable() {
            @Override
            public void run() {
                final String message = getResources().getString(R.string.postDeletionMessage_successful);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (postListing instanceof EditableCommentListing){
                    final Event event = ((EditableCommentListing) postListing).getEvent();
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

        boolean isDeletionAllowed = postListing.deletePost(post, successCommand, errorCommand);

        if (!isDeletionAllowed){
            final String message = getResources().getString(R.string.postDeletionMessage_not_allowed);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void editPost(Post post){
        //TODO:
    }


    @Override
    public void onStart() {
        postListing.addObserver(this);
        AIxDataManager.getInstance().addObserver(this);
        isErrorOccured = false;

        super.onStart();

        setIsLoading(postListing.isWaitingForInit());
        setIsFinished(postListing.isFinished());

        updateTaskHandler.post(updateTask);
    }

    @Override
    public void onStop() {
        updateTaskHandler.removeCallbacks(updateTask);
        cancelRequests();
        super.onStop();

        postListing.deleteObserver(this);
        AIxDataManager.getInstance().deleteObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateVisibleViews();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void cancelRequests(){
        AIxNetworkManager.getInstance().cancelAllRequests(this);
    }

    private void clear(){
        cancelRequests();
        setIsLoading(true);
        setIsFinished(false);
        isErrorOccured = false;
    }

    public void refresh(){
        if (!isLoading()){
            clear();
            postListing.refresh();
        }
    }

    public void setOrder(PostListing.Order order) {
        if (order != postListing.getOrder()){
            clear();
            postListing.setOrder(order);
        }
    }

    public void setIsLoading(boolean isLoading){
        this.isLoading = isLoading;
    }

    public void setIsFinished(boolean finished){
        if (finished){
            listView.removeFooterView(loadingPanel);
            if (postListing.getPosts().isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
            }
        }
        else{
            if (listView.getFooterViewsCount() == 0){
                listView.addFooterView(loadingPanel);
            }
            emptyView.setVisibility(View.GONE);
        }
    }

    public boolean isLoading(){
        return isLoading;
    }

    public void loadNewerPosts(){
        postListing.loadNewerPosts();
    }

    public void loadOlderPosts(){
        setIsLoading(true);
        postListing.loadPosts();
    }

    public AIxMainActivity getAIxActivity() {
        return (AIxMainActivity) getActivity();
    }

    public void startActivity(ListingSource listingSource) {
        getAIxActivity().startActivity(listingSource);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null) {
            String key = data.toString();

            switch (key) {
                case PostListing.OBSERVER_KEY_CHANGED_DATASET:
                    adapter.notifyDataSetChanged();
                    setIsLoading(false);
                    if (!postListing.getPosts().isEmpty()) {
                        emptyView.setVisibility(View.GONE);
                    }
                    break;
                case INTERACTION_KEY_CHANGED_EDITABILITY:
                    listener.onFragmentInteraction(PostListing.OBSERVER_KEY_CHANGED_EDITABILITY);
                    break;
                /*case Likeable.OBSERVER_KEY_CHANGED_LIKESTATUS:
                    adapter.updateVisibleViews();
                    break;*/
                case AIxDataManager.OBSERVER_KEY_CHANGED_LOCATIONS:
                    adapter.updateVisibleViews();
                    break;
                case PostListing.OBSERVER_KEY_FINISHED:
                    setIsLoading(false);
                    setIsFinished(true);
                    break;
                case PostListing.OBSERVER_KEY_CONNECTION_ERROR:
                    isErrorOccured = true;
                    break;
            }
        }
    }

    public void update(){
        if (postListing.isWaitingForInit()){
            setIsLoading(true);
            postListing.loadInitialPosts();
        }
        else{
            if (!postListing.isEmpty()){
                PostListing.Order order = postListing.getOrder();
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
                        AIxNetworkManager.getInstance().requestIsUpToDate(this, upToDateListener, postListing);
                        break;
                    case POPULAR_FIRST:
                        break;
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (!postListing.isFinished() && visibleItemCount != 0 && !postListing.isEmpty()) {
            if (!isLoading() || isErrorOccured) {
                final int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (lastVisibleItem == totalItemCount) {
                    loadOlderPosts();
                    isErrorOccured = false;
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            Post post = adapter.getItem(info.position);
            getActivity().getMenuInflater().inflate(R.menu.context_post, menu);

            for (int i = 0; i < menu.size(); i++){
                MenuItem item = menu.getItem(i);
                switch(item.getItemId()){
                    case R.id.context_read_comments:
                        if (post instanceof Event){
                            item.setVisible(true);
                            item.setEnabled(true);
                        }
                        break;
                    case R.id.context_open_location:
                        if (post instanceof Event && !((Event) post).getLocation().equals(postListing.getListingSource())){
                            item.setTitle("Öffne " + ((Event) post).getLocation().getName() + "-Profil");
                            item.setVisible(true);
                            item.setEnabled(true);
                        }
                        break;
                    case R.id.context_delete:
                        if (post.isDeletionAllowed()){
                            item.setVisible(true);
                            item.setEnabled(true);
                        }
                        break;
                }
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final Post post = adapter.getItem(info.position);
        final PostView postView = (PostView) info.targetView;
        switch (item.getItemId()){
            case R.id.context_read_comments:
                postView.openComments();
                return true;
            case R.id.context_open_location:
                postView.openLocation();
                return true;
            case R.id.context_edit:
                editPost(post);
                return true;
            case R.id.context_delete:
                //Ask the user for confirmation
                new AlertDialog.Builder(getActivity())
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.delete_post_dialog_title)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePost(post);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
