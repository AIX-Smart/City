package com.aix.city;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;


public class AIxMainActivity extends AppCompatActivity implements PostListingFragment.OnFragmentInteractionListener, LeftDrawerFragment.OnFragmentInteractionListener {

    public final static String INTENT_EXTRA_LISTING_SOURCE = "AIxMainActivity.ListingSource";
    public final static String POST_LISTING_FRAGMENT_TAG = "PostListingFragment";
    public final static String LISTING_SOURCE_FRAGMENT_TAG = "ListingSourceFragment";
    public static final int CREATE_POST_REQUEST_CODE = 100;

    private DrawerLayout drawerLayout;
    private ScrollView leftDrawerLayout;
    private LinearLayout mainLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;
    private ListingSource listingSource;
    private Menu menu;

    public AIxMainActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        leftDrawerLayout = (ScrollView) findViewById(R.id.fragment_drawer_left);

        createActionBar();
        createMainLayout();
    }

    protected void createActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar == null){
            throw new IllegalStateException();
        }

        actionBar.setTitle(AIxDataManager.getInstance().getCurrentCity().getName());
        if (getListingSource().getType() == ListingSourceType.TAG){
            actionBar.setSubtitle(((Tag) getListingSource()).getName());
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getListingSource().getType() != ListingSourceType.EVENT) {

            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.acc_drawer_open, R.string.acc_drawer_close) {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    //if (drawerView.getId() == R.id.fragment_right_menu) slideOffset *= -1;
                    float moveFactor = (leftDrawerLayout.getWidth() * slideOffset);
                    mainLayout.setTranslationX(moveFactor);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    //actionBar.setTitle("Navigation!");
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                @Override
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    //actionBar.setTitle(AIxDataManager.getInstance().getCurrentCity().getName());
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.setDrawerListener(drawerToggle);
        }
    }

    protected void createMainLayout(){
        //get or create fragments with data
        PostListingFragment postListingFragment = getPostListingFragment();
        ListingSourceFragment listingSourceFragment = ListingSourceFragment.newInstance(getListingSource());

        // Create transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace fragment container with actual fragment
        if (listingSourceFragment != null){
            transaction.replace(R.id.fragment_container_top, listingSourceFragment, LISTING_SOURCE_FRAGMENT_TAG);
        }
        transaction.replace(R.id.fragment_container_bottom, postListingFragment, POST_LISTING_FRAGMENT_TAG);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPostListing().updateLikes();
    }

    @Override
    protected void onDestroy() {
        getPostListing().cancelRequests();
        getPostListingFragment().cancelRequests();
        super.onDestroy();
    }

    public void createPost(String content){
        getPostListingFragment().createPost(content);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @NonNull
    public ListingSource getListingSource() {
        if(listingSource == null){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(POST_LISTING_FRAGMENT_TAG);

            if (fragment != null && fragment instanceof PostListingFragment){
                listingSource = ((PostListingFragment) fragment).getPostListing().getListingSource();
            }
            else{
                listingSource = getIntent().getParcelableExtra(INTENT_EXTRA_LISTING_SOURCE);
                if (listingSource == null){
                    listingSource = AIxDataManager.getInstance().getCurrentCity();
                }
            }
        }

        return listingSource;
    }

    @NonNull
    public PostListing getPostListing(){
        return getPostListingFragment().getPostListing();
    }

    @NonNull
    public PostListingFragment getPostListingFragment(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(POST_LISTING_FRAGMENT_TAG);
        if (fragment == null){
            fragment = PostListingFragment.newInstance(getListingSource().createPostListing());
        }

        if (fragment instanceof PostListingFragment){
            return (PostListingFragment) fragment;
        }
        else{
            throw new IllegalStateException();
        }
    }

    @Nullable
    public ListingSourceFragment getListingSourceFragment(){
        return (ListingSourceFragment) getSupportFragmentManager().findFragmentByTag(LISTING_SOURCE_FRAGMENT_TAG);
    }

    public void startActivity(ListingSource ls){
        if (!ls.equals(getListingSource())){
            mainLayout.requestFocus();
            drawerLayout.closeDrawers();

            Intent intent = new Intent(this, AIxMainActivity.class);
            intent.putExtra(AIxMainActivity.INTENT_EXTRA_LISTING_SOURCE, ls);

            this.startActivity(intent);
            switch(getListingSource().getType()){
                case EVENT:
                    finish();
                    break;
                case LOCATION:
                    if (ls.getType() != ListingSourceType.EVENT){
                        finish();
                    }
                    break;
                case TAG:
                    if (ls.getType() == ListingSourceType.TAG || ls.getType() == ListingSourceType.CITY){
                        finish();
                    }
                    break;
                case CITY:
                    if (ls.getType() == ListingSourceType.CITY){
                        finish();
                    }
                    break;
            }
        }
    }

    @Override
    public void onFragmentInteraction(String key) {
        if (key != null){
            switch(key){
                /*case ListingSourceFragment.INTERACTION_KEY_BACK:
                    Intent intent = new Intent(this, AIxMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;*/
                case LeftDrawerFragment.INTERACTION_KEY_NEWEST_FIRST:
                    getPostListingFragment().setOrder(PostListing.Order.NEWEST_FIRST);
                    //drawerLayout.closeDrawers();
                    break;
                case LeftDrawerFragment.INTERACTION_KEY_POPULAR_FIRST:
                    getPostListingFragment().setOrder(PostListing.Order.POPULAR_FIRST);
                    //drawerLayout.closeDrawers();
                    break;
                case PostListingFragment.INTERACTION_KEY_CHANGED_EDITABILITY:
                    updatePostCreationVisibility();
                    break;
            }
        }
    }

    public void updatePostCreationVisibility(){
        final MenuItem createEvent = menu.findItem(R.id.action_create);
        final MenuItem createComment = menu.findItem(R.id.action_create_comment);

        PostListing postListing = getPostListing();
        if (postListing.isEditable()){
            switch (postListing.getListingSource().getType()){
                case LOCATION:
                    createEvent.setVisible(true);
                    createEvent.setEnabled(true);
                    menu.add(createEvent.getGroupId(), createEvent.getItemId(), createEvent.getOrder(), createEvent.getTitle());
                    break;
                case EVENT:
                    createComment.setVisible(true);
                    createComment.setEnabled(true);
                    menu.add(createComment.getGroupId(), createComment.getItemId(), createComment.getOrder(), createComment.getTitle());
                    break;
            }
        }
        else{
            createEvent.setEnabled(false);
            createComment.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(leftDrawerLayout)/* || drawerLayout.isDrawerOpen(rightDrawerLayout)*/){
            drawerLayout.closeDrawers();
        }
        else{
            if (getListingSource().equals(AIxDataManager.getInstance().getCurrentCity())){
                getPostListing().clear();
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        updatePostCreationVisibility();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getListingSource().getType() == ListingSourceType.EVENT){
                    onBackPressed();
                    return true;
                }
                return drawerToggle.onOptionsItemSelected(item);
            case R.id.action_settings:
                return true;

            case R.id.action_create:
            case R.id.action_create_comment:
                Intent intent = new Intent(this, CreatePostSubActivity.class);
                intent.putExtra(CreatePostSubActivity.INTENT_EXTRA_LISTING_SOURCE, getListingSource());
                startActivityForResult(intent, CREATE_POST_REQUEST_CODE);
                return true;

            case R.id.action_refresh:
                ListingSourceFragment fragment = getListingSourceFragment();
                if (fragment != null){
                    fragment.refresh();
                }
                getPostListingFragment().refresh();
                AIxDataManager.getInstance().refreshData();
                return true;
        }
        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getListingSource().getType() != ListingSourceType.EVENT){
            drawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getListingSource().getType() != ListingSourceType.EVENT){
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CREATE_POST_REQUEST_CODE && resultCode == CreatePostSubActivity.SUCCESS_RETURN_CODE){
            String content = data.getStringExtra(CreatePostSubActivity.INTENT_EXTRA_POST_CONTENT);
            getPostListingFragment().createPost(content);
        }
    }
}
