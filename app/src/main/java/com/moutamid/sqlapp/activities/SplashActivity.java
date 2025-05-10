package com.moutamid.sqlapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Stash.put(Constants.IS_PREMIUM, true);
        Stash.put("wasRunning", false);
        LinearLayout splashTextLayout = findViewById(R.id.splash_text_layout);
        Animation zoomAndFade = AnimationUtils.loadAnimation(this, R.anim.zoom_in_and_fade_out);
        splashTextLayout.startAnimation(zoomAndFade);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }, 1800);
    }

}