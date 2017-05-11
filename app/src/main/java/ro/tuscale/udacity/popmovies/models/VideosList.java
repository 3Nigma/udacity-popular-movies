package ro.tuscale.udacity.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VideosList implements Parcelable {
    @SerializedName("results")
    private List<Video> mVideos;

    public VideosList() {
        // Required by Realm
    }

    private VideosList(Parcel in) {
        mVideos = new ArrayList<>();
        in.readList(mVideos, Video.class.getClassLoader());
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mVideos);
    }

    public static final Parcelable.Creator<VideosList> CREATOR = new Parcelable.Creator<VideosList>() {
        @Override
        public VideosList createFromParcel(Parcel source) {
            return new VideosList(source);
        }

        @Override
        public VideosList[] newArray(int size) {
            return new VideosList[size];
        }
    };
}
