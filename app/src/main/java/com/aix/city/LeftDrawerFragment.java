package com.aix.city;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.Searchable;
import com.aix.city.core.data.Tag;
import com.aix.city.view.SearchSuggestionAdapter;

import java.util.ArrayList;
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
public class LeftDrawerFragment extends Fragment implements Observer, View.OnClickListener {

    public static final String INTERACTION_KEY_POPULAR_FIRST = "POPULAR_FIRST";
    public static final String INTERACTION_KEY_NEWEST_FIRST = "NEWEST_FIRST";

    private ScrollView leftDrawerLayout;
    private ListView listView;
    private SearchView searchView;
    private RadioButton newestFirstButton;
    private RadioButton popularFirstButton;
    private SearchSuggestionAdapter searchAdapter;

    private BaseListingActivity mActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LeftDrawerFragment.
     */
    public static LeftDrawerFragment newInstance() {
        LeftDrawerFragment fragment = new LeftDrawerFragment();
        return fragment;
    }

    public LeftDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        leftDrawerLayout = (ScrollView) inflater.inflate(R.layout.fragment_drawer_left, container, false);
        //listView = (ListView) leftDrawerLayout.findViewById(R.id.drawer_left_list);
        searchView = (SearchView) leftDrawerLayout.findViewById(R.id.drawer_left_searchView);
        newestFirstButton = (RadioButton) leftDrawerLayout.findViewById(R.id.drawer_left_radio_newest_first);
        popularFirstButton = (RadioButton) leftDrawerLayout.findViewById(R.id.drawer_left_radio_popular_first);

        leftDrawerLayout.findViewById(R.id.drawer_left_hungrig).setOnClickListener(this);
        leftDrawerLayout.findViewById(R.id.drawer_left_durstig).setOnClickListener(this);
        leftDrawerLayout.findViewById(R.id.drawer_left_party).setOnClickListener(this);
        leftDrawerLayout.findViewById(R.id.drawer_left_alles_andere).setOnClickListener(this);

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && (searchAdapter == null || searchAdapter.isInvalidated())) {
                    searchAdapter = new SearchSuggestionAdapter(getContext(), new ArrayList<Searchable>(AIxDataManager.getInstance().getCityLocations()), mActivity.getListingSource());
                    searchView.setSuggestionsAdapter(searchAdapter);
                }
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                mActivity.startActivity(searchAdapter.getSearchableItem(position));
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchAdapter.getSearchableItems().isEmpty() && searchAdapter.isFiltered()){
                    Searchable item = searchAdapter.getSearchableItem(0);
                    searchView.setQuery(item.getName(), false);
                    mActivity.startActivity(item);
                    return true;
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        newestFirstButton.setOnClickListener(this);
        popularFirstButton.setOnClickListener(this);

        return leftDrawerLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity = (BaseListingActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onClick(View v) {
        Tag tag;

        switch(v.getId()) {
            case R.id.drawer_left_radio_newest_first:
                if (((RadioButton) v).isChecked()){
                    mActivity.onFragmentInteraction(INTERACTION_KEY_NEWEST_FIRST);
                }
                break;
            case R.id.drawer_left_radio_popular_first:
                if (((RadioButton) v).isChecked()){
                    mActivity.onFragmentInteraction(INTERACTION_KEY_POPULAR_FIRST);
                }
                break;
            case R.id.drawer_left_hungrig:
                tag = AIxDataManager.getInstance().getTag(AIxDataManager.HUNGRIG_ID);
                if (tag != null){
                    mActivity.startActivity(tag);
                }
                break;
            case R.id.drawer_left_durstig:
                tag = AIxDataManager.getInstance().getTag(AIxDataManager.DURSTIG_ID);
                if (tag != null){
                    mActivity.startActivity(tag);
                }
                break;
            case R.id.drawer_left_party:
                tag = AIxDataManager.getInstance().getTag(AIxDataManager.PARTY_ID);
                if (tag != null){
                    mActivity.startActivity(tag);
                }
                break;
            case R.id.drawer_left_alles_andere:
                /*Intent intent = new Intent(this.getContext(), TagListActivity.class);
                getContext().startActivity(intent);*/
                tag = AIxDataManager.getInstance().getTag(AIxDataManager.ALLES_ANDERE_ID);
                if (tag != null){
                    mActivity.startActivity(tag);
                }
                break;
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data != null){
            String key = data.toString();
            switch(key){
                case AIxDataManager.OBSERVER_KEY_CHANGED_LOCATIONS:
                    searchAdapter.invalidate();
                    /*locationAdapter.setAllLocations(AIxDataManager.getInstance().getCityLocations());
                    locationAdapter.filter(locationAdapter.getFilterConstraint());*/
                    break;
                /*case AIxDataManager.OBSERVER_KEY_CHANGED_TAGS:
                    tagAdapter.setAllTags(AIxDataManager.getInstance().getTags());
                    tagAdapter.filter(tagAdapter.getFilterConstraint());
                    break;*/
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
