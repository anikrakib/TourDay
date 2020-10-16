package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anikrakib.tourday.Adapter.Event.AdapterGoingEvent;
import com.anikrakib.tourday.Models.Event.GoingEvent;
import com.anikrakib.tourday.R;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    RecyclerView category;
    AdapterGoingEvent adapterGoingEvent;
    LinearLayout goingLinearLayout,pendingLinearLayout;
    RelativeLayout joinNow;
    Dialog myDialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        category = findViewById(R.id.eventGoingRecyclerView);
        goingLinearLayout = findViewById(R.id.goingLinearLayout);
        pendingLinearLayout = findViewById(R.id.pendingLinearLayout);
        joinNow = findViewById(R.id.joinNowRelativeLayOut);

        myDialog = new Dialog(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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
}