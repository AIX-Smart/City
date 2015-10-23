package com.aix.city;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LocationActivity extends ListActivity {

    private ListView leftMenuList;
    private ArrayAdapter<String> leftListAdapter;
    private String[] leftListElements = {"post", "bars", "restaurants"};
    private String[] rightListElements = {"favorites", "commented", "own business"};
    private ListView rightMenuList;
    private ArrayAdapter<String> rightListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        leftMenuList = (ListView) findViewById(R.id.list_menue);
        leftListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leftListElements);
        leftMenuList.setAdapter(leftListAdapter);

        rightMenuList = (ListView) findViewById(R.id.list_userProfile);
        rightListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rightListElements);
        rightMenuList.setAdapter(rightListAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
