package com.aix.city.core;

import android.os.Parcel;

import com.aix.city.core.data.City;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Post;
import com.aix.city.core.data.Tag;

/**
 * Created by Thomas on 03.11.2015.
 */
public enum ListingSourceType {
    CITY {
        @Override
        public int getParcelDescription() {
            return PARCEL_DESCRIPTION_CITY;
        }

        @Override
        public City fromParcel(Parcel source) {
            return new City(source);
        }
    },
    LOCATION {
        @Override
        public int getParcelDescription() {
            return PARCEL_DESCRIPTION_LOCATION;
        }

        @Override
        public Location fromParcel(Parcel source) {
            return new Location(source);
        }
    },
    TAG {
        @Override
        public int getParcelDescription() {
            return PARCEL_DESCRIPTION_TAG;
        }

        @Override
        public Tag fromParcel(Parcel source) {
            return new Tag(source);
        }
    },
    EVENT {
        @Override
        public int getParcelDescription() {
            return PARCEL_DESCRIPTION_EVENT;
        }

        @Override
        public Event fromParcel(Parcel source) {
            return new Event(source);
        }
    };

    public static final int PARCEL_DESCRIPTION_EVENT = Post.PARCEL_DESCRIPTION_EVENT;
    public static final int PARCEL_DESCRIPTION_CITY = PARCEL_DESCRIPTION_EVENT + 1;
    public static final int PARCEL_DESCRIPTION_LOCATION = PARCEL_DESCRIPTION_CITY + 1;
    public static final int PARCEL_DESCRIPTION_TAG = PARCEL_DESCRIPTION_LOCATION + 1;

    public static ListingSourceType getType(int parcelDescription){
        ListingSourceType[] types = ListingSourceType.values();
        for (int i = 0; i < types.length; i++){
            if(types[i].getParcelDescription() == parcelDescription)
                return types[i];
        }
        throw new IllegalArgumentException();
    }

    public static ListingSource createListingSource(Parcel source) {
        int parcelDescription = source.readInt();
        ListingSourceType type = getType(parcelDescription);
        return type.fromParcel(source);
    }

    public abstract int getParcelDescription();

    public abstract ListingSource fromParcel(Parcel source);
}
