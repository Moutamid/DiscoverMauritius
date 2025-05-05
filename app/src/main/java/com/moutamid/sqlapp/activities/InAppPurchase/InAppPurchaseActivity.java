package com.moutamid.sqlapp.activities.InAppPurchase;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.moutamid.sqlapp.R;
import com.smarteist.autoimageslider.SliderView;

public class InAppPurchaseActivity extends AppCompatActivity {
    SliderView  sliderView;
    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase);
        sliderView=findViewById(R.id.slider);
//        sliderView.sl
    }
}