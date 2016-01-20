package com.aix.city;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Tag;
import com.aix.city.view.LocationAdapter;
import com.aix.city.view.TagAdapter;

import java.util.Observable;
import java.util.Observer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeftDrawerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeftDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeftDrawerFragment extends Fragment implements Observer, TabLayout.OnTabSelectedListener {

    public static final String ARG_STATE = "LeftDrawerFragment.state";
    public static final String M_TAG_ADAPTER = "LeftDrawerFragment.tagAdapter";
    public static final String M_LOCATION_ADAPTER = "LeftDrawerFragment.locationAdapter";

    public enum State{
        TAG_SELECTED,
        LOCATION_SELECTED;

        public static State get(int ordinal){
            State[] states = State.values();
            for(State state: states) {
                if(state.ordinal() == ordinal)
                    return state;
            }
            throw new IllegalArgumentException();
        }
    }

    private State state;

    private TagAdapter tagAdapter;
    private LocationAdapter locationAdapter;

    LinearLayout leftDrawerLayout;
    private ListView listView;
    private SearchView searchView;
    private TabLayout tabLayout;
    private TabLayout.Tab locationTab;
    private TabLayout.Tab tagTab;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LeftDrawerFragment.
     */
    public static LeftDrawerFragment newInstance(State state) {
        LeftDrawerFragment fragment = new LeftDrawerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATE, state.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    public LeftDrawerFragment() {
        // Required empty public constructor
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state != state){
            this.state = state;
            updateViews();
        }
    }

    public LocationAdapter getLocationAdapter() {
        return locationAdapter;
    }

    public TagAdapter getTagAdapter() {
        return tagAdapter;
    }

    public void updateViews(){
        String queryHint;
        switch (state){
            case LOCATION_SELECTED:
                listView.setAdapter(locationAdapter);
                queryHint = getResources().getString(R.string.search_location_hint);
                break;
            case TAG_SELECTED:
                listView.setAdapter(tagAdapter);
                queryHint = getResources().getString(R.string.search_tag_hint);
                break;
            default:
                throw new IllegalStateException();
        }
        searchView.setQueryHint(queryHint);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            int stateOrdinal = args.getInt(ARG_STATE);
            this.state = State.get(stateOrdinal);
        }
        else{
            this.state = State.get(0);
        }

        tagAdapter = new TagAdapter(this, AIxDataManager.getInstance().getTags());
        locationAdapter = new LocationAdapter(this, AIxDataManager.getInstance().getCityLocations());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        leftDrawerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_drawer_left, container, false);
        listView = (ListView) leftDrawerLayout.findViewById(R.id.drawer_left_list);
        searchView = (SearchView) leftDrawerLayout.findViewById(R.id.drawer_left_searchView);
        tabLayout = (TabLayout) leftDrawerLayout.findViewById(R.id.drawer_left_layout);

        searchView.setIconifiedByDefault(false);
        tagTab = tabLayout.newTab().setText(R.string.left_tab_tags);
        tagTab.setTag(State.TAG_SELECTED);
        locationTab = tabLayout.newTab().setText(R.string.left_tab_locations);
        locationTab.setTag(State.LOCATION_SELECTED);
        tabLayout.addTab(tagTab);
        tabLayout.addTab(locationTab);
        tabLayout.setOnTabSelectedListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (listView.getAdapter() == locationAdapter) {
                    for (int i = 0; i < locationAdapter.getCount(); i++) {
                        Location location = (Location) locationAdapter.getItem(i);
                        if (locationAdapter.getCount() == 1 || location.getName().equalsIgnoreCase(query)) {
                            locationAdapter.startBaseListingActivity(location);
                        }
                    }
                } else if (listView.getAdapter() == tagAdapter) {
                    for (int i = 0; i < tagAdapter.getCount(); i++) {
                        Tag tag = (Tag) tagAdapter.getItem(i);
                        if (tagAdapter.getCount() == 1 || tag.getName().equalsIgnoreCase(query)) {
                            tagAdapter.startBaseListingActivity(tag);
                        }
                    }
                } else {
                    throw new IllegalStateException();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (listView.getAdapter() == locationAdapter) {
                    locationAdapter.filter(newText);
                } else if (listView.getAdapter() == tagAdapter) {
                    tagAdapter.filter(newText);
                } else {
                    throw new IllegalStateException();
                }
                return false;
            }
        });

        return leftDrawerLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_STATE, state.ordinal());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart(){
        super.onStart();

        AIxDataManager.getInstance().addObserver(this);
        updateViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        AIxDataManager.getInstance().deleteObserver(this);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        State tag = (State) tab.getTag();
        setState(tag);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void update(Observable observable, Object data) {
        if(data != null){
            String key = data.toString();
            switch(key){
                case AIxDataManager.OBSERVER_KEY_CHANGED_LOCATIONS:
                    locationAdapter.setAllLocations(AIxDataManager.getInstance().getCityLocations());
                    locationAdapter.filter(locationAdapter.getFilterConstraint());
                    break;
                case AIxDataManager.OBSERVER_KEY_CHANGED_TAGS:
                    tagAdapter.setAllTags(AIxDataManager.getInstance().getTags());
                    tagAdapter.filter(tagAdapter.getFilterConstraint());
                    break;
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String key);
    }
}
