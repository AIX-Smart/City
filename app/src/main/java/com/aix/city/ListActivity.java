package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.widget.ListView;

import com.aix.city.view.PostAdapter;

public class ListActivity extends FragmentActivity implements ListingFragment.OnFragmentInteractionListener {

    private ListView mainListView ;
    private PostAdapter listAdapter ;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
