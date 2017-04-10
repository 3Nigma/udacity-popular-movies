package ro.tuscale.udacity.popmovies.backend;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ro.tuscale.udacity.popmovies.models.MoviesPage;

public interface TMDBService {
    @GET("movie/popular")
    Single<MoviesPage> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int pag);

    @GET("movie/top_rated")
    Single<MoviesPage> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pag);

}
