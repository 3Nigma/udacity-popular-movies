package ro.tuscale.udacity.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    @SerializedName("original_title")
    private String mTitle;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("overview")
    private String mPlotSynopsis;
    @SerializedName("vote_average")
    private double mVoteAverage;

    // TODO: make this a 'Date' object (also involves touching the RestManager)
    @SerializedName("release_date")
    private String mReleaseDate;

    private Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mPosterPath = in.readString();
        this.mPlotSynopsis = in.readString();
        this.mVoteAverage = in.readDouble();
        this.mReleaseDate = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterPath() {
        /**
         * will be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original".
         * For most phones we recommend using “w185”.
         */
        String size = "w185";

        return "http://image.tmdb.org/t/p/" + size + "/" + mPosterPath;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mPlotSynopsis);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
