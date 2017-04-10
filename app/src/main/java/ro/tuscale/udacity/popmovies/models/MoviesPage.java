package ro.tuscale.udacity.popmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ro.tuscale.udacity.popmovies.models.Movie;

public class MoviesPage {
    @SerializedName("page")
    private int mPageNumber;
    @SerializedName("results")
    private List<Movie> mMovies;
    @SerializedName("total_results")
    private int mTotalMovies;
    @SerializedName("total_pages")
    private int mTotalPages;

    public List<Movie> getMovies() {
        return mMovies;
    }
}
