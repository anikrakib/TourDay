package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.Window;
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
    WebView webView;
    String username;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_d_map_view);
        bdMapBackButton = findViewById(R.id.bdMapBackButton);

        if(loadNightModeState()){
            if (Build.VERSION.SDK_INT >= 23) {
                setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
                getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        mDialog = new Dialog(this);


        bdMapBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = userPref.getString("userName","");

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        if(username.isEmpty()){
            showWarningPopUp();
        }else{
            setMapTheme();
            //webView.loadUrl("https://www.tourday.team/api/map/"+username);
        }
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) BDMapViewActivity.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
    }

    private void setMapTheme() {
        if(loadNightModeState()){
            webView.loadUrl("https://www.tourday.team/api/map-dark/"+username);
        }else{
            webView.loadUrl("https://www.tourday.team/api/map/"+username);
        }
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

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }
    public Boolean loadNightModeState (){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("nightMode", Context.MODE_PRIVATE);
        return userPref.getBoolean("night_mode",false);
    }
}