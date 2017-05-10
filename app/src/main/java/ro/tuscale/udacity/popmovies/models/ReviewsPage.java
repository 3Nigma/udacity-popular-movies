package ro.tuscale.udacity.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ReviewsPage implements Parcelable, RealmModel {
    @SerializedName("results")
    private RealmList<Review> mReviews;

    // TODO: use these fields if needed
//    @SerializedName("page")
//    private int mPageNumber;
//    @SerializedName("total_results")
//    private int mTotalMovies;
//    @SerializedName("total_pages")
//    private int mTotalPages;

    public ReviewsPage() {
        // Required by Realm
    }

    private ReviewsPage(Parcel in) {
        mReviews = new RealmList<>();
        mReviews.addAll(in.readArrayList(Review.class.getClassLoader()));
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mReviews);
    }

    public static final Parcelable.Creator<ReviewsPage> CREATOR = new Parcelable.Creator<ReviewsPage>() {
        @Override
        public ReviewsPage createFromParcel(Parcel source) {
            return new ReviewsPage(source);
        }

        @Override
        public ReviewsPage[] newArray(int size) {
            return new ReviewsPage[size];
        }
    };
}
