package com.moutamid.sqlapp.activities.Organizer.helper;
import android.provider.BaseColumns;

public class DatabaseContract {
    private DatabaseContract() {
    }

    // Edited text table
    public static class EditedTextEntry implements BaseColumns {
        public static final String TABLE_NAME = "edited_text";
        public static final String COLUMN_NAME_DOCUMENT_TYPE = "document_type";
        public static final String COLUMN_NAME_DOCUMENT_TITLE = "document_title";
        public static final String COLUMN_NAME_DOCUMENT_NUMBER = "document_number";
        public static final String COLUMN_NAME_COUNTRY_DOCUMENT = "country_document";
        public static final String COLUMN_NAME_ISSUED_BY = "issued_by";
        public static final String COLUMN_NAME_ISSUED_DATE = "issued_date";
        public static final String COLUMN_NAME_EXPIRE_DATE = "expire_date";
        public static final String COLUMN_NAME_NOTE = "note";
    }

    // Image table
    public static class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_IMAGE_NAME = "image_name";
        public static final String COLUMN_NAME_IMAGE_URI = "image_icon";
        public static final String COLUMN_NAME_IMAGE_SIZE = "image_size";
        public static final String COLUMN_NAME_EDITED_TEXT_ID = "edited_text_id";
    }

    // File table
    public static class FileEntry implements BaseColumns {
        public static final String TABLE_NAME = "files";
        public static final String COLUMN_NAME_FILE_NAME = "file_name";
        public static final String COLUMN_NAME_FILE_SIZE = "file_size";
        public static final String COLUMN_NAME_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_FILE_DATA = "file_image";
        public static final String COLUMN_NAME_EDITED_TEXT_ID = "edited_text_id";
    }

    // SQL statements for table creation
    public static final String SQL_CREATE_EDITED_TEXT_TABLE =
            "CREATE TABLE " + EditedTextEntry.TABLE_NAME + " (" +
                    EditedTextEntry._ID + " INTEGER PRIMARY KEY," +
                    EditedTextEntry.COLUMN_NAME_DOCUMENT_TYPE + " TEXT," +
                    EditedTextEntry.COLUMN_NAME_DOCUMENT_TITLE + " TEXT," +
                    EditedTextEntry.COLUMN_NAME_DOCUMENT_NUMBER + " TEXT," +
                    EditedTextEntry.COLUMN_NAME_COUNTRY_DOCUMENT + " TEXT," +
                    EditedTextEntry.COLUMN_NAME_ISSUED_BY + " TEXT," +
                    EditedTextEntry.COLUMN_NAME_ISSUED_DATE + " TEXT," +
                    EditedTextEntry.COLUMN_NAME_EXPIRE_DATE + " TEXT," +
                    EditedTextEntry.COLUMN_NAME_NOTE + " TEXT)";

    public static final String SQL_CREATE_IMAGE_TABLE =
            "CREATE TABLE " + ImageEntry.TABLE_NAME + " (" +
                    ImageEntry._ID + " INTEGER PRIMARY KEY," +
                    ImageEntry.COLUMN_NAME_IMAGE_NAME + " TEXT," +
                    ImageEntry.COLUMN_NAME_IMAGE_URI + " TEXT," +
                    ImageEntry.COLUMN_NAME_IMAGE_SIZE + " INTEGER," +
                    ImageEntry.COLUMN_NAME_EDITED_TEXT_ID + " INTEGER," +
                    "FOREIGN KEY(" + ImageEntry.COLUMN_NAME_EDITED_TEXT_ID + ") REFERENCES " +
                    EditedTextEntry.TABLE_NAME + "(" + EditedTextEntry._ID + ") ON DELETE CASCADE)";

    public static final String SQL_CREATE_FILE_TABLE =
            "CREATE TABLE " + FileEntry.TABLE_NAME + " (" +
                    FileEntry._ID + " INTEGER PRIMARY KEY," +
                    FileEntry.COLUMN_NAME_FILE_NAME + " TEXT," +
                    FileEntry.COLUMN_NAME_FILE_PATH + " TEXT," +
                    FileEntry.COLUMN_NAME_FILE_DATA + " BLOB," +
                    FileEntry.COLUMN_NAME_FILE_SIZE + " INTEGER," +
                    FileEntry.COLUMN_NAME_EDITED_TEXT_ID + " INTEGER," +
                    "FOREIGN KEY(" + FileEntry.COLUMN_NAME_EDITED_TEXT_ID + ") REFERENCES " +
                    EditedTextEntry.TABLE_NAME + "(" + EditedTextEntry._ID + ") ON DELETE CASCADE)";
}

