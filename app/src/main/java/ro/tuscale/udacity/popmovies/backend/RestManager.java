package ro.tuscale.udacity.popmovies.backend;

import android.support.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.tuscale.udacity.popmovies.BuildConfig;
import ro.tuscale.udacity.popmovies.models.MoviesPage;

public class RestManager {
    private Retrofit mRetrofit;

    public RestManager() {
        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_URI_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Single<MoviesPage> getSortedMoviePage(int pageNr, @NonNull SortingType sort) {
        if (pageNr < 1) {
            throw new IllegalArgumentException("Page number cannot be less than 1.");
        }
        TMDBService movieService = mRetrofit.create(TMDBService.class);
        Single<MoviesPage> moviesSource = null;

        switch (sort) {
            case MOST_POPULAR:
                moviesSource = movieService.getPopularMovies(BuildConfig.TMDB_API_KEY, pageNr);
                break;
            case TOP_RATED:
                moviesSource = movieService.getTopRatedMovies(BuildConfig.TMDB_API_KEY, pageNr);
                break;
        }

        return moviesSource
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
