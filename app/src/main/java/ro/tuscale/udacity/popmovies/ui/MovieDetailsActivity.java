package ro.tuscale.udacity.popmovies.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import ro.tuscale.udacity.popmovies.models.FavoriteMovieManager;
import ro.tuscale.udacity.popmovies.ui.models.MovieDetailsViewModel;
import ro.tuscale.udacity.popmovies.R;
import ro.tuscale.udacity.popmovies.ui.adapters.ReviewsAdapter;
import ro.tuscale.udacity.popmovies.ui.adapters.VideosAdapter;
import ro.tuscale.udacity.popmovies.databinding.ActivityMovieDetailsBinding;
import ro.tuscale.udacity.popmovies.models.Movie;
import ro.tuscale.udacity.popmovies.models.Video;

public class MovieDetailsActivity extends AppCompatActivity implements VideosAdapter.VideoClickHandler {
    public static final String MOVIE_EXTRA_INTENT_KEY = "ro.tuscale.udacity.popmovies.ui.MovieDetailsActivity.MOVIE_EXTRA_INTENT";

    private Movie mMovie;
    private FavoriteMovieManager mFavMovieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // A bit of non-mvvm setup
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Extract the movie payload
        Intent startingIntent = getIntent();
        Movie moviePayload = null;

        if (startingIntent != null) {
            moviePayload = startingIntent.getParcelableExtra(MOVIE_EXTRA_INTENT_KEY);

            if (moviePayload == null) {
                // Nothing to do but finish
                finish();
            }
            mMovie = moviePayload;

            // Initialize the movie Videos list. Start with dependencies
            RecyclerView rvVideos = (RecyclerView) findViewById(R.id.rv_movie_detail_trailers);
            if (rvVideos != null) {
                VideosAdapter videosAdapter = new VideosAdapter(this.getApplicationContext(), mMovie);

                videosAdapter.setVideoClickedHandler(this);
                rvVideos.setLayoutManager(new LinearLayoutManager(this));
                rvVideos.setAdapter(videosAdapter);
            }

            // Initialize the moview Reviews list. Start, again, with dependencies
            RecyclerView rvReviews = (RecyclerView) findViewById(R.id.rv_movie_detail_comments);
            if (rvReviews != null) {
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this.getApplicationContext(), mMovie);

                rvReviews.setLayoutManager(new LinearLayoutManager(this));
                rvReviews.setAdapter(reviewsAdapter);
            }

            // Load the movie to display its details
            binding.setMovie(new MovieDetailsViewModel(getApplicationContext(), moviePayload));
        }

        // Prepare the Favorite button
        final FloatingActionButton fabFavorite = (FloatingActionButton) findViewById(R.id.fab_movie_details_favorite);
        mFavMovieManager = new FavoriteMovieManager(this);
        if (fabFavorite != null) {
            if (mFavMovieManager.isMovieFavorited(mMovie)) {
                fabFavorite.setImageResource(R.drawable.ic_favorite_full_white_24dp);
            } else {
                fabFavorite.setImageResource(R.drawable.ic_favorite_empty_white_24dp);
            }
            fabFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isMovieFavorite = mFavMovieManager.isMovieFavorited(mMovie);

                    if (isMovieFavorite) {
                        fabFavorite.setImageResource(R.drawable.ic_favorite_empty_white_24dp);
                        mFavMovieManager.removeFromFavorites(mMovie);
                    } else {
                        fabFavorite.setImageResource(R.drawable.ic_favorite_full_white_24dp);
                        mFavMovieManager.addToFavorites(mMovie);
                    }
                }
            });
        }

        // Add a bit of sparkling animation to the FAB
        NestedScrollView contentScrollView = (NestedScrollView) findViewById(R.id.nsv_movie_detail_scroll_host);
        if (contentScrollView != null) {
            contentScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY - oldScrollY > 0) {
                        fabFavorite.hide();
                    } else {
                        fabFavorite.show();
                    }
                }
            });
        }

        // Set screen title to the movie name
        setTitle(mMovie.getTitle());
    }

    @Override
    public void onVideoClicked(Video video) {
        Intent videoPlayIntent = new Intent(this, MovieDetailsActivity.class);

        switch(video.getSite()) {
            case "YouTube":
                videoPlayIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
                break;
            default:
                Toast.makeText(this, "Trailer video source not supported.", Toast.LENGTH_SHORT).show();
        }

        startActivity(videoPlayIntent);
    }
}
