package com.aix.city.core;

import com.android.internal.util.Predicate;
import com.android.volley.Request;

/**
 * Created by Thomas on 11.10.2015.
 */
public interface ListingSource {

    Request getRequest();

    Predicate<Post> getFilter();

    PostListing getPostListing();

}
