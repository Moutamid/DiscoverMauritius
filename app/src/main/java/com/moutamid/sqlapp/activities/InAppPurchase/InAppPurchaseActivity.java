package com.moutamid.sqlapp.activities.InAppPurchase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.sqlapp.R;
import com.smarteist.autoimageslider.SliderView;

public class InAppPurchaseActivity extends AppCompatActivity {
    SliderView  sliderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase);
        sliderView=findViewById(R.id.slider);
//        sliderView.sl
    }
}