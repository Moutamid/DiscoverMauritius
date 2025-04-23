package com.moutamid.sqlapp.activities.Calender.calenderapp.database;

import android.provider.BaseColumns;

public class EventContract {
    private EventContract() {} // Prevent instantiation

    public static class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_EXACT_TIME = "exact_time";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CHECKED = "checked";
        public static final String COLUMN_COMPLETE = "complete";
    }
}
