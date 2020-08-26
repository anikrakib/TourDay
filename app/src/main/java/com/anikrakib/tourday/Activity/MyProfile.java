package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.anikrakib.tourday.Adapter.ViewPagerAdapter;
import com.anikrakib.tourday.R;
import com.google.android.material.tabs.TabLayout;

public class MyProfile extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    ImageButton profileBackButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        coordinatorLayout = findViewById(R.id.profileLayout);
        profileBackButton = findViewById(R.id.profileBackButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }



        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        /////*     initialize view   */////
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        /////*     initialize ViewPager   */////
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(null);

    }

    @Override
    public void onBackPressed() {
        MyProfile.this.finish();
    }
}