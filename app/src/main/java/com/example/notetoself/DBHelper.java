package com.example.notetoself;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notes.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                Notes.TABLE_NAME + " (" +
                Notes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Notes.COLUMN_TITLE + " TEXT NOT NULL, " +
                Notes.COLUMN_TEXT + " TEXT NOT NULL, " +
                Notes.COLUMN_IMAGE + " TEXT , " +
                Notes.COLUMN_TIMESTAMP + " INTEGER DEFAULT "+ System.currentTimeMillis() +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Notes.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
