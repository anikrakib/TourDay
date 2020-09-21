package com.anikrakib.tourday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anikrakib.tourday.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity {
    Intent intent;
    TextView recentLocation,currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Dialog mDialog;
    LinearLayout editLocationBody;
    ImageView backButton,copyLocation;
    ImageButton refreshLocation,saveNewLocation;
    EditText newLocationEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        recentLocation = findViewById(R.id.recentLocation);
        currentLocation = findViewById(R.id.currentLocation);
        editLocationBody = findViewById(R.id.editLocationBody);
        refreshLocation = findViewById(R.id.refreshLocation);
        backButton = findViewById(R.id.backButtonLocation);
        copyLocation = findViewById(R.id.copyLocationButton);
        newLocationEdit = findViewById(R.id.newlocationEditText);
        saveNewLocation = findViewById(R.id.clickOkNewLocationImageButton);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mDialog = new Dialog(this);

        intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            String recentLocationString =(String) bundle.get("recentLocation");
            recentLocation.setText(recentLocationString);
        }


        if (ActivityCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            statusCheck();
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        refreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCheck();
                getCurrentLocation();
            }
        });

        copyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", currentLocation.getText().toString());
                clipboard.setPrimaryClip(clip);
                DynamicToast.makeSuccess(getApplicationContext(), "Your Current Address copy to Clipboard").show();
                copyLocation.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.black));

            }
        });

        newLocationEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveNewLocation.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.black));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        LocationActivity.this.finish();
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
                        Geocoder geocoder = new Geocoder(LocationActivity.this,
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        currentLocation.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //buildAlertMessageNoGps();
            createEnableLocationPopUp();

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
}