package com.moutamid.sqlapp.activities.Organizer.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.moutamid.sqlapp.activities.Organizer.Model.EditedText;
import com.moutamid.sqlapp.activities.Organizer.Model.FileData;
import com.moutamid.sqlapp.activities.Organizer.Model.ImageData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "organizer.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(DatabaseContract.SQL_CREATE_EDITED_TEXT_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_IMAGE_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_FILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade policy, if needed
    }

    // Insert or update edited text data
    public long insertOrUpdateEditedText(String docType, String docTitle, String docNumber,
                                         String countryDoc, String issuedBy, String issuedDate,
                                         String expireDate, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TYPE, docType);
        values.put(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TITLE, docTitle);
        values.put(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_NUMBER, docNumber);
        values.put(DatabaseContract.EditedTextEntry.COLUMN_NAME_COUNTRY_DOCUMENT, countryDoc);
        values.put(DatabaseContract.EditedTextEntry.COLUMN_NAME_ISSUED_BY, issuedBy);
        values.put(DatabaseContract.EditedTextEntry.COLUMN_NAME_ISSUED_DATE, issuedDate);
        values.put(DatabaseContract.EditedTextEntry.COLUMN_NAME_EXPIRE_DATE, expireDate);
        values.put(DatabaseContract.EditedTextEntry.COLUMN_NAME_NOTE, note);

        // Check if the data already exists
        long editedTextId = getEditedTextIdByTitle(docTitle);
        if (editedTextId == -1) {
            // Data does not exist, insert new record
            editedTextId = db.insert(DatabaseContract.EditedTextEntry.TABLE_NAME, null, values);
        } else {
            // Data already exists, update the record
            db.update(DatabaseContract.EditedTextEntry.TABLE_NAME, values,
                    DatabaseContract.EditedTextEntry._ID + " = ?", new String[]{String.valueOf(editedTextId)});
        }

        return editedTextId;
    }

    // Helper method to get edited text ID by its title
    private long getEditedTextIdByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {DatabaseContract.EditedTextEntry._ID};
        String selection = DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TITLE + " = ?";
        String[] selectionArgs = {title};
        Cursor cursor = db.query(
                DatabaseContract.EditedTextEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        long editedTextId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            editedTextId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.EditedTextEntry._ID));
            cursor.close();
        }
        return editedTextId;
    }

    // Retrieve all edited text data from the database
    public List<EditedText> getAllEditedTexts() {
        List<EditedText> editedTexts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.EditedTextEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                EditedText editedText = new EditedText();
                editedText.setId(cursor.getLong(cursor.getColumnIndex(DatabaseContract.EditedTextEntry._ID)));
                editedText.setDocumentType(cursor.getString(cursor.getColumnIndex(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TYPE)));
                editedText.setDocumentTitle(cursor.getString(cursor.getColumnIndex(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_TITLE)));
                editedText.setDocumentNumber(cursor.getString(cursor.getColumnIndex(DatabaseContract.EditedTextEntry.COLUMN_NAME_DOCUMENT_NUMBER)));
                editedText.setCountryDocument(cursor.getString(cursor.getColumnIndex(DatabaseContract.EditedTextEntry.COLUMN_NAME_COUNTRY_DOCUMENT)));
                editedText.setIssuedBy(cursor.getString(cursor.getColumnIndex(DatabaseContract.EditedTextEntry.COLUMN_NAME_ISSUED_BY)));
                editedText.setIssuedDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.EditedTextEntry.COLUMN_NAME_ISSUED_DATE)));
                editedText.setExpireDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.EditedTextEntry.COLUMN_NAME_EXPIRE_DATE)));
                editedText.setNote(cursor.getString(cursor.getColumnIndex(DatabaseContract.EditedTextEntry.COLUMN_NAME_NOTE)));
                List<ImageData> images = getImagesForEditedText(editedText.getId());
                editedText.setImages(images);
                List<FileData> files = getFilesForEditedText(editedText.getId());
                editedText.setFiles(files);
                editedTexts.add(editedText);
            }
            cursor.close();
        }
        return editedTexts;
    }

    // Retrieve images associated with edited text from the database
    public List<ImageData> getImagesForEditedText(long editedTextId) {
        List<ImageData> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {DatabaseContract.ImageEntry._ID,
                DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_NAME,
                DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_SIZE,
                DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_URI
        }; // Include image URI column
        String selection = DatabaseContract.ImageEntry.COLUMN_NAME_EDITED_TEXT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(editedTextId)};
        Cursor cursor = db.query(
                DatabaseContract.ImageEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long imageId = cursor.getLong(cursor.getColumnIndex(DatabaseContract.ImageEntry._ID));
                String imageName = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_NAME));
                long imageSize = cursor.getLong(cursor.getColumnIndex(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_SIZE));
                Uri imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_URI))); // Get image URI
                images.add(new ImageData(imageUri, imageName, imageSize)); // Pass image URI to constructor
            }
            cursor.close();
        }
        return images;
    }

    // Retrieve files associated with edited text from the database
    // Retrieve files associated with edited text from the database
