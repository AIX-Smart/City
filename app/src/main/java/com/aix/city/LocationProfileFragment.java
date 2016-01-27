package com.aix.city;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Location;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationProfileFragment extends ListingSourceFragment {

    public static final int MAX_HEIGHT_DP = 200;
    public static final int MIN_HEIGHT_DP = 120;

    private Location location;
    private RelativeLayout mainLayout;
    private TextView locationNameView;
    private ImageView expandMoreIcon;
    private ImageView expandLessIcon;

    public static LocationProfileFragment newInstance(Location location) {
        LocationProfileFragment fragment = new LocationProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LISTING_SOURCE, location);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationProfileFragment() {
    }

    @Override
    public ListingSource getListingSource() {
        return location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_location, container, false);
        locationNameView = (TextView) mainLayout.findViewById(R.id.location_name);
        expandMoreIcon = (ImageView) mainLayout.findViewById(R.id.location_expand_more);
        expandLessIcon = (ImageView) mainLayout.findViewById(R.id.location_expand_less);

        locationNameView.setText(location.getName());

        return mainLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_LISTING_SOURCE);
            if(obj != null && obj instanceof Location){
                location = ((Location)obj);
            }
        }
    }

    public void expand(){
        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_HEIGHT_DP, getResources().getDisplayMetrics());
        mainLayout.setLayoutParams(params);

        expandMoreIcon.setVisibility(View.INVISIBLE);
        expandLessIcon.setVisibility(View.VISIBLE);
    }

    public void collapse(){
        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MIN_HEIGHT_DP, getResources().getDisplayMetrics());
        mainLayout.setLayoutParams(params);

        expandMoreIcon.setVisibility(View.VISIBLE);
        expandLessIcon.setVisibility(View.INVISIBLE);
    }
}
