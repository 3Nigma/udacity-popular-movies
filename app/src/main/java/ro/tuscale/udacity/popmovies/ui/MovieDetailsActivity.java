package ro.tuscale.udacity.popmovies.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ro.tuscale.udacity.popmovies.MovieDetailsViewModel;
import ro.tuscale.udacity.popmovies.R;
import ro.tuscale.udacity.popmovies.databinding.ActivityMovieDetailsBinding;
import ro.tuscale.udacity.popmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String MOVIE_EXTRA_INTENT_KEY = "ro.tuscale.udacity.popmovies.ui.MovieDetailsActivity.MOVIE_EXTRA_INTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // A bit of non-mvvm setup
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Extract the moview payload
        Intent startingIntent = getIntent();
        Movie moviePayload = null;

        if (startingIntent != null) {
            moviePayload = startingIntent.getParcelableExtra(MOVIE_EXTRA_INTENT_KEY);

            if (moviePayload == null) {
                // Nothing to do but finish
                finish();
            }
            binding.setMovie(new MovieDetailsViewModel(getApplicationContext(), moviePayload));
        }
    }
}
