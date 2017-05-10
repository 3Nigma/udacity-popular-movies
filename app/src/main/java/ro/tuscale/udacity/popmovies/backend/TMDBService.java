package ro.tuscale.udacity.popmovies.backend;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ro.tuscale.udacity.popmovies.models.MoviesPage;
import ro.tuscale.udacity.popmovies.models.ReviewsPage;
import ro.tuscale.udacity.popmovies.models.VideosList;

public interface TMDBService {
    @GET("movie/popular")
    Single<MoviesPage> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int pag);

    @GET("movie/top_rated")
    Single<MoviesPage> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pag);

    @GET("movie/{id}/videos")
    Single<VideosList> getVideosForMovieId(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Single<ReviewsPage> getReviewsPageForMovieId(@Path("id") int movieId,
                                                 @Query("api_key") String apiKey, @Query("page") int pag);
}
