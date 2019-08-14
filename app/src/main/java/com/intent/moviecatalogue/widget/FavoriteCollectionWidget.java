package com.intent.moviecatalogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.activity.DetailActivity;
import com.intent.moviecatalogue.database.Favorite;

import java.util.ArrayList;

import static com.intent.moviecatalogue.Public.EXTRA_CATALOGUE;
import static com.intent.moviecatalogue.Public.EXTRA_ID;
import static com.intent.moviecatalogue.Public.EXTRA_OVERVIEW;
import static com.intent.moviecatalogue.Public.EXTRA_POSTER;
import static com.intent.moviecatalogue.Public.EXTRA_RELEASE;
import static com.intent.moviecatalogue.Public.EXTRA_TITLE;
import static com.intent.moviecatalogue.Public.GET_WIDGET_UPDATE;
import static com.intent.moviecatalogue.Public.KEY_CATALOGUE_LIST;
import static com.intent.moviecatalogue.Public.TOAST_ACTION;

public class FavoriteCollectionWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, CollectionWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteCollectionWidget.class);
        toastIntent.setAction(TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                ArrayList<Favorite> getFavorite = new ArrayList<>();
                getFavorite.clear();

                Favorite favorite = new Favorite();
                favorite.setId(intent.getLongExtra(EXTRA_ID, 0));
                favorite.setTitle(intent.getStringExtra(EXTRA_TITLE));
                favorite.setRelease(intent.getStringExtra(EXTRA_RELEASE));
                favorite.setOverview(intent.getStringExtra(EXTRA_OVERVIEW));
                favorite.setPoster(intent.getStringExtra(EXTRA_POSTER));
                favorite.setKind(intent.getStringExtra(EXTRA_CATALOGUE));
                getFavorite.add(favorite);

                Intent intentFlag = new Intent(context, DetailActivity.class);
                intentFlag.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentFlag.putParcelableArrayListExtra(KEY_CATALOGUE_LIST, getFavorite);
                context.startActivity(intentFlag);
            }
            if (intent.getAction().equals(GET_WIDGET_UPDATE)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, FavoriteCollectionWidget.class));

                appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
            }
        }
    }

}

