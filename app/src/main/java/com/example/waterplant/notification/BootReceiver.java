package com.example.waterplant.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.waterplant.dataBase.PlantDBHandler;
import com.example.waterplant.dataBase.ScheduleDBHandler;
import com.example.waterplant.fragment.SchedulePlantModel;
import com.example.waterplant.model.PlantModel;

import java.util.List;
import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {
    /*
     * restart reminders alarms when user's device reboots
     * */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PlantDBHandler plantDBHandler = new PlantDBHandler(context);
            List<PlantModel> plants = plantDBHandler.fetchPlanData(isSuccessful -> { });

            ScheduleDBHandler scheduleDBHandler = new ScheduleDBHandler(context);
            List<SchedulePlantModel> schedulePlants = scheduleDBHandler.fetchSchedulePlant(plants);

            for (SchedulePlantModel schedulePlant : schedulePlants) {
                ReminderManager.startReminder(context, schedulePlant);
            }
        }
    }
}
