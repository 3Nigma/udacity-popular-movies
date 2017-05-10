package ro.tuscale.udacity.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import ro.tuscale.udacity.popmovies.backend.RestManager;

@RealmClass
public class Movie implements Parcelable, RealmModel {
    @PrimaryKey
    @SerializedName("id")
    private int mId;
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

    // Non-backend provided values
    private boolean mIsFavorited;

    // Cached values
    private VideosList mVideos;
    private ReviewsPage mReviews;

    public Movie() {
        // Required by Realm
    }

    private Movie(Parcel in) {
        this.mId = in.readInt();
        this.mTitle = in.readString();
        this.mPosterPath = in.readString();
        this.mPlotSynopsis = in.readString();
        this.mVoteAverage = in.readDouble();
        this.mReleaseDate = in.readString();
        this.mVideos = in.readParcelable(VideosList.class.getClassLoader());
        this.mReviews = in.readParcelable(ReviewsPage.class.getClassLoader());
        this.mIsFavorited = (in.readByte() == 1);
    }

    public static Movie getFavoriteById(int movieId) {
        Realm realm = Realm.getDefaultInstance();
        Movie favMovie = realm.where(Movie.class)
                              .equalTo("mId", movieId)
                              .equalTo("mIsFavorited", true)
                              .findFirst();

        if (favMovie != null) {
            favMovie = realm.copyFromRealm(favMovie);
        }
        realm.close();

        return favMovie;
    }
    public static List<Movie> getAllFavorites() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Movie> favManagedMovies = realm.where(Movie.class)
                .equalTo("mIsFavorited", true)
                .findAll();
        List<Movie> favMovies = realm.copyFromRealm(favManagedMovies);

        realm.close();

        return favMovies;
    }

    public boolean isItFavorite() {
        return mIsFavorited;
    }
    public void setFavorited(final boolean isIt) {
        if (mIsFavorited != isIt) {
            // Indeed, there was a change
            Realm realm = Realm.getDefaultInstance();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mIsFavorited = isIt;
                    if (isIt) {
                        realm.copyToRealm(Movie.this);
                    } else {
                        RealmObject.deleteFromRealm(
                                realm.where(Movie.class)
                                    .equalTo("mId", Movie.this.getId())
                                    .equalTo("mIsFavorited", true)
                                    .findFirst()
                            );
                    }
                }
            });
            realm.close();
        }
    }

    public int getId() {
        return mId;
    }
    public String getTitle() {
        return mTitle;
    }

    public Single<VideosList> getVideosList() {
        Single<VideosList> movieVideoListObservable;

        if (mVideos == null) {
            // This is the first time the user requires the movie's videos. Fetch them from the internet
            RestManager restManager = new RestManager();

            movieVideoListObservable = restManager.getVideosForMovie(mId)
                    .doOnSuccess(new Consumer<VideosList>() {
                        @Override
                        public void accept(VideosList videosList) throws Exception {
                            mVideos = videosList;
                        }
                    });
        } else {
            // We already have the videos cached. Return them
            movieVideoListObservable = Single.create(new SingleOnSubscribe<VideosList>() {
                @Override
                public void subscribe(SingleEmitter<VideosList> e) throws Exception {
                    e.onSuccess(mVideos);
                }
            });
        }

        return movieVideoListObservable;
    }

    public Single<ReviewsPage> getReviewsPage(int pagNr) {
        Single<ReviewsPage> movieReviewsPageObservable;

        if (mReviews == null) {
            // No cached reviews available. Do network request
            RestManager restManager = new RestManager();

            movieReviewsPageObservable = restManager.getMovieReviewsPage(mId, pagNr)
                    .doOnSuccess(new Consumer<ReviewsPage>() {
                        @Override
                        public void accept(ReviewsPage reviewsPage) throws Exception {
                            // TODO: handle multiple page reviews (rare situation - didn't see any)
                            mReviews = reviewsPage;
                        }
                    });
        } else {
            // Cache reviews available. Retrieve them
            movieReviewsPageObservable = Single.create(new SingleOnSubscribe<ReviewsPage>() {
                @Override
                public void subscribe(SingleEmitter<ReviewsPage> e) throws Exception {
                    e.onSuccess(mReviews);
                }
            });
        }

        return movieReviewsPageObservable;
    }

    public String getPosterPath() {
        /*
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
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mPlotSynopsis);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mReleaseDate);
        dest.writeParcelable(mVideos, flags);
        dest.writeParcelable(mReviews, flags);
        dest.writeByte((byte) (mIsFavorited ? 1 : 0));
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
