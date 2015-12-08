package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.comm.GetPostsRequest;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.ListingSource;
import com.aix.city.core.ListingSourceType;
import com.aix.city.core.PostListing;
import com.android.volley.Response;

/**
 * Created by Thomas on 11.10.2015.
 */
public class Tag implements ListingSource {

    private int id;
    private String name;

    //no-argument constructor for JSON
    private Tag(){}

    //Parcelable constructor
    public Tag(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
    }

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        if(name == null){
            name = "";
        }
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public void requestPosts(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, int postNum, Post lastPost) {
        AIxNetworkManager.getInstance().requestPosts(listener, errorListener, postNum, lastPost, this);
    }

    @Override
    public PostListing createPostListing() {
        return new PostListing(this);
    }

    @Override
    public ListingSourceType getType() {
        return ListingSourceType.TAG;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return id == tag.id;

    }

    @Override
    public int hashCode() {
        return 73*id;
    }

    @Override
    public int describeContents() {
        return ListingSource.PARCEL_DESCRIPTION_TAG;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.describeContents());
        dest.writeInt(getId());
        dest.writeString(getName());
    }

    public static final Parcelable.Creator<Tag> CREATOR =
            new Parcelable.Creator<Tag>(){

                @Override
                public Tag createFromParcel(Parcel source) {
                    //read class description
                    source.readInt();
                    return new Tag(source);
                }

                @Override
                public Tag[] newArray(int size) {
                    return new Tag[size];
                }
            };
}
