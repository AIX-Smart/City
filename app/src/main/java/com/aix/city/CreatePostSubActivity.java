package com.aix.city;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;

/**
 * Created by Thomas on 21.01.2016.
 */
public class CreatePostSubActivity extends AppCompatActivity {

    public static final int SUCCESS_RETURN_CODE = 1;
    public static final String INTENT_EXTRA_LISTING_SOURCE = "com.aix.city.core.ListingSource";
    public static final String INTENT_EXTRA_POST_CONTENT = "CreatePostSubActivity.content";

    private Location location;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null){
            throw  new IllegalStateException();
        }

        Object obj = getIntent().getParcelableExtra(INTENT_EXTRA_LISTING_SOURCE);
        if(obj != null){
            if (obj instanceof Location){
                location = ((Location)obj);
            }
            else if (obj instanceof Event){
                location = ((Event) obj).getLocation();
            }
        }
        else{
            throw  new IllegalStateException();
        }

        actionBar.setTitle(location.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        editText = (EditText) findViewById(R.id.postCreationTextField);
        editText.setHorizontallyScrolling(false);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Post.MAX_CONTENT_LENGTH)});
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!v.getText().toString().equals("")){
                    createPost();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        editText.requestFocus();
    }

    public void createPost(){
        //Ask the user for confirmation
        new AlertDialog.Builder(new ContextThemeWrapper(CreatePostSubActivity.this, android.R.style.Theme_Holo_Dialog))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.create_post_dialog_title)
                        //.setMessage(R.string.create_post_dialog_text)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = editText.getText().toString();
                        Intent intent = new Intent();
                        intent.putExtra(INTENT_EXTRA_POST_CONTENT, content);
                        setResult(SUCCESS_RETURN_CODE, intent);
                        CreatePostSubActivity.this.finish();
                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
