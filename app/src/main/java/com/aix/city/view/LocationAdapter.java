package com.aix.city.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aix.city.BaseListingActivity;
import com.aix.city.R;
import com.aix.city.core.data.Location;

import java.util.List;

/**
 * Created by Thomas on 13.12.2015.
 */
public class LocationAdapter extends ArrayAdapter<Location> {
    private final Context context;
    private final List<Location> locations;

    static class ViewHolder {
        FrameLayout locationLayout;
        TextView locationNameView;
    }

    public LocationAdapter(Context context, List<Location> locations) {
        super(context, -1, locations);
        this.context = context;
        this.locations = locations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null || !(convertView.getTag() instanceof LocationAdapter.ViewHolder)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_element_location, parent, false);

            holder = new ViewHolder();
            holder.locationNameView = (TextView) convertView.findViewById(R.id.locationElementName);
            holder.locationLayout = (FrameLayout) convertView.findViewById(R.id.locationElementLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Location location = locations.get(position);

        if (location != null) {
            updateLocationLayout(holder.locationLayout, location);
            updateLocationNameView(holder.locationNameView, location);
        }

        return convertView;
    }

    private void updateLocationLayout(FrameLayout locationLayout, final Location location) {
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BaseListingActivity.class);
                intent.putExtra(BaseListingActivity.EXTRAS_LISTING_SOURCE, location);
                getContext().startActivity(intent);
            }
        });
    }

    private void updateLocationNameView(TextView locationNameView, final Location location) {
        locationNameView.setText(location.getName());
    }
}
