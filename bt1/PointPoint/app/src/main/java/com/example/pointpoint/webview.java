package com.example.pointpoint;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webview extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        WebView webview = (WebView) findViewById(R.id.webView1);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://www.google.com/maps/place/Street+Taco+Shop+18A/@10.7727283,106.6782156,13z/data=!4m8!1m2!2m1!1staco+shop!3m4!1s0x31752fd7f1982c65:0x4c10eb8ec15ac8c1!8m2!3d10.7864401!4d106.7002472?hl=vi-VN");
    }
}
