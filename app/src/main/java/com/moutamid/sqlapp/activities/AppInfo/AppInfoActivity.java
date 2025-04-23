package com.moutamid.sqlapp.activities.AppInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.moutamid.sqlapp.R;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
    }

    public void BackPress(View view) {
        onBackPressed();
    }
}