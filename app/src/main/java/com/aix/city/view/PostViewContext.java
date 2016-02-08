package com.aix.city.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.aix.city.BaseListingActivity;
import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;

/**
 * Created by Thomas on 02.02.2016.
 */
public interface PostViewContext {

    int getPostColor(Post post);

    void putPostColor(Post post, int color);

    void startActivity(ListingSource listingSource);

    void startActivity(ListingSource listingSource, int postColor);

    @Nullable
    Location getSourceLocation();

    Activity getActivity();

    Context getContext();
}