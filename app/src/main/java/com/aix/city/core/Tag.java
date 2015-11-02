package com.aix.city.core;

import com.android.internal.util.Predicate;
import com.android.volley.Request;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Tag extends Group {

    private String name;
    private transient PostListing listing;

    //no-argument constructor for JSON
    private Tag(){}

    public Tag(String name) {
        if(name == null) this.name = "";
        else this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public Predicate<Post> getFilter() {
        return null;
    }

    @Override
    public PostListing getPostListing() {
        if (listing == null) {
            listing = new PostListing(this);
        }
        return listing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Tag tag = (Tag) o;

        return name.equals(tag.name);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
