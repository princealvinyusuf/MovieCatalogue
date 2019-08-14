package com.intent.moviecatalogue.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.intent.moviecatalogue.interfaces.FavoriteInterface;

import static com.intent.moviecatalogue.Public.DATABASE_NAME;


@Database(entities = {Favorite.class},version = 1,exportSchema = false)
public abstract class RoomDatabases extends RoomDatabase {
    public abstract FavoriteInterface favoriteInterface();

    private static RoomDatabases sInstance;

    public static synchronized RoomDatabases getInstance(Context context){
        if (sInstance==null){
            sInstance=Room.databaseBuilder(context.getApplicationContext(),RoomDatabases.class,DATABASE_NAME).allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }
}
