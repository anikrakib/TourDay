package com.anikrakib.tourday.Activity.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.Profile.MyProfileActivity;
import com.anikrakib.tourday.Models.Profile.Profile;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {
    Button saveOrGetCode;
    LinearLayout resetPassWordLayOut,forgetPasswordLayout;
    TextView resendCode;
    Dialog postDialog ;
    Toolbar toolbarBack;
    String slug;
    EditText emailOrUserName,code,newPassWord,confirmNewPassword;
    TextInputLayout inputLayoutCode, inputLayoutNewPassword,inputLayoutConfirmNewPassword,inputLayoutUserNameOrEmail;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        saveOrGetCode = findViewById(R.id.saveOrGetCodeForgetPasswordButton);
        resetPassWordLayOut = findViewById(R.id.resetPasswordLayout);
        forgetPasswordLayout = findViewById(R.id.forgetPasswordLayout);
        resendCode = findViewById(R.id.resendCodeTextView);
        emailOrUserName = findViewById(R.id.userNameOrEmail);
        code = findViewById(R.id.reset8DigitCode);
        newPassWord = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        inputLayoutCode = findViewById(R.id.input_layout_code);
        inputLayoutConfirmNewPassword = findViewById(R.id.input_layout_confirm_new_password);
        inputLayoutNewPassword = findViewById(R.id.input_layout_new_password);
        inputLayoutUserNameOrEmail = findViewById(R.id.input_layout_username_or_email);
        toolbarBack = findViewById(R.id.toolbarBackForgetPassword);
        constraintLayout = findViewById(R.id.constraintLayout);


        postDialog = new Dialog(this);

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

        setSupportActionBar(toolbarBack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");


        saveOrGetCode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(saveOrGetCode.getText().toString().equals("Get Code")){
                    if(emailOrUserName.getText().toString().isEmpty()){
                        requestFocus(emailOrUserName);
                        inputLayoutUserNameOrEmail.setError("Enter Valid UserName or Valid Email");
                    }else{
                        inputLayoutUserNameOrEmail.setErrorEnabled(false);
                        loader("Get Code");
                    }
                }else{
                    if(code.getText().toString().isEmpty() || code.getText().length() < 8){
                        requestFocus(code);
                        inputLayoutCode.setError("Enter 8 Digit Reset Code");
                    }else{
                        if(newPassWord.getText().toString().isEmpty() || newPassWord.getText().length() < 8){
                            requestFocus(newPassWord);
                            inputLayoutCode.setErrorEnabled(false);
                            inputLayoutNewPassword.setError("Password Must be 8 Character");
                        }else{
                            if(confirmNewPassword.getText().toString().isEmpty() || confirmNewPassword.getText().length() < 8){
                                requestFocus(confirmNewPassword);
                                inputLayoutCode.setErrorEnabled(false);
                                inputLayoutNewPassword.setErrorEnabled(false);
                                inputLayoutConfirmNewPassword.setError("Password Must be 8 Character");
                            }else{
                                if(confirmNewPassword.getText().toString().equals(newPassWord.getText().toString())){
                                    inputLayoutConfirmNewPassword.setErrorEnabled(false);
                                    inputLayoutCode.setErrorEnabled(false);
                                    inputLayoutNewPassword.setErrorEnabled(false);
                                    loader("Save");
                                }else{
                                    requestFocus(newPassWord);
                                    inputLayoutConfirmNewPassword.setError("Password Not Matched");
                                    inputLayoutNewPassword.setError("Password Not Matched");
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("SetTextI18n")
    public void loader(String action){
        postDialog.setContentView(R.layout.gif_view);
        postDialog.setCancelable(false);
        Objects.requireNonNull(postDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(action.equals("Get Code")){
            forgetPassword();
        }else{
            resetPassword(slug);
        }
        postDialog.show();
    }

    public void setTimeForRunLoder(String action){
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            @SuppressLint("SetTextI18n")
            public void run(){
                if(action.equals("Get Code")){
                    forgetPassword();
                }else{
                    saveOrGetCode.setText("Get Code");
                    resetPassWordLayOut.setVisibility(View.GONE);
                    forgetPasswordLayout.setVisibility(View.VISIBLE);
                    resendCode.setVisibility(View.GONE);
                    postDialog.dismiss();
                }
            }
        }, 1000);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void forgetPassword(){
        retrofit2.Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .forgetPassword(emailOrUserName.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.getInt("status") == 404){
                            postDialog.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(constraintLayout, emailOrUserName.getText().toString()+" Not Exist", Snackbar.LENGTH_LONG);
                            snackbar.setTextColor(Color.RED);
                            snackbar.show();
                        }else{
                            slug = jsonObject.getString("slug");

                            saveOrGetCode.setText("Save");
                            resetPassWordLayOut.setVisibility(View.VISIBLE);
                            forgetPasswordLayout.setVisibility(View.GONE);
                            resendCode.setVisibility(View.VISIBLE);
                            postDialog.dismiss();
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetPassword(String slug) {
        retrofit2.Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .resetPassword(slug,code.getText().toString().trim(),newPassWord.getText().toString().trim(),confirmNewPassword.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());

                        if(jsonObject.getInt("status") == 200){

                            postDialog.dismiss();
                            DynamicToast.makeSuccess(getApplicationContext(), "Your password Has been changed!!").show();

                            SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                            boolean isLoggedIn = userPref.getBoolean("isLoggedIn",false);

                            if(isLoggedIn){
                                startActivity(new Intent(ForgetPassword.this, MyProfileActivity.class));
                                finish();
                            }else{
                                startActivity(new Intent(ForgetPassword.this, SignInActivity.class));
                                finish();
                            }
                        }else{
                            postDialog.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(constraintLayout, "Invalid Reset Code !!", Snackbar.LENGTH_LONG);
                            snackbar.setTextColor(Color.RED);
                            snackbar.show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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