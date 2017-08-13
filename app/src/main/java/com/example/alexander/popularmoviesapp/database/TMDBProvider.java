package com.example.alexander.popularmoviesapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by awest on 8/11/2017.
 */

public class TMDBProvider extends ContentProvider {

    private TMDBHelper tmdbHelper;

    // Codes for the different Uri types
    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Uri matcher
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add the needed uris to the matcher
        // Uri for all database contents
        uriMatcher.addURI(
                TMDBContract.CONTENT_AUTHORITY,
                TMDBContract.PATH_FAVORITE_MOVIES,
                FAVORITE_MOVIES);
        // Uri for a single stored movie
        uriMatcher.addURI(
                TMDBContract.CONTENT_AUTHORITY,
                TMDBContract.PATH_FAVORITE_MOVIES + "/#",
                FAVORITE_MOVIE_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        tmdbHelper = new TMDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = tmdbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIES:
                cursor = db.query(
                        TMDBContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String[] mSelectionArgs = new String[]{uri.getLastPathSegment()};
                String select = TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?";
                cursor = db.query(
                        TMDBContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        select,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri.toString());
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = tmdbHelper.getWritableDatabase();

        Uri returnUri;
        // This application will only insert one favorite movie at a time
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIES:
                long id = db.insert(
                        TMDBContract.FavoriteMovieEntry.TABLE_NAME,
                        null,
                        values
                );

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TMDBContract.FavoriteMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert movie into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri.toString());
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = tmdbHelper.getWritableDatabase();
        int rowsDeleted;
        // This will only allow deletion of one movie at a time
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIE_WITH_ID:
                String[] mSelctionArgs = new String[]{uri.getLastPathSegment()};
                rowsDeleted = db.delete(
                        TMDBContract.FavoriteMovieEntry.TABLE_NAME,
                        TMDBContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?",
                        mSelctionArgs
                );

                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri For Deletion: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            throw new android.database.SQLException("Failure to delete movie in database with uri: " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
