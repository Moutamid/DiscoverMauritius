package com.moutamid.sqlapp.activities.Organizer.Model;

import android.graphics.Bitmap;

public class FileData {
    private String fileUri;
    private String fileName;
    private long fileSize;
    private long file;
    Bitmap bitmap;


    public FileData(String fileName, long fileSize, String fileUri, Bitmap bitmap) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileUri = fileUri;
        this.bitmap = bitmap;

    }

    public FileData(long file, String  fileName, long fileSize, String fileUri, Bitmap bitmap) {
        this.file = file;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileUri = fileUri;
        this.bitmap = bitmap;

    }




    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getFile() {
        return file;
    }

    public void setFile(long file) {
        this.file = file;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
