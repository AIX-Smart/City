package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.aix.city.core.EditableEventListing;
import com.aix.city.core.Likeable;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Location extends Likeable implements ListingSource {

    private int id;
    private String name;
    private int[] tagIds;
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
        tagIds = in.createIntArray();
        description = in.readString();
        cityId = in.readInt();
        street = in.readString();
        houseNumber = in.readString();
        phoneNumber = in.readString();
        gps = in.readString();
    }

    public Location(int id, String name, int[] tags, String description, int cityId, String street, String houseNumber, String phoneNumber, boolean liked, int likeCount) {
        super(liked, likeCount);
        this.id = id;
        this.name = name;
        this.tagIds = tags;
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
    @JsonIgnore
    public List<Tag> getTags() {
        return new ArrayList<Tag>();
    }

    @NonNull
    public int[] getTagIds() {
        if (tagIds == null){
            tagIds = new int[0];
        }
        return tagIds;
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

    public void addTag(Tag tag) {
        int size = tagIds.length;
        int[] newArray = new int[size + 1];
        for (int i = 0; i < tagIds.length; i++){
            newArray[i] = tagIds[i];
        }
        newArray[tagIds.length] = tag.getId();
        tagIds = newArray;
    }

    @Override
    public EditableEventListing createPostListing() {
        return new EditableEventListing(this);
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
        return getType().getParcelDescription();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.describeContents());
        super.writeToParcel(dest, flags);
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeIntArray(getTagIds());
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
                    //read class description
                    source.readInt();
                    return new Location(source);
                }

                @Override
                public Location[] newArray(int size) {
                    return new Location[size];
                }
            };

}
