package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListActivity extends FragmentActivity implements ListingFragment.OnFragmentInteractionListener {

    private ListView mainListView ;
    private ListView leftMenuList;
    private ArrayAdapter<String> leftListAdapter;
    private String[] leftListElements = {"post", "bars", "restaurants"};
    private String[] rightListElements = {"favorites", "commented", "own business"};
    private ListView rightMenuList;
    private ArrayAdapter<String> rightListAdapter;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        /*leftMenuList = (ListView) findViewById(R.id.list_menue);
        leftListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leftListElements);
        leftMenuList.setAdapter(leftListAdapter);

        rightMenuList = (ListView) findViewById(R.id.list_userProfile);
        rightListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rightListElements);
        rightMenuList.setAdapter(rightListAdapter);*/

    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
