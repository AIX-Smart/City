package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aix.city.core.ListingSource;
import com.aix.city.core.data.City;
import com.aix.city.dummy.DummyContent;

/**
 * Created by Thomas on 03.11.2015.
 */
public class CityFragment extends ListingSourceFragment {

    public final static String ARG_CITY = "city";

    private City city = DummyContent.AACHEN;

    public static CityFragment newInstance(City city) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    public CityFragment() {
    }

    @Override
    public ListingSource getListingSource() {
        return city;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        TextView cityNameView = (TextView) view.findViewById(R.id.cityName);
        cityNameView.setText(city.getName());

        Button openSearchMenuButton = (Button) view.findViewById(R.id.openLeft);
        openSearchMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener().onFragmentInteraction(INTERACTION_KEY_OPEN_LEFT);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_CITY);
            if(obj != null && obj instanceof City){
                city = ((City)obj);
            }
        }
    }


}
