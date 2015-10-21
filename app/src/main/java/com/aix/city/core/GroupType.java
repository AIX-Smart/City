package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
public enum GroupType {
    FAVORITES,
    COMMENTED,
    RATED,
    TAG;

    @Override
    public String toString() {
        switch (this) {
            case FAVORITES:
                return "Favorites";
            case COMMENTED:
                return "Commented";
            case RATED:
                return "Rated";
            case TAG:
                return "Tag";
            default:
                throw new IllegalArgumentException();
        }
    }
}
