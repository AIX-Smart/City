package com.aix.city.core;

/**
 * Created by Thomas on 11.10.2015.
 */
public enum GroupType {
    FAVORITES,
    COMMENTED,
    RATED,
    OTHER;

    @Override
    public String toString() {
        switch (this) {
            case FAVORITES:
                return "favorites";
            case COMMENTED:
                return "commented";
            case RATED:
                return "rated";
            case OTHER:
                return "other";
            default:
                throw new IllegalArgumentException();
        }
    }
}
