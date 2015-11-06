package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aix.city.core.Location;
import com.aix.city.dummy.DummyContent;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationProfileFragment extends Fragment {

    public final static String ARG_LOCATION = "location";

    private Location location = DummyContent.GINBAR;

    public static LocationProfileFragment newInstance(Location location) {
        LocationProfileFragment fragment = new LocationProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_LOCATION);
            if(obj != null && obj instanceof Location){
                location = ((Location)obj);
            }
        }
    }
}
