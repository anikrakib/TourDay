package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Event.AdapterGoingUserEvent;
import com.anikrakib.tourday.Adapter.Event.AdapterPendingPayment;
import com.anikrakib.tourday.Models.Blog.DeleteBlogResponse;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Event.DeleteEventResponse;
import com.anikrakib.tourday.Models.Event.GoingUser;
import com.anikrakib.tourday.Models.Event.PendingPayment;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.marozzi.roundbutton.RoundButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourEventDetailsActivity extends AppCompatActivity {
    RelativeLayout pendingPaymentLinearLayout;
    RecyclerView pendingUserRecyclerView,goingUserRecyclerView;
    AdapterPendingPayment adapterPendingPayment;
    Intent intent;
    TextView eventDetailsTitleTextView,eventLocationTextView,eventTotalGoingTextView,eventTotalPendingTextView,eventTotalCapacityTextView;
    SocialTextView eventDetailsTextView;
    KenBurnsView eventDetailsImage;
    List<GoingUser> goingUserList;
    List<PendingPayment> pendingPayment;
    ImageButton backButton,deleteEventButton,bookmarkButton;
    AdapterGoingUserEvent adapterGoingUserEvent;
    LinearLayout goingLinearLayout;
    int eventId,totalCapacity,eventCost;
    Dialog myDialog,mDialog;
    SharedPreferences userPref;
    EditText capacity,cost,accountNumber1,accountNumber2,editTextLocation,eventPopUpTitle,eventPopUpDescription;
    String eventDateString,eventUrl,account1,account2;
    TextView eventDate;
    String[] paymentType;
    Resources resources;
    FloatingActionButton editEvent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_event_details);

        pendingPaymentLinearLayout = findViewById(R.id.upDownArrowPendingPaymentRelativeLayOut);
        eventDetailsImage = findViewById(R.id.eventDetailsImage);
        eventDetailsTitleTextView = findViewById(R.id.eventDetailsTitleTextView);
        eventLocationTextView = findViewById(R.id.eventDetailsLocationTextView);
        eventTotalCapacityTextView = findViewById(R.id.eventDetailsTotalCapacityTextView);
        eventTotalGoingTextView = findViewById(R.id.eventDetailsTotalGoingTextView);
        eventTotalPendingTextView = findViewById(R.id.eventDetailsTotalPendingTextView);
        eventDetailsTextView = findViewById(R.id.eventDetailsDescriptionTextView);
        pendingUserRecyclerView = findViewById(R.id.pendingUserRecyclerView);
        backButton = findViewById(R.id.backButtonEvent);
        goingUserRecyclerView = findViewById(R.id.eventGoingRecyclerView);
        goingLinearLayout = findViewById(R.id.goingYourEventLinearLayout);
        deleteEventButton = findViewById(R.id.deleteEvent);
        bookmarkButton = findViewById(R.id.favouriteButton);
        editEvent = findViewById(R.id.yourEventEditFloatingActionButton);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        myDialog = new Dialog(this);
        mDialog = new Dialog(this);
        deleteEventButton.setVisibility(View.VISIBLE);
        bookmarkButton.setVisibility(View.GONE);
        resources= getResources();
        paymentType = resources.getStringArray(R.array.paymentType);

        assert bundle != null;
        eventId = bundle.getInt("eventId");

        //set Data
        getEventAllDate();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteBlogPopUp(eventId);
            }
        });

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEventPopUp();
            }
        });

        goingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goingUserRecyclerView.getVisibility() == View.GONE){
                    goingUserRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    goingUserRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        pendingPaymentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pendingUserRecyclerView.getVisibility() == View.GONE){
                    pendingUserRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    pendingUserRecyclerView.setVisibility(View.GONE);
                }
            }
        });


        // goingUser List
        goingUserList = new ArrayList<>();
        pendingPayment = new ArrayList<>();
        goingUserList(eventId);
        getPendingList(eventId);


    }

    public void editEventPopUp() {
        ImageButton postCloseButton;
        Animation top_to_bottom,bottom_to_top;
        ImageView inc,dec;
        final ConstraintLayout createEventLayout;
        Spinner paymentTypeSpinner1,paymentTypeSpinner2;
        RoundButton updateButton;
        CircleImageView userImage;
        RelativeLayout moreAboutEvent,eventImageLayout;
        LinearLayout eventMoreLayout;
        RoundedImageView eventImage;
        ImageView upDownMore1,upDownMore2;

        myDialog.setContentView(R.layout.edit_event);
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
        updateButton = myDialog.findViewById(R.id.updateButton);
        cost = myDialog.findViewById(R.id.eventCost);
        userImage = myDialog.findViewById(R.id.popup_user_image);
        moreAboutEvent = myDialog.findViewById(R.id.moreAboutRelativeLayout);
        eventImageLayout = myDialog.findViewById(R.id.eventImageRelativeLayout);
        eventMoreLayout = myDialog.findViewById(R.id.eventMoreLayout);
        eventImage = myDialog.findViewById(R.id.eventImage);
        upDownMore1 = myDialog.findViewById(R.id.upDownMoreLayout);
        upDownMore2 = myDialog.findViewById(R.id.upDownMoreEventImage);

        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        // set value in district spinner
        ArrayAdapter<String> paymentTypeSpinnerString = new ArrayAdapter<String>(this,R.layout.custom_spinner_item,R.id.districtNameTextView,paymentType);
        paymentTypeSpinner1.setAdapter(paymentTypeSpinnerString);
        paymentTypeSpinner2.setAdapter(paymentTypeSpinnerString);

        // Retrieve and set Event Title and Description from SharedPreferences when again open CreateEvent PopUp
        String userProfilePicture = userPref.getString("userProfilePicture","");

        Picasso.get().load(ApiURL.IMAGE_BASE+userProfilePicture).into(userImage);
        Picasso.get().load(ApiURL.IMAGE_BASE+eventUrl).into(eventImage);
        eventPopUpDescription.setText(eventDetailsTextView.getText().toString());
        eventPopUpTitle.setText(eventDetailsTitleTextView.getText().toString());
        eventDate.setText(eventDateString);
        editTextLocation.setText(eventLocationTextView.getText().toString());
        cost.setText(String.valueOf(eventCost));
        capacity.setText(eventTotalCapacityTextView.getText().toString());
        accountNumber1.setText(account1);
        accountNumber1.setText(account2);

        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Event Title and Description in SharedPreferences when close CreateEvent PopUp
                createEventLayout.startAnimation(bottom_to_top);
                handlerForCustomDialog();
            }
        });

        moreAboutEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventMoreLayout.getVisibility() == View.GONE){
                    eventMoreLayout.setVisibility(View.VISIBLE);
                    upDownMore1.setImageResource(R.drawable.ic_up);
                    eventImage.setVisibility(View.GONE);
                    upDownMore2.setImageResource(R.drawable.ic_down);
                }else{
                    eventMoreLayout.setVisibility(View.GONE);
                    upDownMore1.setImageResource(R.drawable.ic_down);
                }
            }
        });

        eventImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventImage.getVisibility() == View.GONE){
                    eventImage.setVisibility(View.VISIBLE);
                    upDownMore2.setImageResource(R.drawable.ic_up);
                    eventMoreLayout.setVisibility(View.GONE);
                    upDownMore1.setImageResource(R.drawable.ic_down);
                }else{
                    eventImage.setVisibility(View.GONE);
                    upDownMore2.setImageResource(R.drawable.ic_down);
                }
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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        eventDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                datePickerPopUp();
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

    public void handlerForCustomDialog(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myDialog.dismiss();
            }
        },500);
    }

    public void showDeleteBlogPopUp(int eventId) {
        Button yesButton,noButton;
        myDialog.setContentView(R.layout.custom_delete_blog_pop_up);
        yesButton = myDialog.findViewById(R.id.yesButton);
        noButton = myDialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(eventId);
                myDialog.dismiss();
                finish();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    private void deleteEvent(int eventId) {
        String token = userPref.getString("token","");

        Call<DeleteEventResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteEvent("Token "+token,eventId);

        call.enqueue(new Callback<DeleteEventResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeleteEventResponse> call, @NonNull Response<DeleteEventResponse> response) {
                DeleteEventResponse message = response.body();
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getApplicationContext(), message.getMessage()).show();
                }else{
                    DynamicToast.makeError(getApplicationContext(), message.getMessage()).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteEventResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getEventAllDate() {
        Call<AllEventResult> resultCall = RetrofitClient
                .getInstance()
                .getApi()
                .getEventDetails(eventId);
        resultCall.enqueue(new Callback<AllEventResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AllEventResult> call, @NonNull Response<AllEventResult> response) {
                AllEventResult allEventResult = response.body();

                try {
                    eventDateString = allEventResult.getDate();
                    eventCost = allEventResult.getCost();
                    account1 = allEventResult.getPay1Method();
                    account2 = allEventResult.getPay2Method();
                    eventUrl = allEventResult.getImage();

                    eventDetailsTextView.setLinkText(allEventResult.getDetails());
                    eventDetailsTitleTextView.setText(allEventResult.getTitle());
                    eventLocationTextView.setText(allEventResult.getLocation());
                    eventTotalPendingTextView.setText(String.valueOf(allEventResult.getPending().size()));
                    eventTotalGoingTextView.setText(String.valueOf(allEventResult.getGoing().size()));
                    eventTotalCapacityTextView.setText(String.valueOf(allEventResult.getCapacity()));
                    Picasso.get().load(ApiURL.IMAGE_BASE + allEventResult.getImage()).fit().centerInside().into(eventDetailsImage);
                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"May be This Event Removed!!",Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<AllEventResult> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getPendingList(int eventId){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        pendingPayment = new ArrayList<>();
        Call<List<PendingPayment>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllPendingUser("Token "+token,eventId);
        call.enqueue(new Callback<List<PendingPayment>>() {
            @Override
            public void onResponse(@NonNull Call<List<PendingPayment>> call, @NonNull Response<List<PendingPayment>> response) {
                if (response.isSuccessful()) {
                    pendingPayment = response.body();
                }else{
                    Toast.makeText(getApplicationContext(),"Sign In Required",Toast.LENGTH_SHORT).show();
                }
                adapterPendingPayment = new AdapterPendingPayment(YourEventDetailsActivity.this,pendingPayment,eventTotalPendingTextView,eventTotalGoingTextView);
                pendingUserRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                pendingUserRecyclerView.setAdapter(adapterPendingPayment);
            }

            @Override
            public void onFailure(@NonNull Call<List<PendingPayment>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goingUserList(int eventId){
        Call<List<GoingUser>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllGoingUser(eventId);
        call.enqueue(new Callback<List<GoingUser>>() {
            @Override
            public void onResponse(@NonNull Call<List<GoingUser>> call, @NonNull Response<List<GoingUser>> response) {
                if (response.isSuccessful()) {
                    goingUserList = response.body();
                }
                adapterGoingUserEvent = new AdapterGoingUserEvent(YourEventDetailsActivity.this,goingUserList);
                goingUserRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                goingUserRecyclerView.setAdapter(adapterGoingUserEvent);

            }

            @Override
            public void onFailure(@NonNull Call<List<GoingUser>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
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
}