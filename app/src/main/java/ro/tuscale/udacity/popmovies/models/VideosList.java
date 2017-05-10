package ro.tuscale.udacity.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class VideosList implements Parcelable, RealmModel {
    @SerializedName("results")
    private RealmList<Video> mVideos;

    public VideosList() {
        // Required by Realm
    }

    private VideosList(Parcel in) {
        mVideos = new RealmList<>();
        mVideos.addAll(in.readArrayList(Video.class.getClassLoader()));
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
