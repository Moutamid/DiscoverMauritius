package com.moutamid.sqlapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "beac_models";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT1 = "text1";
    public static final String COLUMN_TITLE1 = "title1";
    public static final String COLUMN_MAIN_IMAGE1 = "image1";
    public static final String COLUMN_TEXT2 = "text2";
    public static final String COLUMN_TITLE2 = "title2";
    public static final String COLUMN_TEXT3 = "text3";
    public static final String COLUMN_MAIN_IMAGE2 = "image2";
    public static final String COLUMN_TEXT4 = "text4";
    public static final String COLUMN_TITLE3 = "title3";
    public static final String COLUMN_TEXT5 = "text5";
    public static final String COLUMN_TITLE4 = "title4";
    public static final String COLUMN_TEXT6 = "text6";
    public static final String COLUMN_TITLE5 = "title5";
    public static final String COLUMN_TEXT7 = "text7";
    public static final String COLUMN_TITLE6 = "title6";
    public static final String COLUMN_TITLE7 = "title7";
    public static final String COLUMN_TEXT8 = "text8";
    public static final String COLUMN_MAIN_IMAGE3 = "image3";
    public static final String COLUMN_TEXT9 = "text9";
    public static final String COLUMN_TITLE8 = "title8";
    public static final String COLUMN_TEXT10 = "text10";
    public static final String COLUMN_MAIN_IMAGE4 = "image4";
    public static final String COLUMN_TEXT11 = "text11";
    public static final String COLUMN_MAIN_IMAGE5 = "image5";
    public static final String COLUMN_TEXT12 = "text12";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    private static final String DATABASE_NAME = "beac_model.db";
    private static final int DATABASE_VERSION = 1;
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ID_COPY = "id_copy";
    private static final String COLUMN_MAIN_IMAGE = "main_image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_COPY + " INTEGER," +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_MAIN_IMAGE + " TEXT, " +
                COLUMN_TEXT1 + " TEXT, " +
                COLUMN_TITLE1 + " TEXT, " +
                COLUMN_MAIN_IMAGE1 + " TEXT, " +
                COLUMN_TEXT2 + " TEXT, " +
                COLUMN_TITLE2 + " TEXT, " +
                COLUMN_TEXT3 + " TEXT, " +
                COLUMN_MAIN_IMAGE2 + " TEXT, " +
                COLUMN_TEXT4 + " TEXT, " +
                COLUMN_TITLE3 + " TEXT, " +
                COLUMN_TEXT5 + " TEXT, " +
                COLUMN_TITLE4 + " TEXT, " +
                COLUMN_TEXT6 + " TEXT, " +
                COLUMN_TITLE5 + " TEXT, " +
                COLUMN_TEXT7 + " TEXT, " +
                COLUMN_TITLE6 + " TEXT, " +
                COLUMN_TITLE7 + " TEXT, " +
                COLUMN_TEXT8 + " TEXT, " +
                COLUMN_MAIN_IMAGE3 + " TEXT, " +
                COLUMN_TEXT9 + " TEXT, " +
                COLUMN_TITLE8 + " TEXT, " +
                COLUMN_TEXT10 + " TEXT, " +
                COLUMN_MAIN_IMAGE4 + " TEXT, " +
                COLUMN_TEXT11 + " TEXT, " +
                COLUMN_MAIN_IMAGE5 + " TEXT, " +
                COLUMN_TEXT12 + " TEXT," +
                COLUMN_LAT + " DOUBLE," +
                COLUMN_LNG + " DOUBLE " +
                ")";
        db.execSQL(createTableQuery);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public long insertBeacModel(BeacModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, model.title);
        values.put(COLUMN_MAIN_IMAGE, model.main_image);
        values.put(COLUMN_ID_COPY, model.id_copy);
        values.put(COLUMN_TEXT1, model.text1);
        values.put(COLUMN_TITLE1, model.title1);
        values.put(COLUMN_MAIN_IMAGE1, model.image1);
        values.put(COLUMN_TEXT2, model.text2);
        values.put(COLUMN_TITLE2, model.title2);
        values.put(COLUMN_TEXT3, model.text3);
        values.put(COLUMN_MAIN_IMAGE2, model.image2);
        values.put(COLUMN_TEXT4, model.text4);
        values.put(COLUMN_TITLE3, model.title3);
        values.put(COLUMN_TEXT5, model.text5);
        values.put(COLUMN_TITLE4, model.title4);
        values.put(COLUMN_TEXT6, model.text6);
        values.put(COLUMN_TITLE5, model.title5);
        values.put(COLUMN_TEXT7, model.text7);
        values.put(COLUMN_TITLE6, model.title6);
        values.put(COLUMN_TITLE7, model.title7);
        values.put(COLUMN_TEXT8, model.text8);
        values.put(COLUMN_MAIN_IMAGE3, model.image3);
        values.put(COLUMN_TEXT9, model.text9);
        values.put(COLUMN_TITLE8, model.title8);
        values.put(COLUMN_TEXT10, model.text10);
        values.put(COLUMN_MAIN_IMAGE4, model.image4);
        values.put(COLUMN_TEXT11, model.text11);
        values.put(COLUMN_MAIN_IMAGE5, model.image5);
        values.put(COLUMN_TEXT12, model.text12);
        values.put(COLUMN_LAT, model.lat);
        values.put(COLUMN_LNG, model.lng);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<BeacModel> getAllBeacModels() {
        List<BeacModel> beacModels = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID_COPY + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                BeacModel model = new BeacModel();
                model.title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                model.main_image = cursor.getString(cursor.getColumnIndex(COLUMN_MAIN_IMAGE));
                model.id_copy = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_COPY));
                model.text1 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT1));
                model.title1 = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE1));
                model.image1 = cursor.getString(cursor.getColumnIndex(COLUMN_MAIN_IMAGE1));
                model.text2 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT2));
                model.title2 = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE2));
                model.text3 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT3));
                model.image2 = cursor.getString(cursor.getColumnIndex(COLUMN_MAIN_IMAGE2));
                model.text4 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT4));
                model.title3 = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE3));
                model.text5 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT5));
                model.title4 = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE4));
                model.text6 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT6));
                model.title5 = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE5));
                model.text7 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT7));
                model.title6 = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE6));
                model.title7 = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE7));
                model.text8 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT8));
                model.image3 = cursor.getString(cursor.getColumnIndex(COLUMN_MAIN_IMAGE3));
                model.text9 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT9));
                model.title8 = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE8));
                model.text10 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT10));
                model.image4 = cursor.getString(cursor.getColumnIndex(COLUMN_MAIN_IMAGE4));
                model.text11 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT11));
                model.image5 = cursor.getString(cursor.getColumnIndex(COLUMN_MAIN_IMAGE5));
                model.text12 = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT12));
                model.lat = cursor.getDouble(cursor.getColumnIndex(COLUMN_LAT));
                model.lng = cursor.getDouble(cursor.getColumnIndex(COLUMN_LNG));
                beacModels.add(model);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return beacModels;
    }
    public int updateBeacModel(BeacModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, model.title);
        values.put(COLUMN_MAIN_IMAGE, model.main_image);
        values.put(COLUMN_ID_COPY, model.id_copy);
        values.put(COLUMN_TEXT1, model.text1);
        values.put(COLUMN_TITLE1, model.title1);
        values.put(COLUMN_MAIN_IMAGE1, model.image1);
        values.put(COLUMN_TEXT2, model.text2);
        values.put(COLUMN_TITLE2, model.title2);
        values.put(COLUMN_TEXT3, model.text3);
        values.put(COLUMN_MAIN_IMAGE2, model.image2);
        values.put(COLUMN_TEXT4, model.text4);
        values.put(COLUMN_TITLE3, model.title3);
        values.put(COLUMN_TEXT5, model.text5);
        values.put(COLUMN_TITLE4, model.title4);
        values.put(COLUMN_TEXT6, model.text6);
        values.put(COLUMN_TITLE5, model.title5);
        values.put(COLUMN_TEXT7, model.text7);
        values.put(COLUMN_TITLE6, model.title6);
        values.put(COLUMN_TITLE7, model.title7);
        values.put(COLUMN_TEXT8, model.text8);
        values.put(COLUMN_MAIN_IMAGE3, model.image3);
        values.put(COLUMN_TEXT9, model.text9);
        values.put(COLUMN_TITLE8, model.title8);
        values.put(COLUMN_TEXT10, model.text10);
        values.put(COLUMN_MAIN_IMAGE4, model.image4);
        values.put(COLUMN_TEXT11, model.text11);
        values.put(COLUMN_MAIN_IMAGE5, model.image5);
        values.put(COLUMN_TEXT12, model.text12);
        values.put(COLUMN_LAT, model.lat);
        values.put(COLUMN_LNG, model.lng);
        Log.d("value", values + "  data");
        return db.update(TABLE_NAME, values, COLUMN_TITLE + " = ?",
                new String[]{String.valueOf(model.title)});
    }
    public void deleteAllBeacModels() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
