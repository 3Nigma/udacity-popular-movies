package ro.tuscale.udacity.popmovies.models;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ro.tuscale.udacity.popmovies.persistance.FavoriteMoviesProvider;
import ro.tuscale.udacity.popmovies.persistance.MoviesDatabaseHelper;

public class FavoriteMovieManager {
    private ContentResolver mContentResolver;

    public FavoriteMovieManager(@NonNull Context ctx) {
        this.mContentResolver = ctx.getContentResolver();
    }

    public void addToFavorites(@NonNull Movie movie) {
        changeFavoriteState(movie, true);
    }
    public void removeFromFavorites(@NonNull Movie movie) {
        changeFavoriteState(movie, false);
    }

    private void changeFavoriteState(@NonNull Movie movie, boolean isItFavorited) {
        if (isItFavorited) {
            ContentValues cValues = new ContentValues();
            Gson gson = new Gson();

            cValues.put(FavoriteMoviesProvider._ID, movie.getId());
            cValues.put(FavoriteMoviesProvider._MOVIE, gson.toJson(movie));
            mContentResolver.insert(FavoriteMoviesProvider.CONTENT_URI, cValues);
        } else {
            mContentResolver.delete(FavoriteMoviesProvider.CONTENT_URI,
                    MoviesDatabaseHelper.TABLE_FAVORITE_MOVIES_COLUMN_ID + " = ?", new String[] {String.valueOf(movie.getId())});
        }
    }

    public List<Movie> getAllMovies() {
        List<Movie> favMovies = new ArrayList<>();
        Cursor fmCursor = mContentResolver.query(
                FavoriteMoviesProvider.CONTENT_URI,
                new String[] {FavoriteMoviesProvider._ID, FavoriteMoviesProvider._MOVIE},
                null, null, null);
        String jMovieValue;
        Gson gson = new Gson();

        for (fmCursor.moveToFirst(); !fmCursor.isAfterLast(); fmCursor.moveToNext()) {
            jMovieValue = fmCursor.getString(fmCursor.getColumnIndex(FavoriteMoviesProvider._MOVIE));
            favMovies.add(gson.fromJson(jMovieValue, Movie.class));
        }

        return favMovies;
    }

    public Movie getMovieById(int movieId) {
        Movie favMovie = null;
        Cursor fmCursor = mContentResolver.query(
                FavoriteMoviesProvider.CONTENT_URI,
                new String[] {FavoriteMoviesProvider._ID, FavoriteMoviesProvider._MOVIE},
                MoviesDatabaseHelper.TABLE_FAVORITE_MOVIES_COLUMN_ID + " = ?",
                new String[] {String.valueOf(movieId)},
                null
        );
        String jMovieValue;
        Gson gson = new Gson();

        if (fmCursor.moveToFirst()) {
            jMovieValue = fmCursor.getString(fmCursor.getColumnIndex(FavoriteMoviesProvider._MOVIE));
            favMovie = gson.fromJson(jMovieValue, Movie.class);
        }

        return favMovie;
    }

    public boolean isMovieFavorited(Movie movie) {
        Cursor fmCursor = mContentResolver.query(
                FavoriteMoviesProvider.CONTENT_URI,
                new String[] {FavoriteMoviesProvider._ID},
                MoviesDatabaseHelper.TABLE_FAVORITE_MOVIES_COLUMN_ID + " = ?",
                new String[] {String.valueOf(movie.getId())},
                null
        );

        return fmCursor.getCount() == 1;
    }
}
