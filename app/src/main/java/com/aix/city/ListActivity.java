package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ListActivity extends AppCompatActivity {

    private ListView mainListView;


    ListView listview;
    ArrayAdapter<String> listAdapter;
    String fragmentArray[] = {"post", "bars", "restaurants"};


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listview = (ListView) findViewById(R.id.listview);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fragmentArray);
        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;
                switch (position){
                    case 0:
                        fragment = new ListingFragment();
                        break;
                    case 1:
                        fragment = new ListingFragmentTag();
                        break;
                    case 2:
                        fragment = new ListingFragmentTag();
                        break;
                    default:
                        fragment = new ListingFragment();
                        break;
                }

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.relativelayout, fragment).commit();

            }
        });

    }

}
