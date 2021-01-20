package com.anikrakib.tourday.Fragment.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anikrakib.tourday.R;

public class MyOrderFragment extends Fragment {

    CardView notFound;
    TextView emptyPostTextView1,emptyPostTextView2;
    public boolean isLoggedIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        emptyPostTextView1 = view.findViewById(R.id.emptyPostTextView);
        emptyPostTextView2 = view.findViewById(R.id.emptyPostTextView2);
        notFound = view.findViewById(R.id.emptyCardView);

        SharedPreferences userPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        isLoggedIn = userPref.getBoolean("isLoggedIn",false);

        if(isLoggedIn){
            notFound.setVisibility(View.VISIBLE);
            emptyPostTextView2.setText("Choose Product from shop and confirm your order");
            emptyPostTextView1.setText("You have no Order yet !!");
        }else{
            notFound.setVisibility(View.VISIBLE);
            emptyPostTextView2.setText("If you have no account, then create an account");
            emptyPostTextView1.setText("Sign In Required");
        }

        return view;
    }
}