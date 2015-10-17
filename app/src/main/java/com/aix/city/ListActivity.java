package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aix.city.core.City;
import com.aix.city.core.Event;
import com.aix.city.core.ListingFromLocation;
import com.aix.city.core.Location;
import com.aix.city.core.Post;
import com.aix.city.view.PostAdapter;
import com.aix.city.view.PostView;

public class ListActivity extends FragmentActivity implements ListingFragment.OnFragmentInteractionListener {

    private ListView mainListView ;
    private PostAdapter listAdapter ;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fragment.onCreateView()

        // Find the ListView resource.
       /* mainListView = (ListView) findViewById( R.id.listView );

        Location location = new Location(0, "GinBar", new City("Aachen", 0));
        ListingFromLocation listing = location.getListing();

        // Create and populate a List of planet names.
        Event[] posts = new Event[] {listing.createEvent("message1"), listing.createEvent("message2")};
        *//*ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );*//*

        // Create ArrayAdapter using the planet list.
        listAdapter = new PostAdapter(this, posts);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
*//*        listAdapter.add( "post9" );
        listAdapter.add( "post10" );
        listAdapter.add( "post11" );
        listAdapter.add( "post12" );
        listAdapter.add( "post13" );*//*

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );*/
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
