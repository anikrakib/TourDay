package com.anikrakib.tourday.Activity.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Fragment.Shop.Checkout.CheckOutAddressFragment;
import com.anikrakib.tourday.Fragment.Shop.Checkout.CheckOutOrderDetailsFragment;
import com.anikrakib.tourday.R;

public class CheckoutActivity extends AppCompatActivity {

    public static Fragment orderFragment,addressFragment,paymentFragment,active;
    public static FragmentManager fm = null;
    TextView confirmOrder,orderDetails,information,number1,number2,number3;
    LinearLayout orderListLayout,informationLayout,paymentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        confirmOrder = findViewById(R.id.tvConfirmOrder);
        information = findViewById(R.id.tvInfo);
        orderDetails = findViewById(R.id.tvDelivery);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        orderListLayout = findViewById(R.id.orderDetailsLayout);
        informationLayout = findViewById(R.id.informationLayout);
        paymentLayout = findViewById(R.id.paymentLayout);

        fm = getSupportFragmentManager();

        orderFragment = new CheckOutOrderDetailsFragment();
        active = orderFragment;
        fm.beginTransaction().add(R.id.container, active).commit();

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {
                number1.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                orderDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                number2.setBackground(getDrawable(R.drawable.ic_next_process));
                information.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_text));

                addressFragment = new CheckOutAddressFragment();
                active = addressFragment;
                fm.beginTransaction().add(R.id.container, active).commit();
            }
        });

        orderListLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                number1.setBackground(getDrawable(R.drawable.ic_next_process));
                orderDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_text));

                number2.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                information.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                orderFragment = new CheckOutOrderDetailsFragment();
                active = orderFragment;
                fm.beginTransaction().add(R.id.container, active).commit();
            }
        });

        informationLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                number1.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                orderDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                number2.setBackground(getDrawable(R.drawable.ic_next_process));
                information.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_text));

                addressFragment = new CheckOutAddressFragment();
                active = addressFragment;
                fm.beginTransaction().add(R.id.container, active).commit();
            }
        });
    }
}