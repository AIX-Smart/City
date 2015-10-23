package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aix.city.view.PostAdapter;

public class ListActivity extends FragmentActivity implements ListingFragment.OnFragmentInteractionListener {

    private ListView mainListView ;
    private ListView menue_list;
    ArrayAdapter<String> listAdapter;
    String[] institutions = {"post", "bars", "restaurtants"};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menue_list = (ListView) findViewById(R.id.list_menue);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,institutions);
        menue_list.setAdapter(listAdapter);



    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
