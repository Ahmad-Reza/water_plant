package com.example.waterplant.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "garden_db";
    private static final String TABLE_NAME = "plant";

    private static final String ID_COLUMN = "id";
    private static final String PLANT_COLUMN = "plant";
    private static final String NAME_COLUMN = "name";
    private static final String CATEGORY_COLUMN = "category";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLANT_COLUMN + " BYTE,"
                + NAME_COLUMN + " TEXT,"
                + CATEGORY_COLUMN + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    public void addNewPlant(Byte plantBitmap, String plantName, String plantCategory) {
        ContentValues values = new ContentValues();
        values.put(PLANT_COLUMN, plantBitmap);
        values.put(NAME_COLUMN, plantName);
        values.put(CATEGORY_COLUMN, plantCategory);

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(TABLE_NAME, null, values);

        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // this method is called to check if the table exists already.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
