package com.intent.moviecatalogue;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.intent.moviecatalogue.model.Movie;

import java.util.ArrayList;

public class Public {

    //Database
    public static final String DATABASE_NAME = "movie_db";
    public static final String TABLE_NAME = "favorite_table";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_KIND = "kind";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RELEASE = "release";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POSTER = "poster";

    //Notification
    public static final String NORMAL_DAILY_REMINDER = "DailyReminder";
    public static final String NORMAL_RELEASE_REMINDER = "ReleaseReminder";
    public static final String LONG_DAILY_REMINDER = "LongDailyReminder";
    public static final String LONG_RELEASE_REMINDER = "LongReleasseReminder";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_TITLE = "title";

    public static String CHANNEL_ID = "Channel_1";
    public static String CHANNEL_NAME = "Reminder channel";

    public static final String DAILY_REMINDER_EXTRA = "daily_reminder_extra";
    public static final String RELEASE_REMINDER_EXTRA = "release_reminder_extra";
    public static final String CHANGE_DAILY_REMINDER = "daily_reminder_change";
    public static final String CHANGE_RELEASE_REMINDER = "release_reminder_change";

    //Extra Key Data
    public static final String apiKey = BuildConfig.PRINCE_DEVELOPER_DB_KEY;
    public static final String KEY_TVSHOW = "tvshow";
    public static final String KEY_SEARCH_TVSHOW = "key_search_tvshow";
    public static final String KEY_MOVIES = "movie";
    public static final String KEY_SEARCH_MOVIES = "key_search_movie";


    //Properties
    public static String title;
    public static ArrayList<Movie> type;
    public static final int FAVORITE_LOAD_KEY = 1;
    public static final int ID_DAILY_REMINDER = 100;
    public static final int ID_RELEASE_REMINDER = 101;

    //Key-Value
    public static final String KEY_CATALOGUE = "key_catalogue";
    public static final String KEY_CATALOGUE_FAVORITE = "key_catalogue_favorite";
    public static final String KEY_CATALOGUE_LIST = "key_catalogue_list";
    public static final String CATALOGUE_MOVIE = "catalogue_movie";

    //Provider
    public static final String AUTHORITY = "com.intent.moviecatalogue.provider";
    public static final Uri URI_FAVORITE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    public static final int KEY_FDIR = 1;
    public static final int KEY_FITEM = 2;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Collection Widget
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_RELEASE = "EXTRA_RELEASE";
    public static final String EXTRA_OVERVIEW = "EXTRA_OVERVIEW";
    public static final String EXTRA_POSTER = "EXTRA_POSTER";
    public static final String EXTRA_CATALOGUE = "EXTRA_CATALOGUE";
    public static final String GET_WIDGET_UPDATE = "UPDATE_FAVORITE";
    public static final String TOAST_ACTION = "TOAST_ACTION";

}
