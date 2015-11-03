package com.aix.city;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aix.city.core.City;
import com.aix.city.core.Location;
import com.aix.city.dummy.DummyContent;

/**
 * Created by Thomas on 03.11.2015.
 */
public class CityFragment extends Fragment {

    public final static String ARG_CITY = "city";

    private City city = DummyContent.AACHEN;
    private TextView cityNameView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        cityNameView = (TextView) view.findViewById(R.id.cityName);
        cityNameView.setText(city.getName());
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
