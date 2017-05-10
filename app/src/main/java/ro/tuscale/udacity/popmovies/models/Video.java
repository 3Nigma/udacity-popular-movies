package ro.tuscale.udacity.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Video implements Parcelable, RealmModel {
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("site")
    private String mSite;
    @SerializedName("type")
    private String mType;

    public Video() {
        // Required by Realm
    }

    private Video(Parcel in) {
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mType = in.readString();
    }

    public String getKey() {
        return mKey;
    }
    public String getName() {
        return mName;
    }
    public String getSite() {
        return mSite;
    }
    public String getType() {
        return mType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeString(mType);
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
