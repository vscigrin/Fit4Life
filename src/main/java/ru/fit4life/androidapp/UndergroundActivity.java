package ru.fit4life.androidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class UndergroundActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //WebView webview = new WebView(this);
        //setContentView(webview);
        try {
            Log.i(Sys.TAG, "inicialize method....");
            WebView myWebView = new WebView(this);
            Log.i(Sys.TAG, "web view created");
            setContentView(myWebView);
            Log.i(Sys.TAG, "web view seted as content");
            myWebView.loadUrl("http://www.fit4life.ru/dieti-pohydenia/zhenskaya-figura01.html");
            Log.i(Sys.TAG, "url loaded");
        } catch (Exception e) {
            Log.e(Sys.TAG, e.getMessage());
            Log.e(Sys.TAG, e.getLocalizedMessage());
        }


    }

    public void navigateBack(View view) {
        finish();
    }

    public void navigateHome(View view) {

        Intent intent = new Intent(UndergroundActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