// Retrieve files associated with edited text from the database
    public List<FileData> getFilesForEditedText(long editedTextId) {
        List<FileData> files = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                DatabaseContract.FileEntry._ID,
                DatabaseContract.FileEntry.COLUMN_NAME_FILE_NAME,
                DatabaseContract.FileEntry.COLUMN_NAME_FILE_PATH,
                DatabaseContract.FileEntry.COLUMN_NAME_FILE_SIZE,
                DatabaseContract.FileEntry.COLUMN_NAME_FILE_DATA
        };
        String selection = DatabaseContract.FileEntry.COLUMN_NAME_EDITED_TEXT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(editedTextId)};
        Cursor cursor = db.query(
                DatabaseContract.FileEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long fileId = cursor.getLong(cursor.getColumnIndex(DatabaseContract.FileEntry._ID));
                String fileName = cursor.getString(cursor.getColumnIndex(DatabaseContract.FileEntry.COLUMN_NAME_FILE_NAME));
                String filePath = cursor.getString(cursor.getColumnIndex(DatabaseContract.FileEntry.COLUMN_NAME_FILE_PATH));
                long fileSize = cursor.getLong(cursor.getColumnIndex(DatabaseContract.FileEntry.COLUMN_NAME_FILE_SIZE));
                // Read file data as byte array
                byte[] fileData = cursor.getBlob(cursor.getColumnIndex(DatabaseContract.FileEntry.COLUMN_NAME_FILE_DATA));
                Log.d("path", filePath+ " path");
                // Convert byte array to Bitmap if needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);

                files.add(new FileData(fileId, fileName, fileSize, (filePath), bitmap));
            }
            cursor.close();
        }
        return files;
    }

    // Insert image data for edited text
    public long insertImageForEditedText(long editedTextId, String imageName, long imageSize, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_NAME, imageName);
        values.put(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_SIZE, imageSize);
        values.put(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_URI, imageUri); // Include image URI
        values.put(DatabaseContract.ImageEntry.COLUMN_NAME_EDITED_TEXT_ID, editedTextId);
        return db.insert(DatabaseContract.ImageEntry.TABLE_NAME, null, values);
    }

    // Insert file data for edited text
    public long insertFileForEditedText(long editedTextId, Bitmap bitmap, String fileName, long fileSize, String filePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.FileEntry.COLUMN_NAME_FILE_NAME, fileName);
        values.put(DatabaseContract.FileEntry.COLUMN_NAME_FILE_SIZE, fileSize);
        values.put(DatabaseContract.FileEntry.COLUMN_NAME_FILE_PATH, filePath); // Add file path to database
        values.put(DatabaseContract.FileEntry.COLUMN_NAME_EDITED_TEXT_ID, editedTextId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        values.put(DatabaseContract.FileEntry.COLUMN_NAME_FILE_DATA, byteArray); // Add image data to database
        Log.d("daaaaaaa", "  "+values);

        return db.insert(DatabaseContract.FileEntry.TABLE_NAME, null, values);
    }

    public void deleteEditedText(long editedTextId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContract.EditedTextEntry.TABLE_NAME,
                DatabaseContract.EditedTextEntry._ID + " = ?",
                new String[]{String.valueOf(editedTextId)});
        // You may also need to delete associated images and files for the edited text
        deleteImagesForEditedText(editedTextId);
        deleteFilesForEditedText(editedTextId);
    }

    // Method to delete images associated with edited text
    public void deleteImagesForEditedText(long editedTextId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContract.ImageEntry.TABLE_NAME,
                DatabaseContract.ImageEntry.COLUMN_NAME_EDITED_TEXT_ID + " = ?",
                new String[]{String.valueOf(editedTextId)});
    }

    // Method to delete files associated with edited text
    public void deleteFilesForEditedText(long editedTextId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContract.FileEntry.TABLE_NAME,
                DatabaseContract.FileEntry.COLUMN_NAME_EDITED_TEXT_ID + " = ?",
                new String[]{String.valueOf(editedTextId)});
    }
    public int updateImageData(long imageId, String imageName, long imageSize, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_NAME, imageName);
        values.put(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_SIZE, imageSize);
        values.put(DatabaseContract.ImageEntry.COLUMN_NAME_IMAGE_URI, imageUri);
        return db.update(DatabaseContract.ImageEntry.TABLE_NAME, values,
                DatabaseContract.ImageEntry._ID + " = ?", new String[]{String.valueOf(imageId)});
    }

    // Delete image data
    public int deleteImageData(long imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DatabaseContract.ImageEntry.TABLE_NAME,
                DatabaseContract.ImageEntry._ID + " = ?", new String[]{String.valueOf(imageId)});
    }

    // Update file data
    public int updateFileData(long fileId, String fileName, long fileSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.FileEntry.COLUMN_NAME_FILE_NAME, fileName);
        values.put(DatabaseContract.FileEntry.COLUMN_NAME_FILE_SIZE, fileSize);
        return db.update(DatabaseContract.FileEntry.TABLE_NAME, values,
                DatabaseContract.FileEntry._ID + " = ?", new String[]{String.valueOf(fileId)});
    }

    // Delete file data
    public int deleteFileData(long fileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DatabaseContract.FileEntry.TABLE_NAME,
                DatabaseContract.FileEntry._ID + " = ?", new String[]{String.valueOf(fileId)});
    }
}
