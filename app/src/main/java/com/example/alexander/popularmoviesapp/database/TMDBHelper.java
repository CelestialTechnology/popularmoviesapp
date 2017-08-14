package com.example.alexander.popularmoviesapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by awest on 8/11/2017.
 */

public class TMDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tdmbfavorites.db";
    private static final int DATABASE_VERSION = 2;

    public TMDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " +
                TMDBContract.FavoriteMovieEntry.TABLE_NAME + "(" +
                TMDBContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL," +
                TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER + " BLOB NOT NULL," +
                TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP + " BLOB NOT NULL" +
                ");";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TMDBContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
