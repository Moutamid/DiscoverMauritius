package com.moutamid.sqlapp.activities.Tour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.sqlapp.R;

public class ToursActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);
        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        String url = getString(R.string.privacy_url);
        webView.loadUrl(url);
    }


    public void BackPress(View view) {
        onBackPressed();
    }
}