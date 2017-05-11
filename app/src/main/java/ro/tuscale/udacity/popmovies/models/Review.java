package ro.tuscale.udacity.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {
    @SerializedName("content")
    private String mContent;
    @SerializedName("author")
    private String mAuthor;

    public Review() {
        // Required by Realm
    }

    private Review(Parcel in) {
        mContent = in.readString();
        mAuthor = in.readString();
    }

    public String getAuthor() {
        return mAuthor;
    }
    public String getContent() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mContent);
        dest.writeString(mAuthor);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
