package com.aix.city.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;

/**
 * Created by Thomas on 02.02.2016.
 */
public interface PostViewContext {

    int getPostColor(Post post);

    void startActivity(ListingSource listingSource);

    Context getContext();
}
