package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.anikrakib.tourday.R;

public class SearchActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    Animation rightToLeft,leftToRight;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        constraintLayout = findViewById(R.id.constraintLayout);
        backButton = findViewById(R.id.searchBarBackButton);

        rightToLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        leftToRight = AnimationUtils.loadAnimation(this, R.anim.left_to_right);

        constraintLayout.startAnimation(rightToLeft);
        //overridePendingTransition(R.anim.right_to_left,0);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.left_to_right);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.left_to_right);
    }
}