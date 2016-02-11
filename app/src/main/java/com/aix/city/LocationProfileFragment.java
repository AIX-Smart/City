package com.aix.city;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aix.city.comm.URLFactory;
import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.Likeable;
import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Location;
import com.aix.city.view.NetworkImageViewTopCrop;
import com.android.volley.toolbox.ImageLoader;

import java.util.Observable;
import java.util.Observer;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationProfileFragment extends ListingSourceFragment implements View.OnClickListener, Observer{

    private static final int NOT_INITIALIZED = -1;
    private static final int EXPAND_BUTTON_DELAY_MS = 400;
    private static int MAX_HEIGHT = NOT_INITIALIZED;
    private static int MIN_HEIGHT = NOT_INITIALIZED;

    private final Handler handler = new Handler();
    private final Runnable enableExpandButton = new Runnable() {
        @Override
        public void run() {
            expandButtonIsEnabled = true;
        }
    };

    private Location location;
    private RelativeLayout mainLayout;
    private TextView locationNameView;
    private ImageButton likeButton;
    private TextView likeCounter;
    private ImageButton expandButton;
    private ImageView expandMoreIcon;
    private ImageView expandLessIcon;
    private TextView openHoursView;
    private TextView openText;
    private TextView addressView;
    private ImageButton gpsIcon_big;
    private ImageButton gpsIcon_small;
    private LinearLayout expandLayout;
    private NetworkImageViewTopCrop backgroundImageView;

    private AuthenticationDialog authDialog;
    private ProgressDialog progressDialog;
    private boolean expanded = false;
    private boolean expandButtonIsEnabled = true;

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

        if (MAX_HEIGHT == NOT_INITIALIZED){
            MAX_HEIGHT = getContext().getResources().getDimensionPixelSize(R.dimen.location_profile_max_height);
        }
        if (MIN_HEIGHT == NOT_INITIALIZED){
            MIN_HEIGHT = getContext().getResources().getDimensionPixelSize(R.dimen.location_profile_min_height);
        }

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
        openText = (TextView) mainLayout.findViewById((R.id.location_open_text));
        gpsIcon_big = (ImageButton) mainLayout.findViewById((R.id.location_gpsIcon_big));
        expandLayout = (LinearLayout) mainLayout.findViewById((R.id.location_expand_layout));
        addressView = (TextView) expandLayout.findViewById((R.id.location_address));
        gpsIcon_small = (ImageButton) expandLayout.findViewById((R.id.location_gpsIcon_small));
        backgroundImageView = (NetworkImageViewTopCrop) mainLayout.findViewById(R.id.location_background);

        expandButton.setOnClickListener(this);
        gpsIcon_big.setOnClickListener(this);
        gpsIcon_small.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        registerForContextMenu(mainLayout);

        final String imageUrl = URLFactory.get().createImageUrl(location);
        final ImageLoader imageLoader = AIxNetworkManager.getInstance().getImageLoader();
        backgroundImageView.setImageUrl(imageUrl, imageLoader);

        collapse();

        return mainLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        location.addObserver(this);
        AIxDataManager.getInstance().addObserver(this);
        update();
    }

    @Override
    public void onStop() {
        //AIxNetworkManager.getInstance().cancelAllRequests(location);
        location.deleteObserver(this);
        AIxDataManager.getInstance().deleteObserver(this);
        super.onStop();
    }

    public void expand(){

        expandLayout.setVisibility(View.VISIBLE);
        gpsIcon_big.setVisibility(View.GONE);
        expandMoreIcon.setVisibility(View.INVISIBLE);
        expandLessIcon.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
        params.height = MAX_HEIGHT;
        mainLayout.setLayoutParams(params);

        expanded = true;
    }

    public void collapse(){

        expandLayout.setVisibility(View.GONE);
        gpsIcon_big.setVisibility(View.VISIBLE);
        expandMoreIcon.setVisibility(View.VISIBLE);
        expandLessIcon.setVisibility(View.INVISIBLE);

        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
        params.height = MIN_HEIGHT;
        mainLayout.setLayoutParams(params);

        expanded = false;
    }

    public void update(){
        location.updateLikeable();
        updateLikeViews();
        locationNameView.setText(location.getName());
        String address = location.getStreet() + " " + location.getHouseNumber() + "\n" + location.getPostalCode() + " " + AIxDataManager.getInstance().getCity(location.getCityId()).getName();
        addressView.setText(address);
        if (location.getOpenHours() == null){
            openText.setVisibility(View.INVISIBLE);
            openHoursView.setText("");
        }
        else {
            openText.setVisibility(View.VISIBLE);
            openHoursView.setText(location.getOpenHours());
        }
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

    private void editDescription() {
        //TODO:
    }

    public void editOpenHours(){
        //TODO:
    }

    public void authenticate() {
        authDialog = new AuthenticationDialog(getActivity());
        authDialog.setLocation(location);

        authDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_expand_button:
                if (expandButtonIsEnabled){
                    expandButtonIsEnabled = false;
                    handler.postDelayed(enableExpandButton, EXPAND_BUTTON_DELAY_MS);

                    if (expanded){
                        collapse();
                    }
                    else{
                        expand();
                    }
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
                case AIxDataManager.OBSERVER_KEY_CHANGED_LOCATIONS:
                    update();
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_LISTING_SOURCE, location);
    }

    public void refresh(){
        location.updateLikeable();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_location, menu);
        boolean isAuthorized = location.isAuthorized();

        for (int i = 0; i < menu.size(); i++){
            MenuItem item = menu.getItem(i);
            switch(item.getItemId()){
                /*case R.id.context_edit_description:
                    if (isAuthorized){
                        item.setEnabled(true);
                        item.setVisible(true);
                    }
                    break;
                case R.id.context_edit_open_hours:
                    if (isAuthorized){
                        item.setEnabled(true);
                        item.setVisible(true);
                    }
                    break;*/
                case R.id.context_authenticate:
                    if (!isAuthorized){
                        item.setEnabled(true);
                        item.setVisible(true);
                    }
                    break;
            }
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.context_edit_description:
                editDescription();
                return true;
            case R.id.context_edit_open_hours:
                editOpenHours();
                return true;
            case R.id.context_authenticate:
                authenticate();
                return true;
        }
        return false;
    }
}
