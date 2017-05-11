package ro.tuscale.udacity.popmovies.ui.models;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import ro.tuscale.udacity.popmovies.models.Movie;

public class MovieDetailsViewModel {
    private Context mContext;
    private Movie mMovie;

    public MovieDetailsViewModel(Context ctx, Movie movie) {
        this.mContext = ctx;
        this.mMovie = movie;
    }

    public String getTitle() {
        return mMovie.getTitle();
    }

    public String getReleaseDate() {
        return mMovie.getReleaseDate();
    }

    public String getAverageMark() {
        return String.format("%.1f/10", getAverageRating());
    }
    public float getAverageRating() {
        return (float) mMovie.getVoteAverage();
    }

    public String getPloySynopsis() {
        return mMovie.getPlotSynopsis();
    }

    public RequestCreator loadPosterImage() {
        return Picasso.with(mContext).load(mMovie.getPosterPath());
    }
}
