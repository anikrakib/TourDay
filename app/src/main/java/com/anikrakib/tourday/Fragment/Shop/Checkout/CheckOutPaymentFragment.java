package com.anikrakib.tourday.Fragment.Shop.Checkout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.anikrakib.tourday.R;

public class CheckOutPaymentFragment extends Fragment {
    RadioButton cashOnDeliveryRadiobutton,bkashRadioButton,rocketRadioButton,nagadRadiobutton;
    LinearLayout transactionIdLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out_payment, container, false);

        cashOnDeliveryRadiobutton = view.findViewById(R.id.cashOnDeliveryRadioButton);
        bkashRadioButton = view.findViewById(R.id.bkashRadioButton);
        rocketRadioButton = view.findViewById(R.id.rocketRadioButton);
        nagadRadiobutton = view.findViewById(R.id.nagadRadioButton);
        transactionIdLayout = view.findViewById(R.id.transactionIdLayout);

        cashOnDeliveryRadiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bkashRadioButton.setChecked(false);
                rocketRadioButton.setChecked(false);
                nagadRadiobutton.setChecked(false);
                transactionIdLayout.setVisibility(View.GONE);
            }
        });

        bkashRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashOnDeliveryRadiobutton.setChecked(false);
                rocketRadioButton.setChecked(false);
                nagadRadiobutton.setChecked(false);
                transactionIdLayout.setVisibility(View.VISIBLE);
            }
        });

        rocketRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashOnDeliveryRadiobutton.setChecked(false);
                bkashRadioButton.setChecked(false);
                nagadRadiobutton.setChecked(false);
                transactionIdLayout.setVisibility(View.VISIBLE);
            }
        });

        nagadRadiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bkashRadioButton.setChecked(false);
                rocketRadioButton.setChecked(false);
                cashOnDeliveryRadiobutton.setChecked(false);
                transactionIdLayout.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}