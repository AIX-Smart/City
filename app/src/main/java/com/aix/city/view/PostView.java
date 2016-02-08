package com.aix.city.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aix.city.R;
import com.aix.city.core.AIxDataManager;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Thomas on 17.10.2015.
 */
public class PostView extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {

    private static final DateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm", Locale.GERMAN);
    private static final DateFormat FORMAT_DATE = new SimpleDateFormat("dd/MM", Locale.GERMAN);

    private PostViewContext postContext;
    private Post post;

    private TextView contentView;
    private TextView locationNameView;
    private LinearLayout commentLayout;
    private TextView commentCounterView;
    private ImageButton likeButton;
    private TextView likeCounterView;
    private TextView creationTimeView;
    private ImageButton sourceIcon;

    private boolean isPostChanged;

    public PostView(Context context) {
        super(context);
    }

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
        isPostChanged = true;
    }

    public void init(PostViewContext postContext){
        this.postContext = postContext;

        contentView = (TextView) findViewById(R.id.post_content);
        locationNameView = (TextView) findViewById(R.id.post_location_name);
        commentLayout = (LinearLayout) findViewById(R.id.post_comments);
        commentCounterView = (TextView) commentLayout.findViewById(R.id.post_comments_counter);
        likeButton = (ImageButton) findViewById(R.id.post_like_btn);
        likeCounterView = (TextView) findViewById(R.id.post_like_counter);
        creationTimeView = (TextView) findViewById(R.id.post_time);
        sourceIcon = (ImageButton) findViewById(R.id.post_source_icon);

        this.setOnLongClickListener(this);
        locationNameView.setOnClickListener(this);
        commentLayout.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        sourceIcon.setOnClickListener(this);
    }

    public void update(){
        contentView.setText(post.getContent());
        likeCounterView.setText(String.valueOf(post.getLikeCount()));
        if (isPostChanged){
            String dateString;
            Calendar now = Calendar.getInstance(Locale.GERMAN);
            Calendar creationTime = Calendar.getInstance(Locale.GERMAN);
            creationTime.setTimeInMillis(post.getCreationTime());
            boolean createdToday = now.get(Calendar.YEAR) == creationTime.get(Calendar.YEAR) &&
                    now.get(Calendar.DAY_OF_YEAR) == creationTime.get(Calendar.DAY_OF_YEAR);
            if (createdToday){
                dateString = FORMAT_TIME.format(creationTime.getTime());
            }
            else{
                dateString = FORMAT_DATE.format(creationTime.getTime());
            }
            creationTimeView.setText(dateString);

            setBackgroundColor(postContext.getPostColor(post));

            if (post.isAuthenticated()){
                if (post instanceof Event){
                    locationNameView.setText(((Event) post).getLocation().getName());
                }
                else{
                    if (postContext.getSourceLocation() != null){
                        locationNameView.setText(postContext.getSourceLocation().getName());
                    }
                    else{
                        sourceIcon.setVisibility(View.INVISIBLE);
                        locationNameView.setVisibility(View.INVISIBLE);
                    }
                }
            }
            else{
                sourceIcon.setVisibility(View.INVISIBLE);
                locationNameView.setVisibility(View.INVISIBLE);
            }

        }

        if (post.isLiked()){
            likeButton.setSelected(true);
        }
        else{
            likeButton.setSelected(false);
        }

        if (post instanceof Event){
            commentCounterView.setText(String.valueOf(((Event) post).getCommentCount()));
        }
        else{
            commentLayout.setVisibility(View.INVISIBLE);
        }

        isPostChanged = false;
    }

    public void openLocation(){
        if(post instanceof Event) {
            final Location location = ((Event)post).getLocation();
            if (location != AIxDataManager.EMPTY_LOCATION) {
                postContext.startActivity(location);
            }
        }
    }

    public void openComments(){
        if(post instanceof Event) {
            final Event event = (Event)post;
            postContext.startActivity(event, postContext.getPostColor(post));
        }
    }

    public void onLikeButtonClick(){
        if (likeButton.isSelected()){
            post.resetLike();
        }
        else{
            post.setLike();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == locationNameView){
            openLocation();
        }
        else if (v == commentLayout){
            openComments();
        }
        else if (v == likeButton){
            onLikeButtonClick();
        }
        else if (v == sourceIcon){
            openLocation();
        }
    }

    public void setCommentLayout(LinearLayout commentLayout) {
        if (this.commentLayout != commentLayout){
            this.commentLayout.setVisibility(INVISIBLE);

            this.commentLayout = commentLayout;
            this.commentCounterView = (TextView) commentLayout.findViewById(R.id.post_comments_counter);
        }
    }

    public TextView getContentView() {
        return contentView;
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
