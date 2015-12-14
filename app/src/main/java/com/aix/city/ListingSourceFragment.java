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

    public static final String INTERACTION_KEY_OPEN_LEFT = "ListingSourceFragment.openLeft";
    public final static String ARG_LISTING_SOURCE = "listingSource";

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String key);
    }

    public static ListingSourceFragment newInstance(ListingSource listingSource) {
        ListingSourceFragment fragment;
        if(listingSource == null) listingSource = AIxDataManager.getInstance().getCurrentCity();
        switch (listingSource.getType()) {
            case CITY:
                fragment = CityFragment.newInstance((City) listingSource);
                break;
            case LOCATION:
                fragment = LocationProfileFragment.newInstance((Location) listingSource);
                break;
            case TAG:
                fragment = TagFragment.newInstance((Tag) listingSource);
                break;
            case EVENT:
                fragment = EventFragment.newInstance((Event) listingSource);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return fragment;
    }

    public ListingSourceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (OnFragmentInteractionListener) getActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_LISTING_SOURCE, getListingSource());
        super.onSaveInstanceState(outState);
    }

    public OnFragmentInteractionListener getListener() {
        return mListener;
    }

    public abstract ListingSource getListingSource();
}
