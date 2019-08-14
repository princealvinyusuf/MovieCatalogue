package com.intent.moviecatalogue.interfaces;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.intent.moviecatalogue.database.Favorite;

import java.util.List;
import static com.intent.moviecatalogue.Public.COLUMN_ID;
import static com.intent.moviecatalogue.Public.COLUMN_KIND;
import static com.intent.moviecatalogue.Public.TABLE_NAME;

@Dao
public interface FavoriteInterface {

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " +
            COLUMN_KIND + " IN (kind)")
    List<Favorite> showAllFieldInKindColumn();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " +
            COLUMN_KIND + " LIKE :kind")
    Cursor showFilterKindColumn(String kind);

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " +
            COLUMN_KIND + " LIKE :kind AND " + COLUMN_ID + " LIKE :Id")
    Cursor showFavoriteFilterByKindId(String kind, long Id);

    @Query("DELETE FROM " + TABLE_NAME + " WHERE " +
            COLUMN_ID + " = :Id")
    int deleteFavoriteFilterId(Long Id);

    @Insert
    long insertToProvider(Favorite favorites);

    @Update
    int updateToProvider(Favorite favorite);
}