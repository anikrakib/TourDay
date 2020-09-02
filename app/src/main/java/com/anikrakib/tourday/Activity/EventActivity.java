package com.anikrakib.tourday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.anikrakib.tourday.Adapter.ViewCreatePostAndEventPagerAdapter;
import com.anikrakib.tourday.Adapter.ViewEventPagerAdapter;
import com.anikrakib.tourday.Adapter.ViewProfilePagerAdapter;
import com.anikrakib.tourday.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {
    TabLayout tabLayoutEvent;
    ViewPager viewPagerEvent;
    ViewEventPagerAdapter viewEventPagerAdapter;
    FloatingActionButton createEvent;
    Dialog myDialog,mDialog;
    ImageButton profileBackButton,refreshLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText editTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        createEvent =  findViewById(R.id.fabButtonCreateEvent);
        profileBackButton = findViewById(R.id.backButtonEvent);


        myDialog = new Dialog(this);
        mDialog = new Dialog(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }



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






    }

    @Override
    public void onBackPressed() {
        EventActivity.this.finish();
    }

    public void createEventPopUp() {
        ImageButton postCloseButton;

        myDialog.setContentView(R.layout.create_event);
        postCloseButton= myDialog.findViewById(R.id.createEventCloseButton);
        editTextLocation= myDialog.findViewById(R.id.createEventLocationEditText);
        refreshLocation= myDialog.findViewById(R.id.refreshLocationInEvent);


        if (ActivityCompat.checkSelfPermission(myDialog.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(myDialog.getOwnerActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        refreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCheck();
            }
        });


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


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //buildAlertMessageNoGps();
            createEnableLocationPopUp();
        }else{
            getCurrentLocation();
        }
    }

    public void createEnableLocationPopUp() {
        Button yesButton,noButton;
        mDialog.setContentView(R.layout.custom_pop_up_enable_location);
        yesButton = mDialog.findViewById(R.id.yesButton);
        noButton = mDialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                mDialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setCancelable(false);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(myDialog.getContext(),
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        editTextLocation.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}