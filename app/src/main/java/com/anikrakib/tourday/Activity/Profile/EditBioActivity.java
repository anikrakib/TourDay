package com.anikrakib.tourday.Activity.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Authentication.SignInActivity;
import com.anikrakib.tourday.Adapter.Profile.HobbyAdapter;
import com.anikrakib.tourday.Models.Profile.Hobby;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditBioActivity extends AppCompatActivity {
    RecyclerView hobbyRecyclerView;
    EditText bioEditText;
    ImageButton saveBio,backBio;
    TextView textView;
    Dialog postDialog ;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bio);

        hobbyRecyclerView = findViewById(R.id.hobbyRecyclerView);
        bioEditText = findViewById(R.id.aboutBio);
        saveBio = findViewById(R.id.saveButton);
        textView = findViewById(R.id.characterCount);
        backBio = findViewById(R.id.editBioBackButton);

        if(loadNightModeState()){
            if (Build.VERSION.SDK_INT >= 23) {
                setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
                getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        postDialog = new Dialog(this);


        setBio();

        List<Hobby> hobbies = new ArrayList<>();
        hobbies.add(new Hobby("\uD83C\uDFA5 Movie Lover"));
        hobbies.add(new Hobby("\uD83C\uDDE7\uD83C\uDDE9 Bangladeshi"));
        hobbies.add(new Hobby(" \uD83C\uDFB8 Guitarist"));
        hobbies.add(new Hobby("⛺️ Traveller"));
        hobbies.add(new Hobby(" \uD83C\uDFAE Video Games"));
        hobbies.add(new Hobby("\uD83D\uDEB2 Cycling"));
        hobbies.add(new Hobby("\uD83D\uDCF7 Nature Photography"));
        hobbies.add(new Hobby("\uD83D\uDCF8  Portrait Photography"));
        hobbies.add(new Hobby(" \uD83C\uDF73 Cooking"));
        hobbies.add(new Hobby("\uD83D\uDCD6 Reading"));
        hobbies.add(new Hobby("\uD83C\uDFCD Biker"));
        hobbies.add(new Hobby("\uD83E\uDE7A Doctor"));
        hobbies.add(new Hobby("\uD83D\uDCF1 Android Developer"));
        hobbies.add(new Hobby("⛳ Golf"));
        hobbies.add(new Hobby("\uD83C\uDF0E Web Developer"));
        hobbies.add(new Hobby(" \uD83D\uDC69\uD83C\uDFFD\u200D\uD83D\uDCBB  Programmer"));
        hobbies.add(new Hobby(" \uD83C\uDFB1  Poll"));
        hobbies.add(new Hobby("⚽ Football(Soccer)"));
        hobbies.add(new Hobby(" \uD83C\uDFCF Cricket"));
        hobbies.add(new Hobby(" \uD83C\uDFC0  Basketball"));
        hobbies.add(new Hobby(" \uD83C\uDF72 Food Lover"));

        //create object of adapter and initialize it
        HobbyAdapter adapter = new HobbyAdapter(hobbies, getApplicationContext(),bioEditText);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        // Set flex direction.
        layoutManager.setFlexDirection(FlexDirection.ROW);
//        // Set JustifyContent.
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        hobbyRecyclerView.setLayoutManager(layoutManager);
        hobbyRecyclerView.setAdapter(adapter);

        bioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() >= 100){
                    textView.setText("Maximum 100 Character !!");
                    textView.setTextColor(getResources().getColor(R.color.dark_red));
                }else{
                    textView.setText(String.valueOf(100-charSequence.length())+" Character Remaining");
                    textView.setTextColor(getResources().getColor(R.color.color_secondary_text));
                }
                if(!bioEditText.getText().toString().isEmpty() && charSequence.length() <= 100){
                    saveBio.setVisibility(View.VISIBLE);
                }else{
                    saveBio.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        saveBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBio();
            }
        });

        backBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setBio() {
        if(!isConnected(EditBioActivity.this)){
            showNoInternetPopUp();
            return;
        }else{
            setTimeForRunLoder();
        }
        SharedPreferences userPref = Objects.requireNonNull(getApplicationContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token "+ token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        bioEditText.setText(profile.getString("bio"));

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Token Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updateBio() {
        if(!isConnected(EditBioActivity.this)){
            showNoInternetPopUp();
            return;
        }else{
            setTimeForRunLoder();
        }
        SharedPreferences userPref = Objects.requireNonNull(getApplicationContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateBio("Token "+token,bioEditText.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(Objects.requireNonNull(getApplicationContext()), "Bio Update Successfully").show();
                }else{
                    DynamicToast.makeError(Objects.requireNonNull(getApplicationContext()), "Something Wrong!").show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isConnected(EditBioActivity editBioActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) editBioActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifi != null && wifi.isConnected()) || (mobileData != null && mobileData.isConnected());
    }
    private void showNoInternetPopUp(){
        postDialog.setContentView(R.layout.custom_no_internet_pop_up);
        postDialog.setCancelable(true);
        Objects.requireNonNull(postDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTimeForRunLoder();
        postDialog.show();
    }
    public void setTimeForRunLoder(){
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

            }
        }, 1500);
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