package com.intent.moviecatalogue.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.activity.DetailActivity;
import com.intent.moviecatalogue.activity.MainActivity;
import com.intent.moviecatalogue.model.Movie;

import java.util.List;

import static com.intent.moviecatalogue.Public.CHANNEL_ID;
import static com.intent.moviecatalogue.Public.CHANNEL_NAME;
import static com.intent.moviecatalogue.Public.KEY_CATALOGUE;
import static com.intent.moviecatalogue.Public.LONG_RELEASE_REMINDER;

public class Notification {

    public static void loadNotification(Context context, String title, String message, String longKey, int notificationId, int notifCount, List<Movie> listMovie) {
        Intent intent;
        if (longKey.equals(LONG_RELEASE_REMINDER)) {
            intent = new Intent(context, DetailActivity.class);
            intent.putExtra(KEY_CATALOGUE, listMovie.get(notifCount));
        } else {
            intent = new Intent(context, MainActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movies)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setGroup(longKey)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        android.app.Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            String NotificationID = String.valueOf(notificationId) + notifCount;
            int NotificationId = Integer.parseInt(NotificationID);

            notificationManagerCompat.notify(NotificationId, notification);
        }
    }
}
