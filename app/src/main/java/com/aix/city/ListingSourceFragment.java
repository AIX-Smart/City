package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.ListingSource;
import com.aix.city.core.data.City;
import com.aix.city.core.data.Event;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Tag;

/**
 * Created by Thomas on 08.12.2015.
 */
public abstract class ListingSourceFragment extends Fragment {

    public final static String ARG_LISTING_SOURCE = "listingSource";

    public ListingSourceFragment() {
    }

    public abstract ListingSource getListingSource();

    public abstract void refresh();
}
