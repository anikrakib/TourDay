package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.anikrakib.tourday.R;

public class YourBlogDetailsActivity extends AppCompatActivity {
    ImageButton yourBlogDetailsBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_blog_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        yourBlogDetailsBackButton = findViewById(R.id.backButtonBlogDetails);


        // on click listener
        yourBlogDetailsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}