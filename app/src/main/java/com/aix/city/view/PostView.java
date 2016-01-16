package com.aix.city.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Thomas on 17.10.2015.
 */
public class PostView extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.GERMAN);

    private PostAdapter adapter;
    private Post post;
    private boolean likeVisible;

    public PostView(Context context) {
        super(context);
    }

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("SWIPED", "onLayout : " + Boolean.toString(changed));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        super.onInterceptTouchEvent(event);
        Log.e("SWIPED", "onInterceptTouchEvent : " + event.getAction());
        return false;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PostAdapter.ViewHolder getViewHolder(){
        return (PostAdapter.ViewHolder) super.getTag();
    }

    public void init(PostAdapter adapter){
        this.adapter = adapter;
        final PostAdapter.ViewHolder h = getViewHolder();
        this.setOnLongClickListener(this);
        h.locationNameView.setOnClickListener(this);
        h.commentLayout.setOnClickListener(this);
        h.likeButton.setOnClickListener(this);
    }

    public void update(){
        final PostAdapter.ViewHolder h = getViewHolder();
        h.contentView.setText(post.getContent());
        h.likeCounter.setText(String.valueOf(post.getLikeCount()));
        String dateString = DATE_FORMAT.format(new Date(post.getCreationTime()));
        h.creationTime.setText(dateString);

        setBackgroundColor(adapter.getPostColor(post));

        if(post.isLiked()) {
            likeVisible = true;
            h.likeButton.setSelected(true);
        }
        else {
            likeVisible = false;
            h.likeButton.setSelected(false);
        }

        if(post instanceof Event){
            final Event event = (Event) post;
            h.locationNameView.setText(event.getLocation().getName());
            h.commentCounterView.setText(String.valueOf(event.getCommentCount()));
        }
        else{
            h.locationNameView.setVisibility(View.INVISIBLE);
            h.commentCounterView.setVisibility(View.INVISIBLE);
        }
    }

    public void onLocationNameClick(){
        if(post instanceof Event) {
            final Location location = ((Event)post).getLocation();
            if (location != AIxDataManager.EMPTY_LOCATION) {
                adapter.startBaseListingActivity(location);
            }
        }
    }

    public void onCommentCounterClick(){
        if(post instanceof Event) {
            final Event event = (Event)post;
            adapter.startBaseListingActivity(event);
        }
    }

    public void onLikeButtonClick(){
        if(likeVisible){
            post.resetLike();
        }
        else{
            post.like();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == getViewHolder().locationNameView){
            onLocationNameClick();
        }
        else if(v == getViewHolder().commentLayout){
            onCommentCounterClick();
        }
        else if(v == getViewHolder().likeButton){
            onLikeButtonClick();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == this){
            adapter.deletePost(post);
            return true;
        }
        return false;
    }
}
