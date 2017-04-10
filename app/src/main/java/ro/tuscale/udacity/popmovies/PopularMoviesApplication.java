package ro.tuscale.udacity.popmovies;

import android.app.Application;

import timber.log.Timber;

public class PopularMoviesApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
