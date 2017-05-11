package ro.tuscale.udacity.popmovies.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class MoviesDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    public static final String TABLE_FAVORITE_MOVIES_NAME = "favorite_movies";
    public static final String TABLE_FAVORITE_MOVIES_COLUMN_ID = "movie_id";
    public static final String TABLE_FAVORITE_MOVIES_COLUMN_VALUE = "movie_json";

    MoviesDatabaseHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_FAVORITE_MOVIES_NAME + " " +
                    "(_ID integer primary key, " + TABLE_FAVORITE_MOVIES_COLUMN_ID + " integer, " + TABLE_FAVORITE_MOVIES_COLUMN_VALUE + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_FAVORITE_MOVIES_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
