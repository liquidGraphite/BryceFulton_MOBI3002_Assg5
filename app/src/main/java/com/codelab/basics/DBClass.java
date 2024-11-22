package com.codelab.basics;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;


/**
 * Created by w0091766 on 4/29/2016.
 */
public class DBClass extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "DB_Name.db";

    // Table and column names for Pokémon
    private static final String TABLE_NAME = "pokemon_table";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_POWER_LEVEL = "power_level";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ACCESS_COUNT = "access_count";

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_NUMBER + " INTEGER NOT NULL, " +
                COLUMN_POWER_LEVEL + " INTEGER NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_ACCESS_COUNT + " INTEGER DEFAULT 0" +
                ");");

        // Pre-load database with Pokémon data
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES " +
                "('Pikachu', 25, 900, 'Electric type', 0), " +
                "('Charmander', 4, 700, 'Fire type', 0), " +
                "('Bulbasaur', 1, 600, 'Grass/Poison type', 0), " +
                "('Squirtle', 7, 650, 'Water type', 0), " +
                "('Jigglypuff', 39, 400, 'Fairy/Normal type', 0), " +
                "('Meowth', 52, 500, 'Normal type', 0);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle schema changes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
