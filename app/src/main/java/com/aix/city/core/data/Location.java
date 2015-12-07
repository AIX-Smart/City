package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.EditableEventListing;
import com.aix.city.core.Likeable;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.android.volley.Response;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Location extends Likeable implements ListingSource {

    private int id;
    private String name;
    private List<Tag> tags;
    private String description;
    private int cityId;
    private String street;
    private String houseNumber;
    private String phoneNumber;
    private String gps;
    //TODO: öffnungszeiten

    //no-argument constructor for JSON
    public Location(){}

    //Parcelable constructor
    public Location(Parcel in){
        super(in);
        id = in.readInt();
        name = in.readString();
        tags = new ArrayList<Tag>();
        in.readTypedList(tags, Tag.CREATOR);
        description = in.readString();
        cityId = in.readInt();
        street = in.readString();
        houseNumber = in.readString();
        phoneNumber = in.readString();
        gps = in.readString();
    }

    public Location(int id, String name, List<Tag> tags, String description, int cityId, String street, String houseNumber, String phoneNumber, boolean liked, int likeCount) {
        super(liked, likeCount);
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.description = description;
        this.cityId = cityId;
        this.street = street;
        this.houseNumber = houseNumber;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        if(name == null){
            name = "";
        }
        return name;
    }

    @NonNull
    public List<Tag> getTags() {
        if(tags == null){
            tags = new ArrayList<Tag>();
        }
        return tags;
    }

    @NonNull
    public String getDescription() {
        if(description == null){
            description = "";
        }
        return description;
    }

    public int getCityId() {
        return cityId;
    }

    @NonNull
    public String getStreet() {
        if(street == null){
            street = "";
        }
        return street;
    }

    @NonNull
    public String getHouseNumber() {
        if(houseNumber == null){
            houseNumber = "";
        }
        return houseNumber;
    }

    @NonNull
    public String getPhoneNumber() {
        if(phoneNumber == null){
            phoneNumber = "";
        }
        return phoneNumber;
    }

    @NonNull
    public String getGps() {
        if(gps == null){
            gps = "";
        }
        return gps;
    }

    public void addTag(Tag tag){
        tags.add(tag);
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
    }

    @Override
    public EditableEventListing createPostListing() {
        return new EditableEventListing(this);
    }

    @Override
    public void requestPosts(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost) {
        AIxNetworkManager.getInstance().requestPosts(listener, errorListener, postNum, lastPost, this);
    }

    @Override
    public ListingSourceType getType() {
        return ListingSourceType.LOCATION;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return id == location.id;

    }

    @Override
    public int hashCode() {
        return 59*id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeTypedList(getTags());
        dest.writeString(getDescription());
        dest.writeInt(getCityId());
        dest.writeString(getStreet());
        dest.writeString(getHouseNumber());
        dest.writeString(getPhoneNumber());
        dest.writeString(getGps());
    }

    public static final Parcelable.Creator<Location> CREATOR =
            new Parcelable.Creator<Location>(){

                @Override
                public Location createFromParcel(Parcel source) {
                    return new Location(source);
                }

                @Override
                public Location[] newArray(int size) {
                    return new Location[size];
                }
            };
}
