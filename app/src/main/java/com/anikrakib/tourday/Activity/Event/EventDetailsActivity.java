package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Event.AdapterGoingEvent;
import com.anikrakib.tourday.Models.Event.GoingUser;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.TapToProgress.Circle;
import com.anikrakib.tourday.Utils.TapToProgress.CircleAnimation;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity implements Animation.AnimationListener {
    RecyclerView goingUserRecyclerView;
    AdapterGoingEvent adapterGoingEvent;
    LinearLayout goingLinearLayout,pendingLinearLayout;
    RelativeLayout joinNow;
    Dialog myDialog;
    TextView eventDetailsTitleTextView,eventLocationTextView,eventTotalGoingTextView,eventTotalPendingTextView,eventTotalCapacityTextView,eventAvailableTextView;
    SocialTextView eventDetailsTextView;
    Intent intent;
    KenBurnsView eventDetailsImage;
    ImageButton backButton;
    String[] paymentType;
    Resources resources;
    String pay1Method,pay2Method,pay1,pay2;
    int cost,eventId;
    List<GoingUser> goingUserList;
    boolean cancel =false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        goingUserRecyclerView = findViewById(R.id.eventGoingRecyclerView);
        goingLinearLayout = findViewById(R.id.goingLinearLayout);
        pendingLinearLayout = findViewById(R.id.pendingLinearLayout);
        joinNow = findViewById(R.id.joinNowRelativeLayOut);
        eventDetailsImage = findViewById(R.id.eventDetailsImage);
        eventDetailsTitleTextView = findViewById(R.id.eventDetailsTitleTextView);
        eventLocationTextView = findViewById(R.id.eventDetailsLocationTextView);
        eventTotalCapacityTextView = findViewById(R.id.eventDetailsTotalCapacityTextView);
        eventTotalGoingTextView = findViewById(R.id.eventDetailsTotalGoingTextView);
        eventTotalPendingTextView = findViewById(R.id.eventDetailsTotalPendingTextView);
        eventDetailsTextView = findViewById(R.id.eventDetailsDescriptionTextView);
        eventAvailableTextView = findViewById(R.id.eventDetailsAvailableTextView);
        backButton = findViewById(R.id.backButtonEvent);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 23) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        resources= getResources();
        paymentType = resources.getStringArray(R.array.paymentType);
        myDialog = new Dialog(this);
        intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        eventId = bundle.getInt("eventId");
        int capacity = bundle.getInt("eventCapacity");
        cost = bundle.getInt("eventCost");
        int hostId = bundle.getInt("eventHostId");
        int totalGoing = bundle.getInt("eventTotalGoing");
        int totalPending = bundle.getInt("eventTotalPending");
        ArrayList<Integer> list = bundle.getIntegerArrayList("list");

        String title = bundle.getString("eventTitle");
        String location = bundle.getString("eventLocation");
        String date = bundle.getString("eventDate");
        String details = bundle.getString("eventDetails");
        pay1 = bundle.getString("eventPay1");
        pay1Method = bundle.getString("eventPay1Method");
        pay2 = bundle.getString("eventPay2");
        pay2Method = bundle.getString("eventPay2Method");
        String eventImageUrl = bundle.getString("eventImageUrl");


        //set Data
        eventDetailsTextView.setLinkText(details);
        eventDetailsTitleTextView.setText(title);
        eventLocationTextView.setText(location);
        eventTotalPendingTextView.setText(String.valueOf(totalPending));
        eventTotalGoingTextView.setText(String.valueOf(totalGoing));
        eventTotalCapacityTextView.setText(String.valueOf(capacity));
        if(!(capacity-totalGoing == 0)){
            eventAvailableTextView.setText(String.valueOf(capacity-totalGoing));
        }else{
            eventAvailableTextView.setText("No Seat");
        }
        Picasso.get().load(ApiURL.IMAGE_BASE + eventImageUrl).fit().centerInside().into(eventDetailsImage);


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
        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentPopUp();
            }
        });


        // going user list
        goingUserList = new ArrayList<>();
        goingUserList(eventId);


    }

    @SuppressLint("SetTextI18n,ClickableViewAccessibility")


    private void showPaymentPopUp() {
        TextView payment1,payment2,eventPrice,infoText;
        Spinner paymentTypeSpinner;


        myDialog.setContentView(R.layout.custom_payment_pop_up);
        payment1 = myDialog.findViewById(R.id.payment1);
        payment2 = myDialog.findViewById(R.id.payment2);
        paymentTypeSpinner = myDialog.findViewById(R.id.paymentTypeSpinner);
        eventPrice = myDialog.findViewById(R.id.eventPriceTextView);


        payment1.setText(pay1Method+" - "+pay1);
        payment2.setText(pay2Method+" - "+pay2);
        eventPrice.setText(String.valueOf(cost));

        // set value in district spinner
        ArrayAdapter<String> arrayAdapterPaymentType = new ArrayAdapter<String>(this,R.layout.custom_spinner_item,R.id.districtNameTextView,paymentType);
        paymentTypeSpinner.setAdapter(arrayAdapterPaymentType);


        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
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
                adapterGoingEvent = new AdapterGoingEvent(EventDetailsActivity.this,goingUserList);
                goingUserRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                goingUserRecyclerView.setAdapter(adapterGoingEvent);
            }

            @Override
            public void onFailure(@NonNull Call<List<GoingUser>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();
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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(cancel){
            Toast.makeText(getApplicationContext(),"incomplete",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"complete",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}