package com.aix.city;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.aix.city.core.PostListing;


public class BaseListingActivity extends AppCompatActivity implements PostListingFragment.OnFragmentInteractionListener, ListingSourceFragment.OnFragmentInteractionListener, LeftDrawerFragment.OnFragmentInteractionListener {

    public final static String EXTRAS_LISTING_SOURCE = "com.aix.city.core.ListingSource";
    public final static String POST_LISTING_FRAGMENT_TAG = "PostListingFragment";
    public final static String LISTING_SOURCE_FRAGMENT_TAG = "ListingSourceFragment";

    private DrawerLayout drawerLayout;
    private LinearLayout leftDrawerLayout;
    private LinearLayout mainLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Menu menu;

    public BaseListingActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_listing);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        leftDrawerLayout = (LinearLayout) findViewById(R.id.fragment_drawer_left);

        createActionBar();
        createMainLayout();
    }

    protected void createActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar == null){
            throw new IllegalStateException();
        }

        actionBar.setTitle(AIxDataManager.getInstance().getCurrentCity().getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

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
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    protected void createMainLayout(){
        //get or create fragments with data
        PostListingFragment postListingFragment = getPostListingFragment();
        ListingSourceFragment listingSourceFragment = getListingSourceFragment();

        // Create transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace fragment container with actual fragment
        transaction.replace(R.id.fragment_container_top, listingSourceFragment, LISTING_SOURCE_FRAGMENT_TAG);
        transaction.replace(R.id.fragment_container_bottom, postListingFragment, POST_LISTING_FRAGMENT_TAG);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        /*SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                loadHistory(query);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });*/

        return true;
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
        ListingSource listingSource = null;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(POST_LISTING_FRAGMENT_TAG);

        if (fragment != null && fragment instanceof PostListingFragment){
            listingSource = ((PostListingFragment) fragment).getPostListing().getListingSource();
        }
        else{
            listingSource = getIntent().getParcelableExtra(EXTRAS_LISTING_SOURCE);
            if (listingSource == null){
                listingSource = AIxDataManager.getInstance().getCurrentCity();
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

    @NonNull
    public ListingSourceFragment getListingSourceFragment(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LISTING_SOURCE_FRAGMENT_TAG);
        if (fragment == null){
            fragment = ListingSourceFragment.newInstance(getListingSource());
        }

        if (fragment instanceof ListingSourceFragment){
            return (ListingSourceFragment) fragment;
        }
        else{
            throw new IllegalStateException();
        }
    }

    public void startActivity(ListingSource ls){
        if (!ls.equals(getListingSource())){
            mainLayout.requestFocus();
            drawerLayout.closeDrawers();

            Intent intent = new Intent(this, BaseListingActivity.class);
            intent.putExtra(BaseListingActivity.EXTRAS_LISTING_SOURCE, ls);
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
                    Intent intent = new Intent(this, BaseListingActivity.class);
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
            }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_search:
                return true;

            case R.id.action_create:
                return true;

            case R.id.action_refresh:
                getPostListingFragment().refresh();
                return true;

            default:
                if (drawerToggle.onOptionsItemSelected(item)) {
                    return true;
                }
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
