package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anikrakib.tourday.Adapter.AdapterGoingEvent;
import com.anikrakib.tourday.Adapter.AdapterPendingPayment;
import com.anikrakib.tourday.Models.GoingEvent;
import com.anikrakib.tourday.Models.PendingPayment;
import com.anikrakib.tourday.R;

import java.util.ArrayList;
import java.util.List;

public class YourEventDetailsActivity extends AppCompatActivity {
    RelativeLayout pendingPaymentLinearLayout;
    RecyclerView pendingUserRecyclerView;
    AdapterPendingPayment adapterPendingPayment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_event_details);

        pendingPaymentLinearLayout = findViewById(R.id.upDownArrowPendingPaymentRelativeLayOut);
        pendingUserRecyclerView = findViewById(R.id.pendingUserRecyclerView);


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
        pendingPayments.add(new PendingPayment("anik","anik@gmail.com"));
        pendingPayments.add(new PendingPayment("asif","asif@gmail.com"));
        pendingPayments.add(new PendingPayment("partha","partha@gmail.com"));
        pendingPayments.add(new PendingPayment("sohag","sohag@gmail.com"));
        pendingPayments.add(new PendingPayment("adol","adol@gmail.com"));


        adapterPendingPayment = new AdapterPendingPayment(YourEventDetailsActivity.this,pendingPayments);
        pendingUserRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        pendingUserRecyclerView.setAdapter(adapterPendingPayment);
    }
}