package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anikrakib.tourday.R;

public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                activity();
                finish();
            }
        });
        thread.start();


    }

    public void doWork(){
        for(int progress=0 ; progress<500 ; progress++){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void activity(){
        Intent intent = new Intent(MainActivity.this,ExploreActivity.class);
        startActivity(intent);
        finish();

    }


}
