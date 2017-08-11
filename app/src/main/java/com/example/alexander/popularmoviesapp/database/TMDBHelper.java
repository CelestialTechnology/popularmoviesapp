package com.example.alexander.popularmoviesapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by awest on 8/11/2017.
 */

public class TMDBHelper extends SQLiteOpenHelper {

    public TMDBHelper(Context context) {
        // TODO: FILL In the proper credentials for this project
        super(context, "", null, 0);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
