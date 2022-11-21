package com.example.waterplant.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.waterplant.model.UserDetailsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserDetailsDBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "garden_db";
    private static final String USER_DETAILS_TABLE = "user_details";

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String EMAIL_ID = "email";
    private static final String PASSWORD = "password";

    public UserDetailsDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE " + USER_DETAILS_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERNAME + " TEXT, "
                + EMAIL_ID + " TEXT, "
                + PASSWORD + " TEXT)";

        database.execSQL(query);
    }

    public void createUserDetails(String username, String emailId, String password, Consumer<Boolean> callback) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(EMAIL_ID, emailId);
        values.put(PASSWORD, password);

        database.insert(USER_DETAILS_TABLE, null, values);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            callback.accept(true);
        }
    }

    public List<UserDetailsModel> fetchUserDetails() {
        List<UserDetailsModel> userDetails = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + USER_DETAILS_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                userDetails.add(new UserDetailsModel(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return userDetails;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS_TABLE);
        onCreate(sqLiteDatabase);
    }
}
