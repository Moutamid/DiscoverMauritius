package com.moutamid.sqlapp.activities.Organizer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.moutamid.sqlapp.R;

public class ImageViewrActivity extends AppCompatActivity {
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewr);
        image = findViewById(R.id.image);
        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));

        // Load the image into the ImageView using Glide or any other library
        Glide.with(this)
                .load(imageUri)
                .into(image);  }

    public void back(View view) {
        onBackPressed();
    }
}