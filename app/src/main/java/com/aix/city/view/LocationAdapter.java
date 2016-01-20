package com.aix.city.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aix.city.BaseListingActivity;
import com.aix.city.R;
import com.aix.city.core.data.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 13.12.2015.
 */
public class LocationAdapter extends ArrayAdapter<Location> implements Filterable {
    private final Fragment fragment;
    private List<Location> allLocations;
    private CharSequence filterConstraint = "";

    static class ViewHolder {
        FrameLayout locationLayout;
        TextView locationNameView;
    }

    public LocationAdapter(Fragment fragment, List<Location> locations) {
        super(fragment.getContext(), -1, new ArrayList<Location>(locations));
        this.fragment = fragment;
        this.allLocations = locations;
    }

    public List<Location> getAllLocations() {
        return allLocations;
    }

    public void setAllLocations(List<Location> allLocations) {
        this.allLocations = allLocations;
    }

    public CharSequence getFilterConstraint() {
        return filterConstraint;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null || !(convertView.getTag() instanceof LocationAdapter.ViewHolder)) {
            LayoutInflater inflater = (LayoutInflater) fragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_location, parent, false);

            holder = new ViewHolder();
            holder.locationNameView = (TextView) convertView.findViewById(R.id.list_item_location_name);
            holder.locationLayout = (FrameLayout) convertView.findViewById(R.id.list_item_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (allLocations.size() > 0){
            final Location location = getItem(position);

            if (location != null) {
                updateLocationLayout(holder.locationLayout, location);
                updateLocationNameView(holder.locationNameView, location);
            }
            return convertView;
        }
        else{
            return null;
        }

    }

    private void updateLocationLayout(FrameLayout locationLayout, final Location location) {
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBaseListingActivity(location);
            }
        });
    }

    private void updateLocationNameView(TextView locationNameView, final Location location) {
        locationNameView.setText(location.getName());
    }

    public void startBaseListingActivity(Location location){
        BaseListingActivity activity = (BaseListingActivity) fragment.getActivity();
        activity.startActivity(location);
    }

    public void filter(CharSequence constraint){
        if (constraint == null) constraint = "";
        filterConstraint = constraint;
        getFilter().filter(constraint);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Location> filteredResult = getFilteredResults(charSequence);

                FilterResults results = new FilterResults();
                results.values = filteredResult;
                results.count = filteredResult.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();
                @SuppressWarnings("unchecked")
                List<Location> filteredList = (List<Location>) filterResults.values;
                addAll(filteredList);
            }


            private List<Location> getFilteredResults(CharSequence constraint){
                if (constraint == null) constraint = "";
                ArrayList<Location> listResult = new ArrayList<Location>();
                for (Location location : allLocations){
                    if (location.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        listResult.add(location);
                    }
                }
                return listResult;
            }
        };
    }
}
