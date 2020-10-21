package com.anikrakib.tourday.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Profile.ViewOtherUsersProfilePagerAdapter;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
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
    ImageView facebookLinkImageView,instagramLinkImageView,bangladeshImageView,otherUserProfileBackButton;
    Dialog myDialog;
    TextView userFullName,facebookLink,instagramLink,bio;
    CircleImageView userProfilePic;
    Intent intent;
    public String userName;
    SharedPreferences userPref ;
    SharedPreferences.Editor editor ;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_user_profile);

        userFullName = findViewById(R.id.otherUserName);
        userProfilePic = findViewById(R.id.otherUsersProfilePic);
        facebookLink = findViewById(R.id.facebookLinkTextView);
        instagramLink = findViewById(R.id.instagramLinkTextView);
        bio = findViewById(R.id.otherUsersBio);
        bangladeshImageView = findViewById(R.id.bangladeshImageView);
        facebookLinkImageView = findViewById(R.id.facebookLinkImageView);
        instagramLinkImageView = findViewById(R.id.instagramLinkImageView);
        otherUserProfileBackButton = findViewById(R.id.otherUserProfileBackButton);
        coordinatorLayout = findViewById(R.id.otherUserProfileLayout);

        myDialog = new Dialog(this);
        userPref = OthersUserProfile.this.getSharedPreferences("otherUser",getApplicationContext().MODE_PRIVATE);
        editor = userPref.edit();

        intent = getIntent();
        Bundle extras = intent.getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        assert extras != null;
        userName = extras.getString("userName");


        editor.putString("otherUsersUserName",userName);
        editor.apply();

        /////*     initialize view   */////
        viewPagerOtherUsers = findViewById(R.id.viewPagerOtherUser);

        /////*     initialize ViewPager   */////
        viewOtherUsersProfilePagerAdapter = new ViewOtherUsersProfilePagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPagerOtherUsers.setAdapter(viewOtherUsersProfilePagerAdapter);
        tabLayoutOtherUsers = findViewById(R.id.sliding_tabs_otherUser);
        tabLayoutOtherUsers.setupWithViewPager(viewPagerOtherUsers);
        tabLayoutOtherUsers.setTabRippleColor(null);

        showUserData(userName);

        bangladeshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBdMap(userName);
            }
        });

        otherUserProfileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("otherUsersUserName","");
                editor.putString("otherUsersProfilePic","");
                editor.apply();
                finish();}
        });

        facebookLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facebookLink.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, userName+" has no Facebook Account!!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();

                }else {
                    if (isFacebookAppInstalled()) {
                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                        String facebookUrl = getFacebookPageURL(getApplicationContext());
                        facebookIntent.setData(Uri.parse(facebookUrl));
                        startActivity(facebookIntent);

                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Facebook not installed!!", Snackbar.LENGTH_LONG)
                                .setAction("Download", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                        snackbar.setActionTextColor(Color.MAGENTA);
                        snackbar.show();
                        showUserSocialMediaAccount("https://www.facebook.com/" + facebookLink.getText().toString());
                    }
                }
            }
        });

        instagramLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instagramLink.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, userName+" has no Instagram Account!!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();

                }else {
                    if (isInstagramInstalled()) {
                        Intent instagramIntent = new Intent(Intent.ACTION_VIEW);
                        String facebookUrl = getInstragamPageURL(getApplicationContext());
                        instagramIntent.setData(Uri.parse(facebookUrl));
                        startActivity(instagramIntent);

                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Instagram not installed!!", Snackbar.LENGTH_LONG)
                                .setAction("Download", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                        snackbar.setActionTextColor(Color.MAGENTA);
                        snackbar.show();
                        showUserSocialMediaAccount("https://www.instagram.com/" + instagramLink.getText().toString());
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        editor.putString("otherUsersUserName","");
        editor.putString("otherUsersProfilePic","");
        editor.apply();
        super.onBackPressed();
    }

    public void showUserData(String userName){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .otherUserProfileInformation(userName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        userFullName.setText(profile.getString("name"));
                        facebookLink.setText(profile.getString("fb"));
                        instagramLink.setText(profile.getString("insta"));
                        bio.setText(profile.getString("bio"));
                        Picasso.get().load("https://www.tourday.team"+profile.getString("picture")).into(userProfilePic);

                        editor.putString("otherUsersProfilePic",profile.getString("picture"));
                        editor.apply();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Token Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void showBdMap(String userName) {
        ImageView close;
        myDialog.setContentView(R.layout.custom_bd_map_pop_up);
        close = myDialog.findViewById(R.id.socialMediaClose);

        final WebView webView = myDialog.findViewById(R.id.webViewSocialMedia);


        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("https://www.tourday.team/api/map/"+userName);
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) OthersUserProfile.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

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

    @SuppressLint("SetJavaScriptEnabled")
    public void showUserSocialMediaAccount(String url) {
        ImageView close;
        myDialog.setContentView(R.layout.custom_bd_map_pop_up);
        close = myDialog.findViewById(R.id.socialMediaClose);

        final WebView webView = myDialog.findViewById(R.id.webViewSocialMedia);

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        webView.loadUrl(url);
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) OthersUserProfile.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

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

    public String getFacebookPageURL(Context context) {
        String fbUsername = facebookLink.getText().toString();
        String FACEBOOK_URL = "https://www.facebook.com/"+fbUsername;
        String FACEBOOK_PAGE_ID = "YourPageName";
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.orca", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
    public String getInstragamPageURL(Context context) {
        String INSTAGRAM_URL = "https://www.instagram.com/"+instagramLink.getText().toString();
        Uri uri = Uri.parse("http://instagram.com/_u/"+instagramLink.getText().toString());
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(INSTAGRAM_URL)));
        }
        return INSTAGRAM_URL;
    }

    public boolean isFacebookAppInstalled() {
        try {
            getApplicationContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public boolean isInstagramInstalled() {
        try {
            getApplicationContext().getPackageManager().getApplicationInfo("com.instagram.android", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}