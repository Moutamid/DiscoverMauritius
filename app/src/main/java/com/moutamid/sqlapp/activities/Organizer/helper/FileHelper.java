package com.moutamid.sqlapp.activities.Organizer.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileHelper {

    public static byte[] readFileData(Context context, Uri fileUri) {
        byte[] fileData = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(fileUri, "r");
            if (parcelFileDescriptor != null) {
                FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

                outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                fileData = outputStream.toByteArray();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileData;
    }
    public static File saveToInternalStorage(Context context, byte[] fileData, String fileName) {
        File fileDir = context.getFilesDir();
        File file = new File(fileDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileData);
            fos.close();
            return file;
        } catch (Exception e) {
            Log.d("Exception", e.getMessage()+ "  error");

            return null;
        }
    }
    public static void openPdfFile(Context context, File pdfFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.d("Exception", e.getMessage()+ "  erro");

            // Handle the case where no PDF viewer app is installed or the file cannot be opened
        }
    }}

