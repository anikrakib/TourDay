package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.anikrakib.tourday.Activity.Authentication.SignInActivity;
import com.anikrakib.tourday.R;

public class BDMapViewActivity extends AppCompatActivity {
    ImageButton bdMapBackButton;
    Dialog mDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_d_map_view);
        bdMapBackButton = findViewById(R.id.bdMapBackButton);

        mDialog = new Dialog(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        bdMapBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = userPref.getString("userName","");

        final WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        if(username.isEmpty()){
            showWarningPopUp();
        }else{
            webView.loadUrl("https://www.tourday.team/api/map/"+username);
        }
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) BDMapViewActivity.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
    }

    public void onBackPressed() {
        BDMapViewActivity.this.finish();
    }

    public void runWarningPopUp(){
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                startActivity(new Intent(BDMapViewActivity.this, SignInActivity.class));
                finish();
            }
        }, 2000);
        mDialog.dismiss();
    }
    private void showWarningPopUp(){
        mDialog.setContentView(R.layout.custom_warning_pop_up);
        mDialog.setCancelable(false);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        runWarningPopUp();
        mDialog.show();
    }
}