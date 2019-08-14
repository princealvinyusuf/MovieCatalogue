package com.intent.moviecatalogue.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import static com.intent.moviecatalogue.Public.COLUMN_ID;
import static com.intent.moviecatalogue.Public.COLUMN_KIND;
import static com.intent.moviecatalogue.Public.COLUMN_OVERVIEW;
import static com.intent.moviecatalogue.Public.COLUMN_POSTER;
import static com.intent.moviecatalogue.Public.COLUMN_RELEASE;
import static com.intent.moviecatalogue.Public.COLUMN_TITLE;
import static com.intent.moviecatalogue.Public.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Favorite implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    private long id;

    @ColumnInfo(name = COLUMN_KIND)
    private String kind;

    @ColumnInfo(name = COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = COLUMN_RELEASE)
    private String release;

    @ColumnInfo(name = COLUMN_OVERVIEW)
    private String overview;

    @ColumnInfo(name = COLUMN_POSTER)
    private String poster;

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    public Favorite() {
    }

    public Favorite(long id, String kind, String title, String release, String overview, String poster) {
        this.id = id;
        this.kind = kind;
        this.title = title;
        this.release = release;
        this.overview = overview;
        this.poster = poster;
    }

    public Favorite(Cursor cursor) {
        this.id = getColumnLong(cursor, COLUMN_ID);
        this.kind = getColumnString(cursor, COLUMN_KIND);
        this.title = getColumnString(cursor, COLUMN_TITLE);
        this.release = getColumnString(cursor, COLUMN_RELEASE);
        this.overview = getColumnString(cursor, COLUMN_OVERVIEW);
        this.poster = getColumnString(cursor, COLUMN_POSTER);
    }

    protected Favorite(Parcel in) {
        this.id = in.readLong();
        this.kind = in.readString();
        this.title = in.readString();
        this.release = in.readString();
        this.overview = in.readString();
        this.poster = in.readString();
    }

    public static ArrayList<Favorite> cursorMapping(Cursor cursor) {
        ArrayList<Favorite> mFavoriteArrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String kind = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KIND));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String release = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER));
            mFavoriteArrayList.add(new Favorite(id, kind, title, release, overview, poster));
        }
        return mFavoriteArrayList;
    }

    public static Favorite fromContentValues(ContentValues values) {
        final Favorite favorite = new Favorite();
        if (values.containsKey(COLUMN_ID)) {
            favorite.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_KIND)) {
            favorite.kind = values.getAsString(COLUMN_KIND);
        }
        if (values.containsKey(COLUMN_TITLE)) {
            favorite.title = values.getAsString(COLUMN_TITLE);
        }
        if (values.containsKey(COLUMN_RELEASE)) {
            favorite.release = values.getAsString(COLUMN_RELEASE);
        }
        if (values.containsKey(COLUMN_OVERVIEW)) {
            favorite.overview = values.getAsString(COLUMN_OVERVIEW);
        }
        if (values.containsKey(COLUMN_POSTER)) {
            favorite.poster = values.getAsString(COLUMN_POSTER);
        }
        return favorite;
    }

    private static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.kind);
        dest.writeString(this.title);
        dest.writeString(this.release);
        dest.writeString(this.overview);
        dest.writeString(this.poster);
    }
}
