package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.ViewOtherUsersProfilePagerAdapter;
import com.anikrakib.tourday.Adapter.ViewProfilePagerAdapter;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersUserProfile extends AppCompatActivity {

    TabLayout tabLayoutOtherUsers;
    ViewPager viewPagerOtherUsers;
    ViewOtherUsersProfilePagerAdapter viewOtherUsersProfilePagerAdapter;
    ImageView facebookLinkImageView,instagramLinkImageView,bangladeshImageView;
    Dialog myDialog;
    TextView userFullName,facebookLink,instagramLink,bio;
    CircleImageView userProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_user_profile);

        userFullName = findViewById(R.id.otherUserName);
        userProfilePic = findViewById(R.id.otherUsersProfilePic);
        facebookLink = findViewById(R.id.facebookLinkTextView);
        instagramLink = findViewById(R.id.instagramLinkTextView);
        bio = findViewById(R.id.otherUsersBio);

        /////*     initialize view   */////
        viewPagerOtherUsers = findViewById(R.id.viewPagerOtherUser);

        /////*     initialize ViewPager   */////
        viewOtherUsersProfilePagerAdapter = new ViewOtherUsersProfilePagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPagerOtherUsers.setAdapter(viewOtherUsersProfilePagerAdapter);
        tabLayoutOtherUsers = findViewById(R.id.sliding_tabs_otherUser);
        tabLayoutOtherUsers.setupWithViewPager(viewPagerOtherUsers);
        tabLayoutOtherUsers.setTabRippleColor(null);


    }


}