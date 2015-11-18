package com.aix.city.core.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aix.city.comm.AIxJacksonRequest;
import com.aix.city.comm.TagEventRequest;
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
        if(name == null) this.name = "";
        else this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public AIxJacksonRequest createRequest(Response.Listener<Post[]> listener, Response.ErrorListener errorListener, boolean ignoreCache, int postNum, Post lastPost) {
        return new TagEventRequest(listener, errorListener, ignoreCache, postNum, lastPost, this);
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
        if (!super.equals(o)) return false;

        Tag tag = (Tag) o;

        return name.equals(tag.name);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Tag> CREATOR =
            new Parcelable.Creator<Tag>(){

                @Override
                public Tag createFromParcel(Parcel source) {
                    return new Tag(source);
                }

                @Override
                public Tag[] newArray(int size) {
                    return new Tag[size];
                }
            };
}
