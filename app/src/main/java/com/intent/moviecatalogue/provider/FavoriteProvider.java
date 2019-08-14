package com.intent.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.intent.moviecatalogue.database.Favorite;
import com.intent.moviecatalogue.database.RoomDatabases;
import com.intent.moviecatalogue.interfaces.FavoriteInterface;

import static com.intent.moviecatalogue.Public.AUTHORITY;
import static com.intent.moviecatalogue.Public.KEY_FDIR;
import static com.intent.moviecatalogue.Public.KEY_FITEM;
import static com.intent.moviecatalogue.Public.TABLE_NAME;
import static com.intent.moviecatalogue.Public.uriMatcher;

public class FavoriteProvider extends ContentProvider {

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, KEY_FDIR);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/*", KEY_FITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = uriMatcher.match(uri);
        if (code == KEY_FDIR || code == KEY_FITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            FavoriteInterface favoriteInterface = RoomDatabases.getInstance(context).favoriteInterface();
            final Cursor cursor;
            if (code == KEY_FDIR) {
                cursor = favoriteInterface.showFilterKindColumn(selection);
            } else {
                cursor = favoriteInterface.showFavoriteFilterByKindId(selection, ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("URI Not Found : " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case KEY_FITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
            case KEY_FDIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_NAME;
            default:
                throw new IllegalArgumentException("URI Not Found : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case KEY_FDIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                if (values == null) {
                    return null;
                }
                final long id = RoomDatabases.getInstance(context).favoriteInterface().insertToProvider(Favorite.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case KEY_FITEM:
                throw new IllegalArgumentException("Cannot Insert, URI Problem : " + uri);
            default:
                throw new IllegalArgumentException("URI Not Found : " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case KEY_FDIR:
                throw new IllegalArgumentException("Cannot Insert, URI Problem " + uri);
            case KEY_FITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                if (values == null) {
                    return 0;
                }
                final Favorite favorite = Favorite.fromContentValues(values);
                favorite.setId(ContentUris.parseId(uri));
                final int count = RoomDatabases.getInstance(context).favoriteInterface().updateToProvider(favorite);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("URI Not Found : " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case KEY_FDIR:
                throw new IllegalArgumentException("Cannot Insert, URI Problem " + uri);
            case KEY_FITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = RoomDatabases.getInstance(context).favoriteInterface().deleteFavoriteFilterId(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("URI Not Found : " + uri);
        }
    }


}
