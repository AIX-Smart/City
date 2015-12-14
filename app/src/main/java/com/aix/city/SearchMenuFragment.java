package com.aix.city;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.SearchView;
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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchMenuFragment extends Fragment implements Observer, TabLayout.OnTabSelectedListener {

    public static final String ARG_STATE = "SearchMenuFragment.state";

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

    LinearLayout searchMenuLayout;
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
     * @return A new instance of fragment SearchMenuFragment.
     */
    public static SearchMenuFragment newInstance(State state) {
        SearchMenuFragment fragment = new SearchMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATE, state.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    public SearchMenuFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchMenuLayout = (LinearLayout) inflater.inflate(R.layout.fragment_search_menu, container, false);
        listView = (ListView) searchMenuLayout.findViewById(R.id.left_menu_list);
        searchView = (SearchView) searchMenuLayout.findViewById(R.id.searchView);
        tabLayout = (TabLayout) searchMenuLayout.findViewById(R.id.left_tab_layout);

        tagTab = tabLayout.newTab().setText(R.string.left_tab_tags);
        tagTab.setTag(State.TAG_SELECTED);
        locationTab = tabLayout.newTab().setText(R.string.left_tab_locations);
        locationTab.setTag(State.LOCATION_SELECTED);
        tabLayout.addTab(tagTab);
        tabLayout.addTab(locationTab);
        tabLayout.setOnTabSelectedListener(this);

        return searchMenuLayout;
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

        tagAdapter = new TagAdapter(getActivity(), AIxDataManager.getInstance().getTags());
        locationAdapter = new LocationAdapter(getActivity(), AIxDataManager.getInstance().getCityLocations());
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
                    locationAdapter.setNotifyOnChange(false);
                    locationAdapter.clear();
                    locationAdapter.setNotifyOnChange(true);
                    locationAdapter.addAll(AIxDataManager.getInstance().getCityLocations());
                    break;
                case AIxDataManager.OBSERVER_KEY_CHANGED_TAGS:
                    tagAdapter.setNotifyOnChange(false);
                    tagAdapter.clear();
                    tagAdapter.setNotifyOnChange(true);
                    tagAdapter.addAll(AIxDataManager.getInstance().getTags());
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
