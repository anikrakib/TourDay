package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Profile.ViewProfilePagerAdapter;
import com.anikrakib.tourday.Adapter.Search.ViewSearchPagerAdapter;
import com.anikrakib.tourday.Fragment.Search.UserSearchAll;
import com.anikrakib.tourday.R;
import com.google.android.material.tabs.TabLayout;

public class SearchAllActivity extends AppCompatActivity {
    TabLayout searchAllTabLayout;
    ViewPager viewPager;
    ViewSearchPagerAdapter viewSearchPagerAdapter;
    TextView keyWord;
    Intent intent;
    public static String keyWordText;
    ImageButton backButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);

        keyWord = findViewById(R.id.searchKeyWord);
        viewPager = findViewById(R.id.viewPagerSearchAll);
        backButton = findViewById(R.id.searchAllBackButton);

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        keyWordText = extras.getString("keyword");

        keyWord.setText("\""+keyWordText+"\"");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final UserSearchAll userSearchAll = new UserSearchAll();

        Bundle bundle = new Bundle();
        String myMessage = "Stackoverflow is cool!";
        bundle.putString("message", myMessage );
        UserSearchAll fragInfo = new UserSearchAll();
        fragInfo.setArguments(bundle);
        //fragmentTransaction.replace(R.id., fragInfo);
        fragmentTransaction.commit();


        /////*     initialize ViewPager   */////
        viewSearchPagerAdapter = new ViewSearchPagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPager.setAdapter(viewSearchPagerAdapter);
        searchAllTabLayout = findViewById(R.id.searchAllTabLayout);
        searchAllTabLayout.setupWithViewPager(viewPager);
        searchAllTabLayout.setTabRippleColor(null);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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