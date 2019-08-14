package com.intent.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.intent.moviecatalogue.Public;
import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.database.RoomDatabases;
import com.intent.moviecatalogue.database.Favorite;

import java.util.ArrayList;
import java.util.List;

import static com.intent.moviecatalogue.Public.DATABASE_NAME;


public class CollectionRemoteViews implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private List<Favorite> dataFavorite = new ArrayList<>();
    private List<Bitmap> getItem = new ArrayList<>();

    CollectionRemoteViews(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        showData();
    }

    private void showData() {
        getItem.clear();
        RoomDatabases roomDatabases = Room.databaseBuilder(context, RoomDatabases.class, DATABASE_NAME).allowMainThreadQueries().build();
        dataFavorite = roomDatabases.favoriteInterface().showAllFieldInKindColumn();
        for (Favorite favorite : dataFavorite) {
            try {
                getItem.add(Glide.with(context.getApplicationContext())
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/original/" + favorite.getPoster())
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        roomDatabases.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return getItem.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        if (getItem.size() != 0) {
            rv.setImageViewBitmap(R.id.imageView, getItem.get(position));
            Bundle bundle = new Bundle();
            bundle.putLong(Public.EXTRA_ID, dataFavorite.get(position).getId());
            bundle.putString(Public.EXTRA_TITLE, dataFavorite.get(position).getTitle());
            bundle.putString(Public.EXTRA_RELEASE, dataFavorite.get(position).getRelease());
            bundle.putString(Public.EXTRA_OVERVIEW, dataFavorite.get(position).getOverview());
            bundle.putString(Public.EXTRA_POSTER, dataFavorite.get(position).getPoster());
            bundle.putString(Public.EXTRA_CATALOGUE, dataFavorite.get(position).getKind());
            Intent intent = new Intent();
            intent.putExtras(bundle);
            rv.setOnClickFillInIntent(R.id.imageView, intent);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
