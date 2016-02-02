package com.aix.city;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.Likeable;
import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Location;

import java.util.Observable;
import java.util.Observer;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationProfileFragment extends ListingSourceFragment implements View.OnClickListener, Observer{

    public static final int MAX_HEIGHT_DP = 210;
    public static final int MIN_HEIGHT_DP = 150;

    private Location location;
    private RelativeLayout mainLayout;
    private TextView locationNameView;
    private ImageButton likeButton;
    private TextView likeCounter;
    private ImageButton expandButton;
    private ImageView expandMoreIcon;
    private ImageView expandLessIcon;
    private TextView openHoursView;
    private TextView addressView;
    private ImageButton gpsIcon_big;
    private ImageButton gpsIcon_small;
    private LinearLayout expandLayout;

    private boolean expanded = false;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getParcelable(ARG_LISTING_SOURCE);
            if(obj != null && obj instanceof Location){
                location = ((Location)obj);
            }
            else{
                throw  new IllegalStateException();
            }
        }
        else{
            throw  new IllegalStateException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_location, container, false);
        locationNameView = (TextView) mainLayout.findViewById(R.id.location_name);
        likeButton = (ImageButton) mainLayout.findViewById(R.id.location_like_btn);
        likeCounter = (TextView) mainLayout.findViewById(R.id.location_like_counter);
        expandMoreIcon = (ImageView) mainLayout.findViewById(R.id.location_expand_more);
        expandLessIcon = (ImageView) mainLayout.findViewById(R.id.location_expand_less);
        expandButton = (ImageButton) mainLayout.findViewById(R.id.location_expand_button);
        openHoursView = (TextView) mainLayout.findViewById((R.id.location_open_hours));
        gpsIcon_big = (ImageButton) mainLayout.findViewById((R.id.location_gpsIcon_big));
        expandLayout = (LinearLayout) mainLayout.findViewById((R.id.location_expand_layout));
        addressView = (TextView) expandLayout.findViewById((R.id.location_address));
        gpsIcon_small = (ImageButton) expandLayout.findViewById((R.id.location_gpsIcon_small));

        expandButton.setOnClickListener(this);
        gpsIcon_big.setOnClickListener(this);
        gpsIcon_small.setOnClickListener(this);
        likeButton.setOnClickListener(this);

        locationNameView.setText(location.getName());
        String address = location.getStreet() + " " + location.getHouseNumber() + "\n" + location.getPlz() + " " + AIxDataManager.getInstance().getCity(location.getCityId()).getName();
        addressView.setText(address);
        openHoursView.setText("17:00 - 02:00");
        collapse();

        return mainLayout;
    }

    @Override
    public void onStart() {
        location.updateLikeable();
        super.onStart();
        location.addObserver(this);
        updateLikeViews();
    }

    @Override
    public void onStop() {
        AIxNetworkManager.getInstance().cancelAllRequests(AIxNetworkManager.TAG_GET_LIKE_COUNT);
        AIxNetworkManager.getInstance().cancelAllRequests(AIxNetworkManager.TAG_GET_LIKE_STATUS);
        location.deleteObserver(this);
        super.onStop();
    }

    public void expand(){
        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_HEIGHT_DP, getResources().getDisplayMetrics());
        mainLayout.setLayoutParams(params);

        expandLayout.setVisibility(View.VISIBLE);
        gpsIcon_big.setVisibility(View.GONE);
        expandMoreIcon.setVisibility(View.INVISIBLE);
        expandLessIcon.setVisibility(View.VISIBLE);

        expanded = true;
    }

    public void collapse(){
        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MIN_HEIGHT_DP, getResources().getDisplayMetrics());
        mainLayout.setLayoutParams(params);

        expandLayout.setVisibility(View.GONE);
        gpsIcon_big.setVisibility(View.VISIBLE);
        expandMoreIcon.setVisibility(View.VISIBLE);
        expandLessIcon.setVisibility(View.INVISIBLE);

        expanded = false;
    }

    public void updateLikeViews(){
        if (location.isLiked()){
            likeButton.setSelected(true);
        }
        else{
            likeButton.setSelected(false);
        }
        likeCounter.setText(String.valueOf(location.getLikeCount()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_expand_button:
                if (expanded){
                    collapse();
                }
                else{
                    expand();
                }
                break;
            case R.id.location_like_btn:
                if (likeButton.isSelected()){
                    location.resetLike();
                }
                else{
                    location.setLike();
                }
                break;
            case R.id.location_gpsIcon_big:
                break;
            case R.id.location_gpsIcon_small:
                break;
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null){
            switch (data.toString()){
                case Likeable.OBSERVER_KEY_CHANGED_LIKESTATUS:
                    updateLikeViews();
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_LISTING_SOURCE, location);
    }
}
