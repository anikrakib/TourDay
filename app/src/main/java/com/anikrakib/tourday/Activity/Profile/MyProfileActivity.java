package com.anikrakib.tourday.Activity.Profile;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.ExploreActivity;
import com.anikrakib.tourday.Activity.LocationActivity;
import com.anikrakib.tourday.Adapter.Profile.ViewProfilePagerAdapter;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.kishandonga.csbx.CustomSnackbar;
import com.marozzi.roundbutton.RoundButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfileActivity extends AppCompatActivity{

    /*     initialize variable   */
    ImageButton profileBackButton,profileMoreButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewProfilePagerAdapter viewProfilePagerAdapter;
    ImageView chooseImage_ImageView,facebookLinkImageView,instagramLinkImageView,bangladeshImageView,editNameImageView;
    Dialog myDialog,myDialog2;
    FloatingActionButton floatingActionButtonCreatePost;
    Button uploadButton,saveButton;
    EditText socialMediaLinkEditText,postPopUpDescription,nameEditTest;
    TextView userFullName,facebookLink,instagramLink,createPostDate;
    CircleImageView userProfilePic;
    private static final int INTENT_REQUEST_CODE = 100;
    Resources resources;
    String[] districtKeys;
    InputStream postInputStream;
    Boolean createPostImageClick = false;
    ImageView postImageView;
    String token,userOldPassword;
    public static String location = "";
    public static final Pattern USER_NAME = Pattern.compile("^([a-z])+([\\w.]{2,})+$");

    // moreOptionPopUp variable
    SocialTextView userNameInPopUp;
    CircleImageView userImageInPopUp;
    TextView userBioInPopUp,userFullNameInPopUp;
    SwipeRefreshLayout swipeRefreshLayout;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        /*     initialize view   */
        profileBackButton = findViewById(R.id.profileBackButton);
        facebookLinkImageView = findViewById(R.id.facebookLinkImageView);
        instagramLinkImageView = findViewById(R.id.instagramLinkImageView);
        bangladeshImageView = findViewById(R.id.bangladeshImageView);
        floatingActionButtonCreatePost = findViewById(R.id.fabButtonCreatePost);
        userFullName = findViewById(R.id.userFullName);
        facebookLink = findViewById(R.id.facebookLinkTextView);
        instagramLink = findViewById(R.id.instagramLinkTextView);
        userProfilePic = findViewById(R.id.userProfilePic);
        editNameImageView = findViewById(R.id.editNameImageView);
        chooseImage_ImageView = findViewById(R.id.chooseImage_ImageView);
        viewPager = findViewById(R.id.viewPager);
        profileMoreButton = findViewById(R.id.profileMoreIcon);

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token","");
        userOldPassword = userPref.getString("password","");

        resources= getResources();
        districtKeys = resources.getStringArray(R.array.bdDistrict);

        myDialog = new Dialog(this);
        myDialog2 = new Dialog(this);

        // show current login user all data
        showUserData();

        /////*     initialize ViewPager   */////
        viewProfilePagerAdapter = new ViewProfilePagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPager.setAdapter(viewProfilePagerAdapter);
        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(null);


        /*    On Click Listener     */

        chooseImage_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MyProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                } else {
                    ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 44);
                }

            }
        });

        facebookLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFacebookAppInstalled()) {
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = getFacebookPageURL(getApplicationContext());
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    startActivity(facebookIntent);

                } else {
                    showUserSocialMediaAccount("https://www.facebook.com/" + facebookLink.getText().toString());
                }
            }
        });
        instagramLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInstagramInstalled()) {
                    Intent instagramIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = getInstragamPageURL(getApplicationContext());
                    instagramIntent.setData(Uri.parse(facebookUrl));
                    startActivity(instagramIntent);

                } else {
                    showUserSocialMediaAccount("https://www.instagram.com/" + instagramLink.getText().toString());
                }
            }
        });
        bangladeshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBdMap();
            }
        });
        floatingActionButtonCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPostPopUp();
            }
        });

        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        editNameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNamePopUp(userFullName.getText().toString());
            }
        });
        profileMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileEditPopUp();
            }
        });

    }

    private void showProfileEditPopUp() {
        View touch;
        Animation rightToLeft,leftToRight;
        LinearLayout linearLayout,emailLayout,changePasswordLayout,deleteAccountLayout,manageMyAccountLayout,manageMyAccountPart,locationLayout,facebookIdLayout,instagramIdLayout;
        ImageView backButton;

        myDialog.setContentView(R.layout.profile_more_option_pop_up);
        touch = myDialog.findViewById(R.id.touchView);
        linearLayout = myDialog.findViewById(R.id.moreOptionLayout);
        backButton = myDialog.findViewById(R.id.backButton);
        manageMyAccountLayout = myDialog.findViewById(R.id.manageMyAccountLayout);
        emailLayout = myDialog.findViewById(R.id.editEmailLayout);
        changePasswordLayout = myDialog.findViewById(R.id.passwordChangeLayout);
        manageMyAccountPart = myDialog.findViewById(R.id.manageMyAccountPartLayout);
        userFullNameInPopUp = myDialog.findViewById(R.id.userFullNameInPopUp);
        userNameInPopUp = myDialog.findViewById(R.id.userUserNameInPopUp);
        userImageInPopUp = myDialog.findViewById(R.id.userImageInPopUp);
        userBioInPopUp = myDialog.findViewById(R.id.userBioInPopUp);
        locationLayout = myDialog.findViewById(R.id.locationLayout);
        facebookIdLayout = myDialog.findViewById(R.id.facebookLinkEdit);
        instagramIdLayout = myDialog.findViewById(R.id.instagramLinkEdit);
        swipeRefreshLayout = myDialog.findViewById(R.id.moreOptionSwifRefreshLayout);
        deleteAccountLayout = myDialog.findViewById(R.id.deleteAccountLayout);


        rightToLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        leftToRight = AnimationUtils.loadAnimation(this, R.anim.left_to_right);

        showUserDataInMoreOptionPopUp();


        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, LocationActivity.class)
                        .putExtra("recentLocation",location));
            }
        });
        userBioInPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, EditBioActivity.class));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showUserDataInMoreOptionPopUp();
            }
        });
        facebookIdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialMediaPopup(v.getId());
            }
        });

        instagramIdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialMediaPopup(v.getId());
            }
        });
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, EditEmailActivity.class));
            }
        });
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, ChangePasswordActivity.class));
            }
        });
        deleteAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAccount();
            }
        });
        manageMyAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manageMyAccountPart.getVisibility() == View.VISIBLE){
                    manageMyAccountPart.setVisibility(View.GONE);
                }else {
                    manageMyAccountPart.setVisibility(View.VISIBLE);
                }
            }
        });
        touch.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                linearLayout.startAnimation(leftToRight);
                handlerForCustomDialog1();
                return true;
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.startAnimation(leftToRight);
                handlerForCustomDialog1();

            }
        });


        linearLayout.startAnimation(rightToLeft);

        myDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().getAttributes().gravity = Gravity.TOP;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(true);
        myDialog.show();

    }

    public void showUserDataInMoreOptionPopUp(){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token "+token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        userFullNameInPopUp.setText(profile.getString("name"));
                        userNameInPopUp.setLinkText("@"+jsonObject.getString("username"));
                        userBioInPopUp.setText(profile.getString("bio"));
                        location = profile.getString("city");
                        swipeRefreshLayout.setRefreshing(false);
                        Picasso.get().load(ApiURL.IMAGE_BASE+profile.getString("picture")).into(userImageInPopUp);

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

    public void deleteUserAccount(){
        myDialog.dismiss();
        EditText oldPassword;
        Button okButton;

        myDialog2.setContentView(R.layout.custom_delete_account_pop_up);

        oldPassword = myDialog2.findViewById(R.id.confirmOldPassword);
        okButton = myDialog2.findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPassword.getText().toString().isEmpty()){
                    snackBar("Password Required",R.color.dark_red);
                }else{
                    if(userOldPassword.equals(getHash(oldPassword.getText().toString()))){
                        deleteAccount();
                    }else{
                        snackBar("Password Not Matched",R.color.dark_red);
                    }
                }
            }
        });

        myDialog2.setCancelable(true);
        Objects.requireNonNull(myDialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }

    private void deleteAccount() {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteAccount("Token "+token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getApplicationContext(), "Name Update Successfully").show();
                    JSONObject jsonObject = null;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        int status = jsonObject.getInt("status");
                        if(status==200){
                            DynamicToast.makeSuccess(getApplicationContext(), "Account Delete SuccessFully").show();
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
                            editor.putString("searchHistoryKey","");
                            editor.apply();
                            startActivity(new Intent(MyProfileActivity.this,ExploreActivity.class));
                            finish();
                        }else{
                            String detail = jsonObject.getString("detail");
                            snackBar(detail,R.color.dark_red);
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong!").show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //this method show pop to edit user name
    private void showEditNamePopUp(String name) {
        ImageView close;

        myDialog2.setContentView(R.layout.custom_edit_name_pop_up);
        nameEditTest = myDialog2.findViewById(R.id.nameEditText);
        close = myDialog2.findViewById(R.id.closeButtonEditNamePopUp);
        saveButton = myDialog2.findViewById(R.id.saveButton);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });

        nameEditTest.setText(name);

        nameEditTest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputsEditName();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                //rest of the work here about edit name
                if(!TextUtils.isEmpty(nameEditTest.getText().toString())){
                    updateName();
                    myDialog2.dismiss();
                    userFullName.setText(nameEditTest.getText().toString());
                }else{
                    DynamicToast.makeError(getApplicationContext(), "Name Can't Empty!").show();
                }
            }
        });


        myDialog2.setCancelable(false);
        Objects.requireNonNull(myDialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }

    /*
        Api-> (POST) "https://www.tourday.team/api/profile/"
        Token is required. Add token with request header. Send only specific filed to update it.
        Ex: If you want to update only name filed, just send name in the request. Keep other field blank.

        Response:
            {
            }
     */
    private void updateName() {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateProfileName("Token "+token,nameEditTest.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getApplicationContext(), "Name Update Successfully").show();
                    JSONObject jsonObject = null;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        userFullName.setText(profile.getString("name"));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong!").show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateInstagramLink(String link) {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateInstagramLink("Token "+token,link);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    showSocialmediaLink("insta");
                    socialMediaLinkEditText.setText(instagramLink.getText().toString());
                    DynamicToast.makeSuccess(getApplicationContext(), "Updated").show();
                }else{
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong!").show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateFacebookLink(String link) {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateFacebookLink("Token "+token,link);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    showSocialmediaLink("fb");
                    socialMediaLinkEditText.setText(facebookLink.getText().toString());
                    DynamicToast.makeSuccess(getApplicationContext(), "Updated").show();
                }else{
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong!").show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showSocialmediaLink(String linkType){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token "+token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        if(linkType.equals("fb")) {
                            facebookLink.setText(profile.getString("fb"));
                            if(!(facebookLink.getText().toString().equals("null") || facebookLink.getText().toString().isEmpty())){
                                facebookLinkImageView.setVisibility(View.VISIBLE);
                            }else{
                                facebookLink.setText("");
                                facebookLinkImageView.setVisibility(View.GONE);
                            }
                        }
                        else {
                            instagramLink.setText(profile.getString("insta"));
                            if(!(instagramLink.getText().toString().equals("null") || instagramLink.getText().toString().isEmpty())){
                                instagramLinkImageView.setVisibility(View.VISIBLE);
                            }else{
                                instagramLink.setText("");
                                instagramLinkImageView.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Token Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyProfileActivity.this, ExploreActivity.class));
    }

    //this method show pop to edit socialMedia Link
    public void showSocialMediaPopup(final int id) {
        ImageView close;

        myDialog2.setContentView(R.layout.custom_social_media_link_pop_up);
        socialMediaLinkEditText = myDialog2.findViewById(R.id.socialMediaLinkEditText);
        close = myDialog2.findViewById(R.id.txtclose);
        uploadButton = myDialog2.findViewById(R.id.uploadButton);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });

        socialMediaLinkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputsSocialMediaLink();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(id == R.id.facebookLinkEdit ){
                    updateFacebookLink(socialMediaLinkEditText.getText().toString().trim());
                    myDialog2.dismiss();
                }else if(id == R.id.instagramLinkEdit ){
                    updateInstagramLink(socialMediaLinkEditText.getText().toString().trim());
                    myDialog2.dismiss();
                }
            }
        });

        if(id == R.id.facebookLinkImageView || id == R.id.facebookLinkEdit ){
            if(facebookLink.getText().toString().isEmpty()){
                socialMediaLinkEditText.setHint("Only facebook username");
            }else{
                showSocialmediaLink("fb");
                socialMediaLinkEditText.setText(facebookLink.getText().toString());
            }
        }else if(id == R.id.instagramLinkImageView || id == R.id.instagramLinkEdit ){
            if(instagramLink.getText().toString().isEmpty()) {
                socialMediaLinkEditText.setHint("Only instagram username");
            }else{
                showSocialmediaLink("insta");
                socialMediaLinkEditText.setText(instagramLink.getText().toString());
            }
        }

        myDialog2.setCancelable(false);
        Objects.requireNonNull(myDialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }

    //this method show pop to create post
    public void createPostPopUp() {
        ImageButton postCloseButton;
        RoundButton createPostButton;
        Animation top_to_bottom,bottom_to_top;
        CircleImageView userProfilePictureCircleImageView;
        final ConstraintLayout createPostLayout;
        Spinner districtSpinner;
        SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = userPref.edit();



        myDialog2.setContentView(R.layout.create_post);
        postCloseButton= myDialog2.findViewById(R.id.postCloseButton);
        createPostLayout = myDialog2.findViewById(R.id.createPostLayout);
        postPopUpDescription = myDialog2.findViewById(R.id.popup_description);
        userProfilePictureCircleImageView = myDialog2.findViewById(R.id.userProfilePicture);
        districtSpinner = myDialog2.findViewById(R.id.districtSpinner);
        createPostButton = myDialog2.findViewById(R.id.createPostButton);
        createPostDate = myDialog2.findViewById(R.id.createPostDate);
        postImageView = myDialog2.findViewById(R.id.postImageView);


        String userFullName = userPref.getString("userFullName","");
        assert userFullName != null;
        String[] arr = userFullName.split(" ", 2);

        postPopUpDescription.setHint("What's on your Mind,"+arr[0]+"?");


        // set animation in create post pop up
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);


        //set user profile pic and draft description using Shared Preference
        String userProfilePicture = userPref.getString("userProfilePicture","");
        String postDescription = userPref.getString("postDescription","");

        postPopUpDescription.setText(postDescription);
        Picasso.get().load("https://tourday.team"+userProfilePicture).into(userProfilePictureCircleImageView);

        //get current date and set date textView
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(date);
        createPostDate.setText(formattedDate);

        // set value in district spinner
        ArrayAdapter<String> arrayAdapterDivision = new ArrayAdapter<String>(this,R.layout.custom_spinner_item,R.id.districtNameTextView,districtKeys);
        districtSpinner.setAdapter(arrayAdapterDivision);


        // onClick Listener
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!districtHashMap(districtSpinner.getSelectedItem().toString()).isEmpty()){
                    try {
                        createPost(createPostDate.getText().toString(),postPopUpDescription.getText().toString(),districtHashMap(districtSpinner.getSelectedItem().toString()),getBytes(postInputStream));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    DynamicToast.makeWarning(getApplicationContext(), "Select District").show();
                }
            }
        });

        postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPostImageClick = true;

                if (ActivityCompat.checkSelfPermission(MyProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                } else {
                    ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 44);
                }

            }
        });

        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Post Title and Description in SharedPreferences when close Post PopUp

                createPostLayout.startAnimation(bottom_to_top);

                editor.putString("postDescription",postPopUpDescription.getText().toString());
                editor.apply();

                //myDialog.dismiss();
                handlerForCustomDialog2();
            }
        });


        createPostLayout.startAnimation(top_to_bottom);

        myDialog2.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        myDialog2.getWindow().getAttributes().gravity = Gravity.TOP;
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCancelable(false);
        myDialog2.show();

    }

    /*
        Api-> (POST) "https://www.tourday.team/api/post/"

        Use Location number instead of full location name.
        location key & Value
        {
            'Panchagarh' : 1,'Thakurgaon' : 2,'Dinajpur' : 4,'Nilphamari' : 0,'Lalmonirhat' : 3,'Kurigram' : 6,'Rangpur' : 5,'Gaibandha' : 7,'Joypurhat' : 16,'Bogra' : 11,'Naogaon' : 15,'Natore' : 12,
            'Chapainawabganj' : 10,'Pabna' : 9,'Rajshahi' : 13,'Sirajganj' : 14,'Jamalpur' : 66,'Mymensingh' : 67,'Netrokona ' : 68,'Sherpur' : 66a,'Habiganj' : 64,'Moulvibazar' : 65,'Sunamganj' : 62,
            'Sylhet' : 63,'Dhaka' : 73,'Faridpur' : 70,'Gazipur' : 76,'Gopalganj' : 71,'Kishoreganj' : 83,'Madaripur' : 75,'Manikganj' : 72,'Munshiganj' : 79,'Narayanganj' : 78,'Narsingdi' : 77,'Rajbari' : 69,
            'Shariatpur' : 74,'Tangail' : 84,'Jessore' : 19,'Jhenaidah' : 17,'Khulna' : 22,'Kushtia' : 23,'Magura' : 18,'Meherpur' : 24,'Narail' : 20,'Satkhira' : 21,'Bagerhat' : 26,'Chuadanga' : 25,'Barguna' : 32,
            'Barisal' : 31,'Bhola' : 37,'Jhalokati' : 30,'Patuakhali' : 33,'Pirojpur' : 29,'Chattogram' : 54,'Bandarban' : 56,'Brahmanbaria' : 41,'Chandpur' : 44,'Comilla' : 43,'Cox's Bazar' : 59,'Feni' : 51,
            'Khagrachhari' : 55,'Lakshmipur' : 45,'Noakhali' : 46,'Rangamati' : 57
        }

        Response:
        {
            "post": "post text here",
            "location": 1,
            "date": "yyyy-mm-dd",
            "image": image.jpg
        }

    */
    public void createPost(String posDate, String postDescription, String districtCode, byte[] imageBytes){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), imageBytes);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"),postDescription );
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"),districtCode );
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"),posDate );

        MultipartBody.Part postImage = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createPost("Token "+token,description,location,date,postImage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    DynamicToast.makeSuccess(getApplicationContext(), "Post Created").show();
                    myDialog2.dismiss();
                    // post description shared pref removed
                    SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("postDescription","");
                    editor.apply();
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong").show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String districtHashMap(String districtKey){
        String districtCode = "";
        Resources resources = getResources();

        HashMap<String, String> meMap=new HashMap<String, String>();

        String[] keys = resources.getStringArray(R.array.bdDistrict);
        String[] values = resources.getStringArray(R.array.bdDistrictLocationCode);


        for(int i = 0; i < keys.length; i++){
            meMap.put(keys[i], values[i]);
        }

        for (String s : meMap.keySet()) {
            districtCode = meMap.get(districtKey);

        }
        return districtCode;
    }

    private void checkInputsSocialMediaLink() {
        if(TextUtils.isEmpty(socialMediaLinkEditText.getText())){
            uploadButton.setEnabled(true);
            uploadButton.setBackgroundResource(R.drawable.button_background);
        }else{
            if (isValidUserName(socialMediaLinkEditText.getText().toString())) {
                uploadButton.setEnabled(true);
                uploadButton.setBackgroundResource(R.drawable.button_background);
            } else {
                uploadButton.setEnabled(false);
                uploadButton.setBackgroundResource(R.drawable.disable_button_background);
            }
        }
    }
    public static boolean isValidUserName(String mobileNo){
        Matcher matcher = USER_NAME.matcher(mobileNo);
        return matcher.matches();
    }


    private void checkInputsEditName() {
        if (!TextUtils.isEmpty(userFullName.getText())) {
            saveButton.setEnabled(true);
            saveButton.setBackgroundResource(R.drawable.button_background);
        } else {
            saveButton.setEnabled(false);
            saveButton.setBackgroundResource(R.drawable.disable_button_background);
        }
    }

    /*
        Api-> (GET) "https://www.tourday.team/api/profile/"
        Token is required. Add token with request header.
        Request:
            {
                "Authorization": "Token 93bc86220b144548e5bb507851b6ef7c2a5e1a14",
            }
            Response:
            {
                "username": "anikrakib",
                "profile": {
                    "name": "Anik Rakib",
                    "email": "anikrakib@gmail.com",
                    "fb": "anik.rakib",
                    "insta": "anik_rakib",
                    "city": "Dhaka",
                    "bio": "Android Developer"
                    "picture": "/media/user/pic.jpg"
                },
                "is_completed": true
            }
     */
    public void showUserData(){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token "+token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    SharedPreferences.Editor editor = userPref.edit();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        userFullName.setText(profile.getString("name"));
                        facebookLink.setText(profile.getString("fb"));
                        instagramLink.setText(profile.getString("insta"));
                        editor.putString("userProfilePicture",profile.getString("picture"));
                        editor.putString("userName",jsonObject.getString("username"));
                        editor.putString("id",jsonObject.getString("id"));
                        editor.putString("userFullName",profile.getString("name"));
                        editor.apply();
                        Picasso.get().load("https://tourday.team"+profile.getString("picture")).into(userProfilePic);
                        Glide.with(getApplicationContext())
                                .load(ApiURL.IMAGE_BASE+profile.getString("picture"))
                                .placeholder(R.drawable.loading)
                                .error(Glide.with(getApplicationContext())
                                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                                .transforms(new CenterCrop(),new RoundedCorners(16))
                                .into(userProfilePic);

                        /////*     Check SocialMediaLink is null or not   */////
                        if(!(facebookLink.getText().toString().equals("null") || facebookLink.getText().toString().isEmpty())){
                            facebookLinkImageView.setVisibility(View.VISIBLE);
                        }else{
                            facebookLink.setText("");
                            facebookLinkImageView.setVisibility(View.GONE);
                        }
                        if(!(instagramLink.getText().toString().equals("null") || instagramLink.getText().toString().isEmpty())){
                            instagramLinkImageView.setVisibility(View.VISIBLE);
                        }else{
                            instagramLink.setText("");
                            instagramLinkImageView.setVisibility(View.GONE);
                        }

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

    //show user bdMap Profile in Popup
    @SuppressLint("SetJavaScriptEnabled")
    public void showBdMap() {
        ImageView close;
        myDialog2.setContentView(R.layout.custom_bd_map_pop_up);
        close = myDialog2.findViewById(R.id.socialMediaClose);

        final WebView webView = myDialog2.findViewById(R.id.webViewSocialMedia);

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = userPref.getString("userName","");

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        webView.loadUrl("https://www.tourday.team/api/map/"+username);
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) MyProfileActivity.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });
        myDialog2.setCancelable(false);
        Objects.requireNonNull(myDialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }

    /*
        Api-> (POST) "https://www.tourday.team/api/profile/"
        Token is required. Add token with request header. Send only specific filed to update it.
        Ex: If you want to update only name filed, just send name in the request. Keep other field blank.

        Response:
            {
            }
     */
    private void updatePhoto(byte[] imageBytes) {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), imageBytes);

        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", "image.jpg", requestFile);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateImage("Token "+token,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    DynamicToast.makeSuccess(getApplicationContext(), "Image Updated").show();
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong").show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // onActivityResult Method for retrieve image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if(createPostImageClick){
                    Uri selectedImage = data.getData();
                    try {
                        postInputStream = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                        postImageView.setImageURI(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Uri selectedImage = data.getData();
                    try {
                        InputStream is = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                        userProfilePic.setImageURI(selectedImage);
                        assert is != null;
                        updatePhoto(getBytes(is));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // set bytes in InputStream
    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    // handler for popUp animation
    public void handlerForCustomDialog1(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myDialog.dismiss();
            }
        },500);
    }

    public void handlerForCustomDialog2(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myDialog2.dismiss();
            }
        },500);
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

    @SuppressLint("SetJavaScriptEnabled")
    public void showUserSocialMediaAccount(String url) {
        ImageView close;
        myDialog2.setContentView(R.layout.custom_bd_map_pop_up);
        close = myDialog2.findViewById(R.id.socialMediaClose);

        final WebView webView = myDialog2.findViewById(R.id.webViewSocialMedia);

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
        WindowManager wm = (WindowManager) MyProfileActivity.this.getSystemService(Context.WINDOW_SERVICE);
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

    public void snackBar(String text,int color){
        CustomSnackbar sb = new CustomSnackbar(MyProfileActivity.this);
        sb.message(text);
        sb.padding(15);
        sb.textColorRes(color);
        sb.backgroundColorRes(R.color.colorPrimaryDark);
        sb.cornerRadius(15);
        sb.duration(Snackbar.LENGTH_LONG);
        sb.show();
    }

    public String getHash(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

