package com.anikrakib.tourday.Activity.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.anikrakib.tourday.Fragment.Shop.Checkout.CheckOutOrderDetails;
import com.anikrakib.tourday.Fragment.Shop.ShopHomeFragment;
import com.anikrakib.tourday.R;

public class CheckoutActivity extends AppCompatActivity {

    public static Fragment orderFragment,addressFragment,paymentFragment,active;
    public static FragmentManager fm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        fm = getSupportFragmentManager();

        orderFragment = new CheckOutOrderDetails();
        active = orderFragment;
        fm.beginTransaction().add(R.id.container, orderFragment).commit();
    }
}