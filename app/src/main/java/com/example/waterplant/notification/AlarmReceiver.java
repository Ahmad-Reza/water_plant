package com.example.waterplant.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.waterplant.R;
import com.example.waterplant.activity.HomeActivity;
import com.example.waterplant.fragment.SchedulePlantModel;
import com.example.waterplant.utilities.ImageUtility;

public class AlarmReceiver extends BroadcastReceiver {
    public static int NOTIFICATION_ID = 1;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String SCHEDULE_PLANT = "SCHEDULE_PLANT";

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);

        SchedulePlantModel schedulePlant = intent.getAction() != null &&
                intent.getAction().equals("action.schedule.plant") ?
                (SchedulePlantModel) intent.getParcelableExtra(SCHEDULE_PLANT) :
                null;

        Intent contentIntent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                1,
                contentIntent,
                PendingIntent.FLAG_MUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.logo_bg);

        if (schedulePlant != null) {
            ImageUtility imageUtility = new ImageUtility(context);
            Bitmap iconBitmap = imageUtility.getBitmapFromUri(schedulePlant.getPlantModel().getImage());

            builder.setContentTitle(schedulePlant.getPlantModel().getName());
            builder.setContentText(schedulePlant.getMessage());
            builder.setLargeIcon(iconBitmap);
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(iconBitmap).bigLargeIcon(null));

        } else {
            builder.setContentTitle("Water your plant");
            builder.setContentText("Hey beautiful people, don't forgot to water your plant");
            builder.setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Hey beautiful people, don't forgot to water your plant"));
        }

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

        // Remove this line if you don't want to reschedule the reminder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ReminderManager.startReminder(context, schedulePlant);
        }
    }
}
