package com.anikrakib.tourday.Activity.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Shop.CategoryItemAdapter;
import com.anikrakib.tourday.Fragment.Shop.ShopHomeFragment;
import com.anikrakib.tourday.Fragment.Shop.WishListFragment;
import com.anikrakib.tourday.Models.Shop.CategoryListItem;
import com.anikrakib.tourday.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    ImageView homeIcon,wishListIcon,cartIcon,orderIcon;
    TextView homeText,wishListText,myCartText,myOrderText;
    LinearLayout homeLayout,wishLayout,cartLayout,orderLayout;
    ImageView categoryView;
    ImageButton backButton,filter;
    RecyclerView categoryRecyclerView;
    List<CategoryListItem> categoryListItems;
    CategoryItemAdapter categoryItemAdapter;

    public static Fragment homeFragment,wishListFragment,active;
    public static FragmentManager fm = null;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        homeIcon = findViewById(R.id.homeIcon);
        wishListIcon = findViewById(R.id.wishListIcon);
        cartIcon = findViewById(R.id.catIcon);
        orderIcon = findViewById(R.id.myOrderIcon);
        homeText = findViewById(R.id.homeText);
        wishListText = findViewById(R.id.wishListText);
        myCartText = findViewById(R.id.myCartText);
        myOrderText = findViewById(R.id.myOrderText);
        homeLayout = findViewById(R.id.homeLayout);
        wishLayout = findViewById(R.id.wishListLayout);
        cartLayout = findViewById(R.id.myCartLayout);
        orderLayout = findViewById(R.id.myOrderLayout);
        categoryView = findViewById(R.id.categoryImageIcon);
        backButton = findViewById(R.id.shopBackButton);
        filter = findViewById(R.id.filter);

        homeText.setTextColor(Color.rgb(255, 115, 70));
        homeText.setTextSize(15);
        homeIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.dividerColor));

        fm = getSupportFragmentManager();

        homeFragment = new ShopHomeFragment();
        active = homeFragment;
        fm.beginTransaction().add(R.id.container, homeFragment).commit();

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void onClick(View v) {
                homeText.setTextColor(Color.rgb(255, 115, 70));
                homeText.setTextSize(15);
                homeIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.dividerColor));

                wishListText.setTextColor(Color.rgb(189, 195, 210));
                wishListText.setTextSize(12);
                wishListIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                myCartText.setTextColor(Color.rgb(189, 195, 210));
                myCartText.setTextSize(12);
                cartIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                myOrderText.setTextColor(Color.rgb(189, 195, 210));
                myOrderText.setTextSize(12);
                orderIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                homeFragment = new ShopHomeFragment();
                active = homeFragment;
                fm.beginTransaction().replace(R.id.container, active).commit();
            }
        });

        wishListIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                wishListText.setTextColor(Color.rgb(255, 115, 70));
                wishListText.setTextSize(15);
                wishListIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.dividerColor));

                homeText.setTextColor(Color.rgb(189, 195, 210));
                homeText.setTextSize(12);
                homeIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                myCartText.setTextColor(Color.rgb(189, 195, 210));
                myCartText.setTextSize(12);
                cartIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                myOrderText.setTextColor(Color.rgb(189, 195, 210));
                myOrderText.setTextSize(12);
                orderIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                wishListFragment = new WishListFragment();
                active = wishListFragment;
                fm.beginTransaction().replace(R.id.container, active).commit();
            }
        });

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                myCartText.setTextColor(Color.rgb(255, 115, 70));
                myCartText.setTextSize(15);
                cartIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.dividerColor));

                homeText.setTextColor(Color.rgb(189, 195, 210));
                homeText.setTextSize(12);
                homeIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                wishListText.setTextColor(Color.rgb(189, 195, 210));
                wishListText.setTextSize(12);
                wishListIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                myOrderText.setTextColor(Color.rgb(189, 195, 210));
                myOrderText.setTextSize(12);
                orderIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));
            }
        });

        orderIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                myOrderText.setTextColor(Color.rgb(255, 115, 70));
                myOrderText.setTextSize(15);
                orderIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.dividerColor));

                homeText.setTextColor(Color.rgb(189, 195, 210));
                homeText.setTextSize(12);
                homeIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                wishListText.setTextColor(Color.rgb(189, 195, 210));
                wishListText.setTextSize(12);
                wishListIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));

                myCartText.setTextColor(Color.rgb(189, 195, 210));
                myCartText.setTextSize(12);
                cartIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.iconColor));
            }
        });

        categoryView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        ShopActivity.this,R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.bottom_sheet_layout,findViewById(R.id.bottomSheetContainer));

                categoryRecyclerView = bottomSheetView.findViewById(R.id.categoryRecyclerView);
                categoryRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
//
                //Toast.makeText(getApplicationContext(),"Toast",Toast.LENGTH_SHORT).show();
                categoryListItems = new ArrayList<>();
                categoryListItems.add(new CategoryListItem(R.drawable.bag_icon,"Bag"));
                categoryListItems.add(new CategoryListItem(R.drawable.sunglass,"Sun Glass"));
                categoryListItems.add(new CategoryListItem(R.drawable.headphone,"Headphone"));
                categoryListItems.add(new CategoryListItem(R.drawable.gadget,"Gadget Accessories"));
                categoryListItems.add(new CategoryListItem(R.drawable.t_shirt,"T Shirt"));
                categoryListItems.add(new CategoryListItem(R.drawable.cap,"Cap"));
                categoryListItems.add(new CategoryListItem(R.drawable.sneaker,"Shoes"));

                categoryItemAdapter = new CategoryItemAdapter(getApplicationContext(),categoryListItems);

                categoryRecyclerView.setAdapter(categoryItemAdapter);
//
                //((View)bottomSheetView.getParent()).setBackgroundColor(Color.TRANSPARENT);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllCategory();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getAllCategory() {
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
    public Boolean loadNightModeState (){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("nightMode", Context.MODE_PRIVATE);
        return userPref.getBoolean("night_mode",false);
    }
}