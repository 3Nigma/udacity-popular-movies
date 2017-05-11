package ro.tuscale.udacity.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReviewsPage implements Parcelable {
    @SerializedName("results")
    private List<Review> mReviews;

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
        mReviews = new ArrayList<>();
        in.readList(mReviews, Review.class.getClassLoader());
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
