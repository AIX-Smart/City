package com.aix.city.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aix.city.R;
import com.aix.city.core.Event;
import com.aix.city.core.Post;

/**
 * Created by Thomas on 17.10.2015.
 */
public class PostAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final Event[] values;

    public PostAdapter(Context context, Event[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.post, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tv1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.tv2);
        Button button = (Button) rowView.findViewById(R.id.button);
        textView.setText(values[position].getMessage());
        textView2.setText(values[position].getLocation().getLocationName());
        // change the icon for Windows and iPhone

        return rowView;
    }
}


