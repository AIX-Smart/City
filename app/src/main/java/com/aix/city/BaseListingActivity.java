package com.aix.city;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RelativeLayout;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.aix.city.core.PostListing;
import com.aix.city.core.data.City;


public class BaseListingActivity extends AppCompatActivity implements PostListingFragment.OnFragmentInteractionListener, ListingSourceFragment.OnFragmentInteractionListener, LeftDrawerFragment.OnFragmentInteractionListener, RightDrawerFragment.OnFragmentInteractionListener {

    public final static String EXTRAS_LISTING_SOURCE = "com.aix.city.core.ListingSource";
    public final static String POST_LISTING_FRAGMENT_TAG = "com.aix.city.PostListingFragment";
    public final static String LISTING_SOURCE_FRAGMENT_TAG = "com.aix.city.ListingSourceFragment";

    private DrawerLayout drawerLayout;
    private LinearLayout leftDrawerLayout;
    //private LinearLayout rightDrawerLayout;
    private LinearLayout mainLayout;

    private ListingSource listingSource;

    public BaseListingActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_listing);

        Intent intent = getIntent();
        listingSource = intent.getParcelableExtra(EXTRAS_LISTING_SOURCE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        leftDrawerLayout = (LinearLayout) findViewById(R.id.fragment_drawer_left);
        //rightDrawerLayout = (LinearLayout) findViewById(R.id.fragment_drawer_right);

        createActionBar();
        createMainLayout();
        createLeftDrawer();
        //createRightDrawer();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null/*R.drawable.ic_drawer*/, R.string.acc_drawer_open, R.string.acc_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //if (drawerView.getId() == R.id.fragment_right_menu) slideOffset *= -1;
                float moveFactor = (leftDrawerLayout.getWidth() * slideOffset);
                mainLayout.setTranslationX(moveFactor);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    protected void createActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(AIxDataManager.getInstance().getCurrentCity().getName());

            if (listingSource != null && !listingSource.equals(AIxDataManager.getInstance().getCurrentCity())){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected void createMainLayout(){
        if (listingSource == null){
            listingSource = AIxDataManager.getInstance().getCurrentCity();
        }

        PostListing postListing = listingSource.createPostListing();

        //create fragments with data
        PostListingFragment postListingFragment = PostListingFragment.newInstance(postListing);
        ListingSourceFragment listingSourceFragment = ListingSourceFragment.newInstance(listingSource);

        // Create transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace fragment container with actual fragment
        transaction.replace(R.id.fragment_container_top, listingSourceFragment, LISTING_SOURCE_FRAGMENT_TAG);
        transaction.replace(R.id.fragment_container_bottom, postListingFragment, POST_LISTING_FRAGMENT_TAG);

        // Commit the transaction
        transaction.commit();
    }

    protected void createLeftDrawer(){

    }

    /*private void createRightDrawer(){
        ListView userMenuList = (ListView) findViewById(R.id.right_menu_list);
        ArrayAdapter<String> rightListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DummyContent.RIGHT_MENU_ELEMENTS);
        userMenuList.setAdapter(rightListAdapter);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public ListingSource getListingSource() {
        PostListingFragment fragment = getPostListingFragment();
        if (fragment == null){
            return null;
        }
        return fragment.getPostListing().getListingSource();
    }

    public PostListing getPostListing(){
        PostListingFragment fragment = getPostListingFragment();
        if (fragment == null){
            return null;
        }
        return fragment.getPostListing();
    }

    public PostListingFragment getPostListingFragment(){
        return (PostListingFragment) getSupportFragmentManager().findFragmentByTag(POST_LISTING_FRAGMENT_TAG);
    }

    public ListingSourceFragment getListingSourceFragment(){
        return (ListingSourceFragment) getSupportFragmentManager().findFragmentByTag(LISTING_SOURCE_FRAGMENT_TAG);
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
                case ListingSourceFragment.INTERACTION_KEY_OPEN_LEFT:
                    drawerLayout.openDrawer(GravityCompat.START);
                    break;
                case ListingSourceFragment.INTERACTION_KEY_BACK:
                    Intent intent = new Intent(this, BaseListingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_search:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.action_create:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
