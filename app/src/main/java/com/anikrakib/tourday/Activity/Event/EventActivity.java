package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Event.ViewEventPagerAdapter;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.marozzi.roundbutton.RoundButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class EventActivity extends AppCompatActivity {

    /*     initialize variable   */
    TabLayout tabLayoutEvent;
    ViewPager viewPagerEvent;
    ViewEventPagerAdapter viewEventPagerAdapter;
    FloatingActionButton createEvent;
    Dialog myDialog, mDialog;
    ImageButton profileBackButton, refreshLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText editTextLocation,eventPopUpTitle,eventPopUpDescription;
    int totalCapacity;
    String[] paymentType;
    Resources resources;
    TextView eventDate;
    final String[] eventTitleSave = new String[1];
    final String[] eventDescriptionSave = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        /*     initialize view   */
        createEvent = findViewById(R.id.fabButtonCreateEvent);
        profileBackButton = findViewById(R.id.backButtonEvent);
        viewPagerEvent = (ViewPager) findViewById(R.id.viewPagerEventActivity);


        myDialog = new Dialog(this);
        mDialog = new Dialog(this);
        resources= getResources();
        paymentType = resources.getStringArray(R.array.paymentType);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /////*     initialize ViewPager   */////
        viewEventPagerAdapter = new ViewEventPagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPagerEvent.setAdapter(viewEventPagerAdapter);
        tabLayoutEvent = (TabLayout) findViewById(R.id.slidingTabsEventActivity);
        tabLayoutEvent.setupWithViewPager(viewPagerEvent);
        tabLayoutEvent.setTabRippleColor(null);


        /*     on click listener   */
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

    }

    @Override
    public void onBackPressed() {
        EventActivity.this.finish();
    }

    public void createEventPopUp() {
        ImageButton postCloseButton;
        Animation top_to_bottom,bottom_to_top;
        EditText capacity,cost,accountNumber1,accountNumber2;
        ImageView inc,dec;
        final ConstraintLayout createEventLayout;
        Spinner paymentTypeSpinner1,paymentTypeSpinner2;
        RoundButton createEventButton;

        myDialog.setContentView(R.layout.create_event);
        postCloseButton = myDialog.findViewById(R.id.createEventCloseButton);
        editTextLocation = myDialog.findViewById(R.id.createEventLocationEditText);
        createEventLayout = myDialog.findViewById(R.id.createEventLayout);
        eventPopUpTitle = myDialog.findViewById(R.id.popupEventTitle);
        eventPopUpDescription = myDialog.findViewById(R.id.popupEventDescription);
        inc = myDialog.findViewById(R.id.increment);
        dec = myDialog.findViewById(R.id.decrement);
        capacity = myDialog.findViewById(R.id.displayCapacity);
        accountNumber1 = myDialog.findViewById(R.id.numberEditText1);
        accountNumber2 = myDialog.findViewById(R.id.numberEditText2);
        paymentTypeSpinner1 = myDialog.findViewById(R.id.paymentTypeSpinner1);
        paymentTypeSpinner2 = myDialog.findViewById(R.id.paymentTypeSpinner2);
        eventDate = myDialog.findViewById(R.id.eventStartDate);
        createEventButton = myDialog.findViewById(R.id.uploadButton);
        cost = myDialog.findViewById(R.id.eventCost);

        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        // set value in district spinner
        ArrayAdapter<String> paymentTypeSpinnerString = new ArrayAdapter<String>(this,R.layout.custom_spinner_item,R.id.districtNameTextView,paymentType);
        paymentTypeSpinner1.setAdapter(paymentTypeSpinnerString);
        paymentTypeSpinner2.setAdapter(paymentTypeSpinnerString);

        // Retrieve and set Event Title and Description from SharedPreferences when again open CreateEvent PopUp
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String eventTitle = sharedPreferences.getString("eventTitle","");
        String eventDescription = sharedPreferences.getString("eventDescription","");

        //get current date and set date textView
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(date);
        eventDate.setText(formattedDate);

        eventPopUpTitle.setText(eventTitle);
        eventPopUpDescription.setText(eventDescription);

        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Event Title and Description in SharedPreferences when close CreateEvent PopUp

                createEventLayout.startAnimation(bottom_to_top);


                eventTitleSave[0] = eventPopUpTitle.getText().toString();
                eventDescriptionSave[0] = eventPopUpDescription.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("eventTitle", eventTitleSave[0]);
                editor.putString("eventDescription", eventDescriptionSave[0]);
                editor.apply();

                handlerForCustomDialog();
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(totalCapacity <= 0){
                    capacity.setText("0");
                }else{
                    totalCapacity = Integer.parseInt(capacity.getText().toString());
                    totalCapacity--;
                    capacity.setText(totalCapacity+"");
                }
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent(eventPopUpTitle.getText().toString(),editTextLocation.getText().toString(),
                        eventDate.getText().toString(),eventPopUpDescription.getText().toString(),
                        paymentTypeSpinner1.getSelectedItem().toString(),accountNumber1.getText().toString(),
                        paymentTypeSpinner2.getSelectedItem().toString(),accountNumber2.getText().toString(),
                        capacity.getText().toString(),cost.getText().toString());
            }
        });

        eventDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                datePickerPopUp();
                //materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        inc.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                totalCapacity = Integer.parseInt(capacity.getText().toString());
                totalCapacity++;
                capacity.setText(totalCapacity+"");
            }
        });

        createEventLayout.startAnimation(top_to_bottom);

        myDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().getAttributes().gravity = Gravity.TOP;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void datePickerPopUp() {

        mDialog.setContentView(R.layout.custom_date_picker_pop_up);

        DatePicker calendar = mDialog.findViewById(R.id.cdrvCalendar);
        // disable previous days
        calendar.setMinDate(System.currentTimeMillis() - 1000);

        calendar.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                eventDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                mDialog.dismiss();
            }
        });

        mDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.show();

    }

    public void createEvent(String blogTitle, String blogLocation, String blogDate, String blogDetails, String blogPay1, String blogPay1Method, String blogPay2, String blogPay2Method, String blogCapacity, String blogCost){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createEvent("Token "+token,blogTitle,blogLocation,blogDate,blogDetails,blogPay1,blogPay1Method,blogPay2,blogPay2Method,blogCapacity,blogCost);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    DynamicToast.makeSuccess(getApplicationContext(), "Event Created").show();
                    myDialog.dismiss();
                    // post description shared pref removed
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("eventTitle", eventTitleSave[0]);
                    editor.putString("eventDescription", eventDescriptionSave[0]);
                    editor.apply();
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong").show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void handlerForCustomDialog(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myDialog.dismiss();
            }
        },500);
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