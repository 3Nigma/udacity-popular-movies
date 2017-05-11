package ro.tuscale.udacity.popmovies.persistance;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavoriteMoviesProvider extends ContentProvider {
    private static final String AUTHORITY = "ro.tuscale.udacity.popmovies.provider";
    private static final String BASE_PATH = "fav_movies";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH;

    public static final String _ID = "_ID";
    public static final String _MOVIE = "movie_cached_data";

    private MoviesDatabaseHelper mOpenHelper;

    // Uri matcher helper init
    private static final int FAV_MOVIES_MATCH_ID = 16;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, FAV_MOVIES_MATCH_ID);
    }

    @Override
    public boolean onCreate() {
        this.mOpenHelper = new MoviesDatabaseHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db;
        Cursor fmCursor;

        // Sanity check
        checkRequestedProjection(projection);
        if (sURIMatcher.match(uri) != FAV_MOVIES_MATCH_ID) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // All the magic follows
        db = mOpenHelper.getReadableDatabase();
        fmCursor = db.query(MoviesDatabaseHelper.TABLE_FAVORITE_MOVIES_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        return fmCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long insertId = 0;

        switch (sURIMatcher.match(uri)) {
            case FAV_MOVIES_MATCH_ID:
                SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                insertId = db.insert(MoviesDatabaseHelper.TABLE_FAVORITE_MOVIES_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + insertId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted = 0;

        switch (sURIMatcher.match(uri)) {
            case FAV_MOVIES_MATCH_ID:
                SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                rowsDeleted = db.delete(MoviesDatabaseHelper.TABLE_FAVORITE_MOVIES_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // No-op
        return 0;
    }

    private void checkRequestedProjection(String[] projection) {
        String[] available = { MoviesDatabaseHelper.TABLE_FAVORITE_MOVIES_COLUMN_ID, MoviesDatabaseHelper.TABLE_FAVORITE_MOVIES_COLUMN_VALUE};
        if (projection != null) {
            boolean isContained;

            for (String requestedColumn : projection) {
                isContained = false;
                for (String availableColumn : available) {
                    if (availableColumn.equals(requestedColumn)) {
                        isContained = true;
                        break;
                    }
                }

                if (isContained == false) {
                    throw new IllegalArgumentException(String.format("Unknown column '%s' present in projection.", requestedColumn));
                }
            }
        }
    }
}
