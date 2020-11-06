package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Event.AdapterGoingEvent;
import com.anikrakib.tourday.Models.Event.GoingEvent;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    RecyclerView category;
    AdapterGoingEvent adapterGoingEvent;
    LinearLayout goingLinearLayout,pendingLinearLayout;
    RelativeLayout joinNow;
    Dialog myDialog;
    TextView eventDetailsTitleTextView,eventLocationTextView,eventTotalGoingTextView,eventTotalPendingTextView,eventTotalCapacityTextView;
    SocialTextView eventDetailsTextView;
    Intent intent;
    KenBurnsView eventDetailsImage;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        category = findViewById(R.id.eventGoingRecyclerView);
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
        backButton = findViewById(R.id.backButtonEvent);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 23) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        myDialog = new Dialog(this);
        intent = getIntent();
        Bundle bundle = intent.getExtras();

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
        Picasso.get().load(ApiURL.IMAGE_BASE + eventImageUrl).fit().centerInside().into(eventDetailsImage);


        goingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category.getVisibility() == View.GONE){
                    category.setVisibility(View.VISIBLE);
                }else{
                    category.setVisibility(View.GONE);
                }
            }
        });
        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentPopUp();
            }
        });


        List<GoingEvent> lstCategory = new ArrayList<>();
        lstCategory.add(new GoingEvent("anik"));
        lstCategory.add(new GoingEvent("asif"));
        lstCategory.add(new GoingEvent("partha"));
        lstCategory.add(new GoingEvent("sohag"));
        lstCategory.add(new GoingEvent("adol"));

        adapterGoingEvent = new AdapterGoingEvent(EventDetailsActivity.this,lstCategory);
        category.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        category.setAdapter(adapterGoingEvent);

    }

    private void showPaymentPopUp() {
        myDialog.setContentView(R.layout.custom_payment_pop_up);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
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