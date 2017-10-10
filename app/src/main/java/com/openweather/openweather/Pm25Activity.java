package com.openweather.openweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class Pm25Activity extends AppCompatActivity {

    private WebView mWebView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm25);
        Fabric.with(this, new Crashlytics());
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://env.healthinfo.tw/air/");
    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
    public void onBack(View view) {
        finish();
    }
}
