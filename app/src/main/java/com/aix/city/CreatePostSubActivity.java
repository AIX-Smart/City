package com.aix.city;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;

/**
 * Created by Thomas on 21.01.2016.
 */
public class CreatePostSubActivity extends AppCompatActivity {

    public static final int SUCCESS_RETURN_CODE = 1;
    public static final int CANCEL_RETURN_CODE = 0;
    public static final String INTENT_EXTRA_LISTING_SOURCE = "com.aix.city.core.ListingSource";
    public static final String INTENT_EXTRA_POST_CONTENT = "CreatePostSubActivity.content";

    private EditText editText;
    private MenuItem sendItem;

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
                Location location = (Location) obj;
                actionBar.setTitle(location.getName());
            }
            else if (obj instanceof Event){
                Event event = (Event) obj;
                actionBar.setTitle(event.getContent());
            }
        }
        else{
            throw  new IllegalStateException();
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        editText = (EditText) findViewById(R.id.create_post_edit_text);
        editText.setHorizontallyScrolling(false);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Post.MAX_CONTENT_LENGTH)});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateActionVisibility();
            }
        });
    }

    public void updateActionVisibility(){
        if (editText != null && sendItem != null){
            if (editText.getText().length() == 0) {
                if (sendItem.isEnabled()){
                    sendItem.setEnabled(false);
                    sendItem.setVisible(false);
                }
            } else {
                if (!sendItem.isEnabled()){
                    sendItem.setEnabled(true);
                    sendItem.setVisible(true);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        editText.requestFocus();
    }

    public void createPost(){

        String content = editText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_POST_CONTENT, content);
        setResult(SUCCESS_RETURN_CODE, intent);
        finish();
        /*
        //Ask the user for confirmation
        new AlertDialog.Builder(CreatePostSubActivity.this*//*new ContextThemeWrapper(CreatePostSubActivity.this, android.R.style.Theme_Holo_Dialog)*//*)
                //.setIcon(android.R.drawable.ic_dialog_alert)
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
                .show();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_post, menu);
        sendItem = menu.findItem(R.id.action_send);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateActionVisibility();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(CANCEL_RETURN_CODE, intent);
                finish();
                return true;
            case R.id.action_send:
                createPost();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
