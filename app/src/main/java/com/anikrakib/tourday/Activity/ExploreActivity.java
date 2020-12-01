package com.anikrakib.tourday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.Authentication.SignInActivity;
import com.anikrakib.tourday.Activity.Blog.BlogActivity;
import com.anikrakib.tourday.Activity.Event.EventActivity;
import com.anikrakib.tourday.Activity.Profile.MyProfileActivity;
import com.anikrakib.tourday.R;
import com.google.android.material.navigation.NavigationView;

public class ExploreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbarMenu;
    ActionBarDrawerToggle toggle;
    Dialog myDialog;
    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences = null;
    Button search;
    EditText keyWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        myDialog = new Dialog(this);

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbarMenu = findViewById(R.id.toolbar);
        keyWord = findViewById(R.id.searchKeyWord);
        search = findViewById(R.id.searchButton);

        setTitle("");

        setSupportActionBar(toolbarMenu);

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean isLoggedIn = userPref.getBoolean("isLoggedIn",false);

        if (isLoggedIn){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.profile).setVisible(true);
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.logout).setVisible(true);
        }else{
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.profile).setVisible(false);
            menu.findItem(R.id.login).setVisible(true);
        }

        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbarMenu,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this,SearchAllActivity.class).putExtra("keyword",keyWord.getText().toString()));
            }
        });

        //change user profile name when userLogin
        String username = userPref.getString("userName","");
        Menu menu = navigationView.getMenu();
        MenuItem userProfileName = menu.findItem(R.id.profile);
        userProfileName.setTitle(username);

    }


    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(ExploreActivity.this, ExploreActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(ExploreActivity.this, MyProfileActivity.class));
                break;
            case R.id.login:
                startActivity(new Intent(ExploreActivity.this, SignInActivity.class));
                break;
            case R.id.event:
                startActivity(new Intent(ExploreActivity.this, EventActivity.class));
                break;
            case R.id.blog:
                startActivity(new Intent(ExploreActivity.this, BlogActivity.class));
                break;
            case R.id.setting:
                showPopup();
                break;
            case R.id.ecom:
                Toast.makeText(getApplicationContext(),"E commerce Comming Soon...",Toast.LENGTH_LONG).show();
                break;
            case R.id.bdmap:
                startActivity(new Intent(ExploreActivity.this, BDMapViewActivity.class));
                break;
            case R.id.logout:
                showLogoutPopUp();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
        }
        return true;
    }

    public void showPopup() {
        ImageView close;
        myDialog.setContentView(R.layout.custom_setting_pop_up);
        close = myDialog.findViewById(R.id.txtclose);
        switchCompat = myDialog.findViewById(R.id.darkMoodLightMoodSwitch);

        if (loadNightModeState()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchCompat.setChecked(false);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    setNightModeState(true);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    setNightModeState(false);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void showLogoutPopUp() {
        Button yesButton,noButton;
        myDialog.setContentView(R.layout.custom_logout_pop_up);
        yesButton = myDialog.findViewById(R.id.yesButton);
        noButton = myDialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = userPref.edit();
                editor.putBoolean("isLoggedIn",false);
                editor.putBoolean("firstTime",false);
                editor.putString("token","");
                editor.putString("userName","");
                editor.putString("userProfilePicture","");
                editor.putString("id","");
                editor.putString("password","");
                editor.putString("userFullName","");
                editor.apply();
                startActivity(new Intent(ExploreActivity.this, ExploreActivity.class));
                myDialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }
    public void setNightModeState(Boolean state) {
        SharedPreferences userPref =getApplicationContext().getSharedPreferences("nightMode", MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putBoolean("night_mode",state);
        editor.apply();
    }
    // this method will load the Night Mode State
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