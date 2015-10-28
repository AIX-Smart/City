package com.aix.city.core;

import com.android.internal.util.Predicate;

/**
 * Created by Thomas on 11.10.2015.
 */
public interface ListingSource {

    DatabaseRequest getDatabaseRequest();

    Predicate<Post> getFilter();

    PostListing getPostListing();

}
