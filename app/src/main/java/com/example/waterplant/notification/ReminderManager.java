package com.example.waterplant.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.waterplant.fragment.SchedulePlantModel;

import java.time.LocalDateTime;
import java.util.Calendar;

public class ReminderManager {
    private static final int REMINDER_NOTIFICATION_REQUEST_CODE = 123;

    @RequiresApi(api = Build.VERSION_CODES.S)
    public static void startReminder(Context context, SchedulePlantModel schedulePlant) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.SCHEDULE_PLANT, schedulePlant);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                REMINDER_NOTIFICATION_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_MUTABLE);

        LocalDateTime reminderTime = schedulePlant != null ? schedulePlant.getLocalDateTime() : LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminderTime.getHour());
        calendar.set(Calendar.MINUTE, reminderTime.getMinute());

        Calendar temp = Calendar.getInstance();
        temp.add(Calendar.MINUTE, 1);
        // If the trigger time you specify is in the past, the alarm triggers immediately.
        // if soo just add one day to required calendar
        // Note: i'm also adding one min cuz if the user clicked on the notification as soon as
        // he receive it it will reschedule the alarm to fire another notification immediately
        if (temp.getTimeInMillis() - calendar.getTimeInMillis() > 0) {
            calendar.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);
    }

    public static void stopReminder(Context context, int reminderId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderId, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
