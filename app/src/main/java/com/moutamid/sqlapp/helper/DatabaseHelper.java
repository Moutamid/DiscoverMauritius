package com.moutamid.sqlapp.helper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
// DatabaseHelper.java

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "mytable";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_IMAGE + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade logic here
    }

    public void insertData(String name, String address, String details, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_DETAILS, details);
        values.put(COLUMN_IMAGE, image);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean checkDataExists(String name, String address, String details, String image) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (cursor.moveToNext()) {
            String existingName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String existingAddress = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
            String existingDetails = cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS));
            String existingImage = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));

            if (name.equals(existingName) &&
                    address.equals(existingAddress) &&
                    details.equals(existingDetails) &&
                    image == existingImage) {
                cursor.close();
                db.close();
                return true; // Data already exists
            }
        }

        cursor.close();
        db.close();
        return false; // Data does not exist
    }
    public void deleteData(String name, String address, String details, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,
                COLUMN_NAME + " = ? AND " +
                        COLUMN_ADDRESS + " = ? AND " +
                        COLUMN_DETAILS + " = ? AND " +
                        COLUMN_IMAGE + " = ?",
                new String[]{name, address, details, String.valueOf(image)});
        db.close();
    }
    public ArrayList<DataModel> getAllData() {
        ArrayList<DataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
            String details = cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS));
            String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));

            DataModel data = new DataModel(name, address, details, image);
            dataList.add(data);
        }

        cursor.close();
        db.close();
        return dataList;
    }
}
