package com.moutamid.sqlapp.activities.AppInfo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.moutamid.sqlapp.R;

public class AppInfoActivity extends AppCompatActivity {

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
    }

    public void BackPress(View view) {
        onBackPressed();
    }
}