package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import com.anikrakib.tourday.Adapter.ViewCreatePostAndEventPagerAdapter;
import com.anikrakib.tourday.Adapter.ViewEventPagerAdapter;
import com.anikrakib.tourday.Adapter.ViewProfilePagerAdapter;
import com.anikrakib.tourday.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class EventActivity extends AppCompatActivity {
    TabLayout tabLayoutEvent;
    ViewPager viewPagerEvent;
    ViewEventPagerAdapter viewEventPagerAdapter;
    FloatingActionButton createEvent;
    Dialog myDialog;
    ImageButton profileBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        createEvent =  findViewById(R.id.fabButtonCreateEvent);
        profileBackButton = findViewById(R.id.backButtonEvent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        myDialog = new Dialog(this);

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventPopUp();
            }
        });
        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /////*     initialize view   */////
        viewPagerEvent = (ViewPager) findViewById(R.id.viewPagerEventActivity);

        /////*     initialize ViewPager   */////
        viewEventPagerAdapter = new ViewEventPagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPagerEvent.setAdapter(viewEventPagerAdapter);
        tabLayoutEvent = (TabLayout) findViewById(R.id.slidingTabsEventActivity);
        tabLayoutEvent.setupWithViewPager(viewPagerEvent);
        tabLayoutEvent.setTabRippleColor(null);

        /////*     Check SocialMediaLink is null or not   */////




    }

    @Override
    public void onBackPressed() {
        EventActivity.this.finish();
    }

    public void createEventPopUp() {
        ImageButton postCloseButton;

        myDialog.setContentView(R.layout.create_event);
        postCloseButton= myDialog.findViewById(R.id.createEventCloseButton);

        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.hide();
            }
        });


        myDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().getAttributes().gravity = Gravity.TOP;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }

}