package com.aix.city;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aix.city.dummy.DummyContent;


public class BaseListingActivity extends FragmentActivity implements PostListingFragment.OnFragmentInteractionListener {

    private Fragment listingSourceFragment;
    private PostListingFragment postListingFragment;
    private ListView searchMenuList;
    private ListView userMenuList;


    /*public static BaseListingActivity newInstance(Fragment listingSourceFragment){
        BaseListingActivity activity = new BaseListingActivity();
        activity.setListingSourceFragment(listingSourceFragment);
        return activity;
    }*/

    public BaseListingActivity(){
        this.listingSourceFragment = new LocationProfileFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_listing);
        postListingFragment = (PostListingFragment)getSupportFragmentManager().findFragmentById(R.id.listing_fragment);


        searchMenuList = (ListView) findViewById(R.id.left_menu_list);
        ArrayAdapter<String> leftListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DummyContent.LEFT_MENU_ELEMENTS);
        searchMenuList.setAdapter(leftListAdapter);

        userMenuList = (ListView) findViewById(R.id.right_menu_list);
        ArrayAdapter<String> rightListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DummyContent.RIGHT_MENU_ELEMENTS);
        userMenuList.setAdapter(rightListAdapter);


        // Create transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_container, listingSourceFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    public Fragment getListingSourceFragment() {
        return listingSourceFragment;
    }

    public void setListingSourceFragment(Fragment listingSourceFragment) {
        this.listingSourceFragment = listingSourceFragment;
    }

    public PostListingFragment getPostListingFragment() {
        return postListingFragment;
    }

    public ListView getSearchMenuList() {
        return searchMenuList;
    }

    public ListView getUserMenuList() {
        return userMenuList;
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
