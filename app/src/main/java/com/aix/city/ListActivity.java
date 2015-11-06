package com.aix.city;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends FragmentActivity implements PostListingFragment.OnFragmentInteractionListener {

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

        String string = getIntent().getStringExtra("Bars");

        Toast toast = Toast.makeText(this, string, Toast.LENGTH_LONG);
        toast.show();

        /*ListView leftMenuList = (ListView) findViewById(R.id.left_menu_list);
        ArrayAdapter<String> leftListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DummyContent.LEFT_MENU_ELEMENTS);
        leftMenuList.setAdapter(leftListAdapter);

        ListView rightMenuList = (ListView) findViewById(R.id.right_menu_list);
        ArrayAdapter<String> rightListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DummyContent.RIGHT_MENU_ELEMENTS);
        rightMenuList.setAdapter(rightListAdapter);*/



        leftMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
