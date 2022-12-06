package com.example.waterplant.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.waterplant.fragment.ActionListener;
import com.example.waterplant.fragment.SchedulePlantModel;
import com.example.waterplant.model.PlantModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScheduleDBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "schedule_db";
    private static final String SCHEDULE_PLANT_TABLE = "schedule_plant";

    private static final String ID = "id";
    private static final String SELECTED_PLANT_POSITION = "position";
    private static final String MESSAGE = "message";
    private static final String SCHEDULE_TIME = "schedule_time";

    public ScheduleDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE " + SCHEDULE_PLANT_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SELECTED_PLANT_POSITION + " TEXT, "
                + MESSAGE + " TEXT, "
                + SCHEDULE_TIME + " TEXT)";

        database.execSQL(query);
    }

    public void updateSchedulePlant(int plantPosition, ActionListener.Action action, SchedulePlantModel schedulePlantModel, Consumer<Boolean> callback) {
        ContentValues values = new ContentValues();
        values.put(SELECTED_PLANT_POSITION, String.valueOf(plantPosition));
        values.put(MESSAGE, schedulePlantModel.getMessage());
        values.put(SCHEDULE_TIME, schedulePlantModel.getLocalDateTime().toString());

        SQLiteDatabase database = this.getWritableDatabase();
        if (ActionListener.Action.SAVE.equals(action)) {
            database.insert(SCHEDULE_PLANT_TABLE, null, values);

        } else if (ActionListener.Action.UPDATE.equals(action)) {
            database.update(SCHEDULE_PLANT_TABLE, values, "message=?", new String[]{schedulePlantModel.getMessage()});
            database.update(SCHEDULE_PLANT_TABLE, values, "schedule_time=?", new String[]{schedulePlantModel.getLocalDateTime().toString()});
        }

        database.close();
        callback.accept(true);
    }

    public List<SchedulePlantModel> fetchSchedulePlant(List<PlantModel> plantModels) {
        List<SchedulePlantModel> schedulePlants = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SCHEDULE_PLANT_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                schedulePlants.add(new SchedulePlantModel(
                        plantModels.get(Integer.parseInt(cursor.getString(1))),
                        cursor.getString(2),
                        LocalDateTime.parse(cursor.getString(3))));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return schedulePlants;
    }

    public void deleteSchedulePlant(SchedulePlantModel schedulePlantModel) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(SCHEDULE_PLANT_TABLE, "id=?", new String[]{schedulePlantModel.getId()});
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_PLANT_TABLE);
        onCreate(sqLiteDatabase);
    }
}
