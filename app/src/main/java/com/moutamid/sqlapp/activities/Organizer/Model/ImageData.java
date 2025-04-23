package com.moutamid.sqlapp.activities.Organizer.Model;

import android.net.Uri;

public class ImageData {
    private String imageName;
    private long imageSize;
    private Uri imageUri; // Add image URI attribute

    public ImageData(Uri imageUri, String imageName, long imageSize) {
        this.imageUri = imageUri;
        this.imageName = imageName;
        this.imageSize = imageSize;
    }

    public String getImageName() {
        return imageName;
    }

    public long getImageSize() {
        return imageSize;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
