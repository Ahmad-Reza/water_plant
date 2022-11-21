package com.example.waterplant.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;

import com.example.waterplant.model.PlantModel;
import com.example.waterplant.utilities.ImageUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlantDBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "plant_db";
    private static final String TABLE_PLANT = "plant";

    private static final String ID = "id";
    private static final String PLANT_IMAGE = "image";
    private static final String PLANT_NAME = "name";
    private static final String PLANT_CATEGORY = "category";

    private final Context context;

    public PlantDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_PLANT + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLANT_IMAGE + " BLOB, "
                + PLANT_NAME + " TEXT, "
                + PLANT_CATEGORY + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    public void createPlantData(byte[] plantByte, String plantName, String plantCategory) {
        ContentValues values = new ContentValues();
        values.put(PLANT_IMAGE, plantByte);
        values.put(PLANT_NAME, plantName);
        values.put(PLANT_CATEGORY, plantCategory);

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(TABLE_PLANT, null, values);
        database.close();
    }

    public List<PlantModel> fetchPlanData(Consumer<Boolean> callback) {
        ImageUtility imageUtility = new ImageUtility(context);
        List<PlantModel> plantModels = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_PLANT, null);
        if (cursor.moveToFirst()) {
            do {
                Uri uri = imageUtility.getUriFromByte(cursor.getBlob(1));

                plantModels.add(new PlantModel(uri, cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }

        cursor.close();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            callback.accept(true);
        }

        return plantModels;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        if (newVersion > oldVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANT);
            onCreate(database);
        }
    }
}
