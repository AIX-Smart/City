package com.aix.city.core;

import com.android.internal.util.Predicate;

/**
 * Created by Thomas on 11.10.2015.
 */
//TODO:
public class Tag extends Group {

    private String name;

    public String getName() {
        return name;
    }

    @Override
    public DatabaseRequest getDatabaseRequest() {
        return super.getDatabaseRequest();
    }

    @Override
    public Predicate<Post> getFilter() {
        return super.getFilter();
    }

    @Override
    public Listing getListing() {
        return super.getListing();
    }
}
