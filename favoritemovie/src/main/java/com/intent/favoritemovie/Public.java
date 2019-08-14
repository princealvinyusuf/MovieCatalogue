package com.intent.favoritemovie;

import android.net.Uri;
import android.provider.BaseColumns;

public class Public {
    //Key-Value
    public static final String KEY_CATALOGUE_FAVORITE = "key_catalogue_favorite";

    //Properties
    public static final int LOADER_FAVORITE = 1;

    //Database
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_KIND = "kind";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RELEASE = "release";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POSTER = "poster";
    public static final String TABLE_NAME = "favorite_table";
    private static final String AUTHORITY = "com.intent.favoritemovie.database";
    public static final Uri URI_FAVORITE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
}
