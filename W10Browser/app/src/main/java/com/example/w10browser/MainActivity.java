package com.example.w10browser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    WebView web;
    EditText addressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.WebView);
        addressBar = findViewById(R.id.editTextAddressBar);
        web.setWebViewClient(new WebViewClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.canGoBackOrForward(10);
        addressBar.setOnEditorActionListener((v, actionId, event) -> {
            System.out.println("url given");
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                String url = addressBar.getText().toString();
                if (url.equals("index.html")) {
                    url = "file:///android_asset/index.html";
                } else if (!url.split("/")[0].equals("http:") ||
                !url.split("/")[0].equals("https:")) {
                    url = "http://" + url;
                }
                loadUrl(url);
                return true;
            }
            return false;
        });
    }

    public void loadUrl(String url) {
        web.loadUrl(url);
    }

    public void refresh(View v) {
        System.out.println("Refresh");
        loadUrl(web.getOriginalUrl());
    }

    public void pageNext(View v) {
        System.out.println("Next");
        web.goForward();
    }

    public void pagePrev(View v) {
        System.out.println("Prev");
        web.goBack();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initialize(View v) {
        if (web.getOriginalUrl().equals("file:///android_asset/index.html")) {
            web.evaluateJavascript("javascript:initialize()", null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void shoutOut(View v) {
        if (web.getOriginalUrl().equals("file:///android_asset/index.html")) {
            web.evaluateJavascript("javascript:shoutOut()", null);
        }
    }
}