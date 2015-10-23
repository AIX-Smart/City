package com.aix.city;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


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

        String string = getIntent().getStringExtra("Bars");

        Toast toast = Toast.makeText(this, string, Toast.LENGTH_LONG);
        toast.show();


        menue_list = (ListView) findViewById(R.id.list_menue);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,institutions);
        menue_list.setAdapter(listAdapter);


        menue_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;


                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), ListActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), BarsActivity.class);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), ListActivity.class);
                        break;
                    default:
                        intent = new Intent(getApplicationContext(), ListActivity.class);
                        break;
                }
                intent.putExtra("Bars","Bars");
                startActivity(intent);

            }
        });

    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
