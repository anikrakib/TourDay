package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Event.AdapterPendingPayment;
import com.anikrakib.tourday.Models.Event.PendingPayment;
import com.anikrakib.tourday.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;
import java.util.List;

public class YourEventDetailsActivity extends AppCompatActivity {
    RelativeLayout pendingPaymentLinearLayout;
    RecyclerView pendingUserRecyclerView;
    AdapterPendingPayment adapterPendingPayment;
    Intent intent;
    TextView eventDetailsTitleTextView,eventLocationTextView,eventTotalGoingTextView,eventTotalPendingTextView,eventTotalCapacityTextView;
    SocialTextView eventDetailsTextView;
    KenBurnsView eventDetailsImage;


    @RequiresApi(api = Build.VERSION_CODES.M)
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

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        assert bundle != null;
        int eventId = bundle.getInt("eventId");
        int capacity = bundle.getInt("eventCapacity");
        int cost = bundle.getInt("eventCost");
        int hostId = bundle.getInt("eventHostId");
        int totalGoing = bundle.getInt("eventTotalGoing");
        int totalPending = bundle.getInt("eventTotalPending");
        ArrayList<Integer> list = bundle.getIntegerArrayList("list");

        String title = bundle.getString("eventTitle");
        String location = bundle.getString("eventLocation");
        String date = bundle.getString("eventDate");
        String details = bundle.getString("eventDetails");
        String pay1 = bundle.getString("eventPay1");
        String pay1Method = bundle.getString("eventPay1Method");
        String pay2 = bundle.getString("eventPay2");
        String pay2Method = bundle.getString("eventPay2Method");
        String eventImageUrl = bundle.getString("eventImageUrl");

        //set Data
        eventDetailsTextView.setLinkText(details);
        eventDetailsTitleTextView.setText(title);
        eventLocationTextView.setText(location);
        eventTotalPendingTextView.setText(String.valueOf(totalPending));
        eventTotalGoingTextView.setText(String.valueOf(totalGoing));
        eventTotalCapacityTextView.setText(String.valueOf(capacity));
        Picasso.get().load("https://www.tourday.team/"+ eventImageUrl).fit().centerInside().into(eventDetailsImage);



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

        List<PendingPayment> pendingPayments = new ArrayList<>();

        assert list != null;
        for (int i : list) {
            pendingPayments.add(new PendingPayment(i));
        }

        adapterPendingPayment = new AdapterPendingPayment(YourEventDetailsActivity.this,pendingPayments);
        pendingUserRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        pendingUserRecyclerView.setAdapter(adapterPendingPayment);
    }
}