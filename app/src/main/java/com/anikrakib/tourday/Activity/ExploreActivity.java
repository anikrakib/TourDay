package com.anikrakib.tourday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.anikrakib.tourday.R;
import com.google.android.material.navigation.NavigationView;

public class ExploreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbarMenu;
    ActionBarDrawerToggle toggle;
    Dialog myDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        myDialog = new Dialog(this);



        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbarMenu = findViewById(R.id.toolbar);

        setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        setSupportActionBar(toolbarMenu);

        if(SignInActivity.getToken()!= null){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.profile).setVisible(true);
            menu.findItem(R.id.login).setVisible(false);
        }else{
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.profile).setVisible(false);
        }

        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbarMenu,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


    }


    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }


    }


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
            case R.id.setting:
                showPopup();
                break;
            case R.id.bdmap:
                startActivity(new Intent(ExploreActivity.this, BDMapViewActivity.class));
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
}
