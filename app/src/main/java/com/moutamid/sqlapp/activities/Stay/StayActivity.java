package com.moutamid.sqlapp.activities.Stay;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.sqlapp.R;
public class StayActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

//        webSettings.setJavaScriptEnabled(true);

        // Ensuring that links open within the WebView
        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("https://expedia.com/affiliates/hotel-search-mauritius.f2QG1Nw");
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setOnTouchListener((v, event) -> {
            if (v.hasFocus()) {
                v.requestFocus();
            }
            return false;
        });*/
    }

    @Override
    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
            super.onBackPressed();
//        }
    }

    public void BackPress(View view) {
        onBackPressed();
    }
}