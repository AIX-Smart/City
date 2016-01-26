package com.aix.city.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.aix.city.BaseListingActivity;
import com.aix.city.R;
import com.aix.city.core.Searchable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Thomas on 25.01.2016.
 */
public class SearchSuggestionAdapter extends CursorAdapter implements FilterQueryProvider {

    public static final Comparator<Searchable> suggestionComparator = new Comparator<Searchable>() {
        @Override
        public int compare(Searchable lhs, Searchable rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    };

    private final ThemedSpinnerAdapter.Helper dropDownHelper;
    private BaseListingActivity activity;
    private TextView text;
    private final List<Searchable> allItems;
    private List<Searchable> filteredItems;
    private boolean invalidated = false;


    public SearchSuggestionAdapter(Context context, List<Searchable> items) {
        super(context, createCursor(items), false);
        this.activity = activity;
        this.allItems = items;
        Collections.sort(allItems, suggestionComparator);
        this.filteredItems = this.allItems;
        dropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        setFilterQueryProvider(this);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Searchable item = filteredItems.get(cursor.getPosition());
        text.setText(item.getName());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = dropDownHelper.getDropDownViewInflater().inflate(R.layout.search_item, parent, false);
        text = (TextView) view.findViewById(R.id.search_item_text);

        return view;
    }

    private static MatrixCursor createCursor(List<Searchable> items){
        String[] columns = new String[] { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1 };
        Object[] temp = new Object[] { 0, "default" };
        MatrixCursor cursor = new MatrixCursor(columns);

        for(int i = 0; i < items.size(); i++) {
            temp[0] = i;
            temp[1] = items.get(i);
            cursor.addRow(temp);
        }

        return cursor;
    }

    public MatrixCursor createCursor(){
        return createCursor(filteredItems);
    }

    @Override
    public Cursor runQuery(CharSequence constraint) {
        if (constraint == null || constraint.length() == 0){

            filteredItems = allItems;
        }
        else{

            filteredItems = new ArrayList<Searchable>();
            ArrayList<Searchable> containsList = new ArrayList<Searchable>();
            for (Searchable item : allItems){
                if (item.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                    filteredItems.add(item);
                }
                else if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                    containsList.add(item);
                }
            }
            filteredItems.addAll(containsList);
        }

        return createCursor();
    }

    public Searchable getSearchableItem(int position){
        return filteredItems.get(position);
    }

    public List<Searchable> getSearchableItems() {
        return filteredItems;
    }

    public void invalidate(){
        invalidated = true;
    }

    public boolean isInvalidated() {
        return invalidated;
    }

    @Override
    public void setDropDownViewTheme(Resources.Theme theme) {
        dropDownHelper.setDropDownViewTheme(theme);
    }

    @Override
    public Resources.Theme getDropDownViewTheme() {
        return dropDownHelper.getDropDownViewTheme();
    }
}