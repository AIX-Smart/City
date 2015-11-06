package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BarsActivity extends FragmentActivity implements ListingBarFragment.OnFragmentInteractionListener {

    private ListView mainListView ;
    private ListView menue_list;
    ArrayAdapter<String> listAdapter;
    String[] institutions = {"post", "bars", "restaurtants"};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bars_activity);

        String string = getIntent().getStringExtra("Bars");

        Toast toast = Toast.makeText(this, string, Toast.LENGTH_LONG);
        toast.show();




    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
