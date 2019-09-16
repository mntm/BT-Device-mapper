// Source : https://www.youtube.com/watch?v=aQAIMY-HzL8

package com.bluetooth.googlemap.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "favorite_devices";
    private static final String COL_0 = "ID";
    private static final String COL_1 = "Class";
    private static final String COL_2 = "Location";
    private static final String COL_3 = "MAC";
    private static final String COL_4 = "Signal";
    private static final String COL_5 = "Name";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " +
                TABLE_NAME +
                " (" + COL_0 +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_1 +
                " TEXT, " +
                COL_2 +
                " TEXT, " +
                COL_3 +
                " TEXT, " +
                COL_4 +
                " TEXT, " +
                COL_5 +
                " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(String class_, String location, String mac, String signal, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, class_);
        contentValues.put(COL_2, location);
        contentValues.put(COL_3, mac);
        contentValues.put(COL_4, signal);
        contentValues.put(COL_5, name);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1)
            return false;
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteData(String mac){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL_3 + " = '" + mac + "'";
        db.execSQL(query);
    }

    public void clearAllDataFromDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }
}
