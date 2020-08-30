package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.anikrakib.tourday.R;

public class BDMapViewActivity extends AppCompatActivity {
    ImageButton bdMapBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_d_map_view);
        bdMapBackButton = findViewById(R.id.bdMapBackButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        bdMapBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        //for remove specific div
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url)
//            {
//                webView.loadUrl("javascript:(function() { " +
//                        "var head = document.getElementsByClassName('btn')[0].style.display='none'; " +
//                        "})()");
//            }
//        });

        webView.loadUrl("https://anikrakib.github.io/BD-Map/");
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) BDMapViewActivity.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
    }

    public void onBackPressed() {
        BDMapViewActivity.this.finish();
    }


}