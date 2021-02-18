package com.example.notetoself;

import android.provider.BaseColumns;

public class Notes implements BaseColumns {
    private Notes() {
    }

    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_TIMESTAMP = "timestamp";
}
