package com.aix.city;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.data.City;
import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Tag;
import com.aix.city.dummy.DummyContent;


public class BaseListingActivity extends FragmentActivity implements PostListingFragment.OnFragmentInteractionListener {

    public final static String EXTRAS_LISTING_SOURCE = "com.aix.city.ListingSource";

    private Fragment listingSourceFragment;
    private PostListingFragment postListingFragment;

    private ListView searchMenuList;
    private ListView userMenuList;

    private ListingSource listingSource;

    public BaseListingActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_listing);

        Intent intent = getIntent();
        listingSource = intent.getParcelableExtra(EXTRAS_LISTING_SOURCE);
        if(listingSource == null){
           /*listingSource = AIxDataManager.getInstance().getCurrentCity();*/
        }

        searchMenuList = (ListView) findViewById(R.id.left_menu_list);
        ArrayAdapter<String> leftListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DummyContent.LEFT_MENU_ELEMENTS);
        searchMenuList.setAdapter(leftListAdapter);

        userMenuList = (ListView) findViewById(R.id.right_menu_list);
        ArrayAdapter<String> rightListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DummyContent.RIGHT_MENU_ELEMENTS);
        userMenuList.setAdapter(rightListAdapter);

        //create fragments with data
        postListingFragment = PostListingFragment.newInstance(listingSource);
        listingSourceFragment = createListingSourceFragment();

        // Create transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_container_top, listingSourceFragment);
        transaction.replace(R.id.fragment_container_bottom, postListingFragment);

        // Commit the transaction
        transaction.commit();

    }



    public Fragment createListingSourceFragment(){
        if(listingSource == null) return CityFragment.newInstance((City) listingSource);

        switch (listingSource.getType()) {
            case CITY:
                return CityFragment.newInstance((City) listingSource);
            case LOCATION:
                return LocationProfileFragment.newInstance((Location) listingSource);
            case TAG:
                return TagFragment.newInstance((Tag) listingSource);
            case EVENT:
                return EventFragment.newInstance((Event) listingSource);
            default:
                return CityFragment.newInstance(AIxDataManager.getInstance().getCurrentCity());
        }

    }

    public Fragment getListingSourceFragment() {
        return listingSourceFragment;
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

    public ListingSource getListingSource() {
        return listingSource;
    }

    @Override
    public void onFragmentInteraction(boolean isEditable) {
    }
}
