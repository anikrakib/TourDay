package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.anikrakib.tourday.Adapter.ViewPagerAdapter;
import com.anikrakib.tourday.R;
import com.google.android.material.tabs.TabLayout;
import com.marozzi.roundbutton.RoundButton;

public class MyProfileActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    ImageButton profileBackButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ImageView facebookLinkImageView,instagramLinkImageView,messengerLinkImageView;
    Dialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        coordinatorLayout = findViewById(R.id.profileLayout);
        profileBackButton = findViewById(R.id.profileBackButton);
        facebookLinkImageView = findViewById(R.id.facebookLinkImageView);
        instagramLinkImageView = findViewById(R.id.instagramLinkImageView);
        messengerLinkImageView = findViewById(R.id.messengerLinkImageView);


        myDialog = new Dialog(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        /////*     Click Listener     */////

        facebookLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v.getId());
            }
        });
        instagramLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v.getId());
            }
        });
        messengerLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v.getId());
            }
        });



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
        MyProfileActivity.this.finish();
    }
    public void showPopup(final int id) {
        ImageView close;
        Button uploadButton;
        final EditText socialMediaLinkEditText;

        myDialog.setContentView(R.layout.custom_social_media_link_pop_up);
        socialMediaLinkEditText = myDialog.findViewById(R.id.socialMediaLinkEditText);
        close = myDialog.findViewById(R.id.txtclose);
        uploadButton = myDialog.findViewById(R.id.uploadButton);

        ColorFilter test = facebookLinkImageView.getColorFilter();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RoundButton bt = (RoundButton) v;
                bt.startAnimation();
                bt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.dismiss();
                        if(id == R.id.facebookLinkImageView){
                            facebookLinkImageView.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.dark_blue));
                        }else if(id == R.id.instagramLinkImageView){
                            instagramLinkImageView.setImageResource(R.drawable.instagram);
                        }
                        else if(id == R.id.messengerLinkImageView){
                            messengerLinkImageView.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.dark_blue));
                        }
                    }
                }, 3000);
            }
        });
        if(id == R.id.facebookLinkImageView){
            socialMediaLinkEditText.setHint("Enter Your Facebook URl");
        }else if(id == R.id.instagramLinkImageView){
            socialMediaLinkEditText.setHint("Enter Your Instagram URl");
        }
        else if(id == R.id.messengerLinkImageView){
            socialMediaLinkEditText.setHint("Enter Your Messenger URl");
        }


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}