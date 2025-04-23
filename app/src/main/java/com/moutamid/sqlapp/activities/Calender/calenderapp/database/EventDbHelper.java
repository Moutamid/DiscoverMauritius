package com.moutamid.sqlapp.activities.Calender.calenderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class EventDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Events.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EventContract.EventEntry.TABLE_NAME + " (" +
                    EventContract.EventEntry._ID + " INTEGER PRIMARY KEY," +
                    EventContract.EventEntry.COLUMN_TITLE + " TEXT," +
                    EventContract.EventEntry.COLUMN_DATE + " TEXT," +
                    EventContract.EventEntry.COLUMN_TIME + " TEXT," +
                    EventContract.EventEntry.COLUMN_EXACT_TIME + " TEXT," +
                    EventContract.EventEntry.COLUMN_DESCRIPTION + " TEXT," +
                    EventContract.EventEntry.COLUMN_COMPLETE + " INTEGER," +
                    EventContract.EventEntry.COLUMN_CHECKED + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EventContract.EventEntry.TABLE_NAME;

    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long insertEvent(String title, String date, String time, String exact_time, String description, boolean checked, int complete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_TITLE, title);
        values.put(EventContract.EventEntry.COLUMN_DATE, date);
        values.put(EventContract.EventEntry.COLUMN_TIME, time);
        values.put(EventContract.EventEntry.COLUMN_EXACT_TIME, exact_time);
        values.put(EventContract.EventEntry.COLUMN_DESCRIPTION, description);
        values.put(EventContract.EventEntry.COLUMN_CHECKED, checked ? 1 : 0);
        values.put(EventContract.EventEntry.COLUMN_COMPLETE, complete);
        long newRowId = db.insert(EventContract.EventEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public int updateEvent(long eventId, String title, String date, String exact_time, String time, String description, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_TITLE, title);
        values.put(EventContract.EventEntry.COLUMN_DATE, date);
        values.put(EventContract.EventEntry.COLUMN_TIME, time);
        values.put(EventContract.EventEntry.COLUMN_EXACT_TIME, exact_time);

        values.put(EventContract.EventEntry.COLUMN_DESCRIPTION, description);
        values.put(EventContract.EventEntry.COLUMN_CHECKED, checked ? 1 : 0);
        values.put(EventContract.EventEntry.COLUMN_COMPLETE, 0);
        String selection = EventContract.EventEntry._ID + "=?";
        String[] selectionArgs = { String.valueOf(eventId) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
        return count;
    }
    public int completeEvent(long eventId, String title, String date, String exact_time, String time, String description, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_TITLE, title);
        values.put(EventContract.EventEntry.COLUMN_DATE, date);
        values.put(EventContract.EventEntry.COLUMN_TIME, time);
        values.put(EventContract.EventEntry.COLUMN_EXACT_TIME, exact_time);

        values.put(EventContract.EventEntry.COLUMN_DESCRIPTION, description);
        values.put(EventContract.EventEntry.COLUMN_CHECKED, 0);
        values.put(EventContract.EventEntry.COLUMN_COMPLETE, 1);
        String selection = EventContract.EventEntry._ID + "=?";
        String[] selectionArgs = { String.valueOf(eventId) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
        return count;
    }

    public int deleteEvent(long eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = EventContract.EventEntry._ID + "=?";
        String[] selectionArgs = { String.valueOf(eventId) };
        int deletedRows = db.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return deletedRows;
    }

    public List<Event> getCheckedEventsByDate(String date) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_TITLE,
                EventContract.EventEntry.COLUMN_DATE,
                EventContract.EventEntry.COLUMN_TIME,
                EventContract.EventEntry.COLUMN_EXACT_TIME,
                EventContract.EventEntry.COLUMN_DESCRIPTION,
                EventContract.EventEntry.COLUMN_CHECKED,
                EventContract.EventEntry.COLUMN_COMPLETE
        };

        String selection = EventContract.EventEntry.COLUMN_DATE + "=? AND " +
                EventContract.EventEntry.COLUMN_CHECKED + "=?";
        String[] selectionArgs = { date, "0" };
        Cursor cursor = db.query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry._ID)));
                event.setTitle(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TITLE)));
                event.setDate(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DATE)));
                event.setTime(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TIME)));
                event.setDescription(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DESCRIPTION)));
                event.setChecked(cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_CHECKED)) == 0);
                event.setComplete(cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_COMPLETE)));
                event.setExact_time(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EXACT_TIME)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }
    public List<Event> getEventsByDate(String date) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_TITLE,
                EventContract.EventEntry.COLUMN_DATE,
                EventContract.EventEntry.COLUMN_TIME,
                EventContract.EventEntry.COLUMN_EXACT_TIME,

                EventContract.EventEntry.COLUMN_DESCRIPTION,
                EventContract.EventEntry.COLUMN_CHECKED,
                EventContract.EventEntry.COLUMN_COMPLETE
        };

        String selection = EventContract.EventEntry.COLUMN_DATE + "=? AND " +
                EventContract.EventEntry.COLUMN_CHECKED + "=?";
        String[] selectionArgs = { date, "1" }; // "1" represents checked events

        Cursor cursor = db.query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry._ID)));
                event.setTitle(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TITLE)));
                event.setDate(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DATE)));
                event.setTime(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TIME)));
                event.setExact_time(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EXACT_TIME)));
                event.setDescription(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DESCRIPTION)));
                event.setChecked(cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_CHECKED)) == 1);
                event.setComplete(cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_COMPLETE)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }
    public List<Event> getEventsAll(String date) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_TITLE,
                EventContract.EventEntry.COLUMN_DATE,
                EventContract.EventEntry.COLUMN_TIME,
                EventContract.EventEntry.COLUMN_EXACT_TIME,

                EventContract.EventEntry.COLUMN_DESCRIPTION,
                EventContract.EventEntry.COLUMN_CHECKED,
                EventContract.EventEntry.COLUMN_COMPLETE
        };

        String selection = EventContract.EventEntry.COLUMN_DATE + "=?";
        String[] selectionArgs = { date }; // "1" represents checked events

        Cursor cursor = db.query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry._ID)));
                event.setTitle(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TITLE)));
                event.setDate(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DATE)));
                event.setTime(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TIME)));
                event.setExact_time(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EXACT_TIME)));
                event.setDescription(cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DESCRIPTION)));
                event.setChecked(cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_CHECKED)) == 0);
                event.setComplete(cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_COMPLETE)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }
}
