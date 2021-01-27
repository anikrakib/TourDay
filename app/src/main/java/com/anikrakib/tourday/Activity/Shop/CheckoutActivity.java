package com.anikrakib.tourday.Activity.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Shop.AdapterAllCartList;
import com.anikrakib.tourday.Adapter.Shop.AdapterProductOrderList;
import com.anikrakib.tourday.Fragment.Shop.Checkout.CheckOutAddressFragment;
import com.anikrakib.tourday.Fragment.Shop.Checkout.CheckOutOrderDetailsFragment;
import com.anikrakib.tourday.Fragment.Shop.Checkout.CheckOutPaymentFragment;
import com.anikrakib.tourday.Fragment.Shop.ShopHomeFragment;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ShopCartTable;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    public static Fragment orderFragment,addressFragment,paymentFragment,homeFragment,active;
    public static FragmentManager fm = null;
    TextView confirmOrder,orderDetails,payment,information,number1,number2,number3,totalAmount;
    LinearLayout orderListLayout,informationLayout,paymentLayout;

    List<ShopCartTable> shopCartTables;
    MyDatabase myDatabase;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        confirmOrder = findViewById(R.id.tvConfirmOrder);
        information = findViewById(R.id.tvInfo);
        orderDetails = findViewById(R.id.tvDelivery);
        payment = findViewById(R.id.tvPayment);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        orderListLayout = findViewById(R.id.orderDetailsLayout);
        informationLayout = findViewById(R.id.informationLayout);
        paymentLayout = findViewById(R.id.paymentLayout);
        totalAmount = findViewById(R.id.tvTotal);

        if(loadNightModeState()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        myDatabase = MyDatabase.getInstance(getApplicationContext());
        shopCartTables = myDatabase.favouriteEventDatabaseDao().getAllCartProduct();

        fm = getSupportFragmentManager();

        orderFragment = new CheckOutOrderDetailsFragment();
        active = orderFragment;
        fm.beginTransaction().add(R.id.container, active).commit();

        totalAmount.setText("৳ "+grandTotal());

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                if(confirmOrder.getText().toString().equals("Next")){

                    number1.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                    orderDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                    number2.setBackground(getDrawable(R.drawable.ic_next_process));
                    information.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_text));

                    number3.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                    payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                    addressFragment = new CheckOutAddressFragment();
                    active = addressFragment;
                    fm.beginTransaction().add(R.id.container, active).commit();
                    confirmOrder.setText("Continue");

                }else if(confirmOrder.getText().toString().equals("Continue")){

                    number1.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                    orderDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                    number3.setBackground(getDrawable(R.drawable.ic_next_process));
                    payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_text));

                    number2.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                    information.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                    paymentFragment = new CheckOutPaymentFragment();
                    active = paymentFragment;
                    fm.beginTransaction().add(R.id.container, active).commit();

                    confirmOrder.setText("Done");

                }else if(confirmOrder.getText().toString().equals("Done")){
                    myDatabase.favouriteEventDatabaseDao().deleteAllCartListProduct(shopCartTables);
                    startActivity(new Intent(CheckoutActivity.this,ShopActivity.class));
                    finish();
                }
            }
        });

        orderListLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                number1.setBackground(getDrawable(R.drawable.ic_next_process));
                orderDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_text));

                number2.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                information.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                number3.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                orderFragment = new CheckOutOrderDetailsFragment();
                active = orderFragment;
                fm.beginTransaction().add(R.id.container, active).commit();

                confirmOrder.setText("Next");
            }
        });

        informationLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                number1.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                orderDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                number2.setBackground(getDrawable(R.drawable.ic_next_process));
                information.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_text));

                number3.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                addressFragment = new CheckOutAddressFragment();
                active = addressFragment;
                fm.beginTransaction().add(R.id.container, active).commit();

                confirmOrder.setText("Continue");
            }
        });

        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                number1.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                orderDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                number2.setBackground(getDrawable(R.drawable.ic_next_process_gray));
                information.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grayColor));

                number3.setBackground(getDrawable(R.drawable.ic_next_process));
                payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_text));

                paymentFragment = new CheckOutPaymentFragment();
                active = paymentFragment;
                fm.beginTransaction().add(R.id.container, active).commit();

                confirmOrder.setText("Done");
            }
        });
    }

    // this method will load the Night Mode State
    public Boolean loadNightModeState (){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("nightMode", Context.MODE_PRIVATE);
        return userPref.getBoolean("night_mode",false);
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

    public int grandTotal() {
        int totalPrice = 0;
        for (int i = 0; i < shopCartTables.size(); i++) {
            totalPrice += (shopCartTables.get(i).getProductPrice()*shopCartTables.get(i).getProductQuantity());
        }
        return totalPrice;
    }
}