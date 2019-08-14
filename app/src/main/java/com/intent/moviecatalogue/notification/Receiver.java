package com.intent.moviecatalogue.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.intent.moviecatalogue.BuildConfig;
import com.intent.moviecatalogue.activity.SettingsActivity;
import com.intent.moviecatalogue.api.GetClient;
import com.intent.moviecatalogue.helper.Notification;
import com.intent.moviecatalogue.interfaces.ApiServiceInterface;
import com.intent.moviecatalogue.model.FeedBackMovies;
import com.intent.moviecatalogue.model.Movie;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.intent.moviecatalogue.Public.EXTRA_MESSAGE;
import static com.intent.moviecatalogue.Public.EXTRA_TITLE;
import static com.intent.moviecatalogue.Public.EXTRA_TYPE;
import static com.intent.moviecatalogue.Public.ID_DAILY_REMINDER;
import static com.intent.moviecatalogue.Public.ID_RELEASE_REMINDER;
import static com.intent.moviecatalogue.Public.LONG_DAILY_REMINDER;
import static com.intent.moviecatalogue.Public.LONG_RELEASE_REMINDER;
import static com.intent.moviecatalogue.Public.NORMAL_DAILY_REMINDER;

public class Receiver extends BroadcastReceiver {

    Calendar dateTime;
    Intent intent;

    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int notificationId;
        int notifCount = 0;
        String longKey, title;
        if (type.equals(NORMAL_DAILY_REMINDER)) {
            notificationId = ID_DAILY_REMINDER;
            title = intent.getStringExtra(EXTRA_TITLE);
            longKey = LONG_DAILY_REMINDER;
            Notification.loadNotification(context, title, message, longKey, notificationId, notifCount, null);
        } else {
            notificationId = ID_RELEASE_REMINDER;
            longKey = LONG_RELEASE_REMINDER;
            getNewRelease(context, message, longKey, notificationId);
        }
    }

    public void bannedReminder(Context context, String type) {
        Intent intent = new Intent(context, SettingsActivity.class);
        int requestCode = type.equalsIgnoreCase(NORMAL_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public void getNewRelease(final Context context, final String message, final String longKey, final int notificationId) {
        ApiServiceInterface apiClient = GetClient.getClient().create(ApiServiceInterface.class);
        Call<FeedBackMovies> call = apiClient.getNew(BuildConfig.PRINCE_DEVELOPER_DB_KEY);
        call.enqueue(new Callback<FeedBackMovies>() {
            @Override
            public void onResponse(@NotNull Call<FeedBackMovies> call, @NotNull Response<FeedBackMovies> response) {
                List<Movie> movies;
                List<Movie> listMovie = new ArrayList<>();
                int notifCount = 0;
                String releaseDate, messageNotif, title;

                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                if (response.body() != null) {
                    movies = response.body().getCatalogues();
                    for (Movie catalogue : movies) {
                        releaseDate = catalogue.getRelease();
                        if (releaseDate.equals(currentDate)) {
                            title = catalogue.getTitle();
                            messageNotif = title + " " + message;
                            listMovie.add(catalogue);
                            Notification.loadNotification(context, title, messageNotif, longKey, notificationId, notifCount, listMovie);
                            notifCount++;
                        }
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<FeedBackMovies> call, @NotNull Throwable t) {

            }
        });

    }

    public void serviceDailyReminder(Context context, String type, String time, String message) {
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(time, TIME_FORMAT)) return;
        setIntent(context, type, message);
        setTime(time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        serviceAlarmManager(context, pendingIntent);
    }

    private void serviceAlarmManager(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, dateTime.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void setIntent(Context context, String type, String message) {
        intent = new Intent(context, Receiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
    }

    public void serviceReleaseReminder(Context context, String type, String time, String message) {
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(time, TIME_FORMAT))
            return;
        setIntent(context, type, message);
        setTime(time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, 0);
        serviceAlarmManager(context, pendingIntent);
    }

    private void setTime(String time) {
        String[] timeArray = time.split(":");
        dateTime = Calendar.getInstance();
        dateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        dateTime.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        dateTime.set(Calendar.SECOND, 0);
    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }


}
