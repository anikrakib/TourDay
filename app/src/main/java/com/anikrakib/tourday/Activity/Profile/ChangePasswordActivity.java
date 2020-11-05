package com.anikrakib.tourday.Activity.Profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.anikrakib.tourday.Activity.Authentication.SignInActivity;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kishandonga.csbx.CustomSnackbar;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText confirmOldPassword,newPassword,confirmNewPassword,changePasswordCode;
    Button changeOrOkButton;
    String userOldPassword,userName,slug;
    ConstraintLayout constraintLayout;
    Dialog postDialog, myDialog ;
    LinearLayout confirmOldPasswordLayout,changePasswordLayout;
    TextInputLayout inputLayoutCode, inputLayoutNewPassword,inputLayoutConfirmNewPassword;
    TextView hintChangePassword;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        confirmOldPassword = findViewById(R.id.confirmOldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        changePasswordCode = findViewById(R.id.reset8DigitCode);
        changeOrOkButton = findViewById(R.id.changeOrOkButton);
        constraintLayout = findViewById(R.id.constraintLayout);
        confirmOldPasswordLayout = findViewById(R.id.confirmOldPasswordLayout);
        changePasswordLayout = findViewById(R.id.resetPasswordLayout);
        inputLayoutCode = findViewById(R.id.input_layout_code);
        inputLayoutConfirmNewPassword = findViewById(R.id.input_layout_confirm_new_password);
        inputLayoutNewPassword = findViewById(R.id.input_layout_new_password);
        hintChangePassword = findViewById(R.id.hintChangePassword);

        SharedPreferences userPref = Objects.requireNonNull(getApplicationContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        userOldPassword = userPref.getString("password","");
        userName = userPref.getString("userName","");

        postDialog = new Dialog(this);
        myDialog = new Dialog(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        changeOrOkButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(changeOrOkButton.getText().toString().equals("OK")){
                    if(userOldPassword.equals(confirmOldPassword.getText().toString())){
                        forgetPassword();
                        snackBar("Password Matched !!",R.color.dark_green);
                    }else{
                        snackBar("Password Not Matched",R.color.dark_red);
                    }
                }else{
                    if(changePasswordCode.getText().toString().isEmpty() || changePasswordCode.getText().length() < 8){
                        requestFocus(changePasswordCode);
                        inputLayoutCode.setError("Enter 8 Digit Reset Code");
                    }else{
                        if(newPassword.getText().toString().isEmpty() || newPassword.getText().length() < 8){
                            requestFocus(newPassword);
                            inputLayoutCode.setErrorEnabled(false);
                            inputLayoutNewPassword.setError("Password Must be 8 Character");
                        }else{
                            if(confirmNewPassword.getText().toString().isEmpty() || confirmNewPassword.getText().length() < 8){
                                requestFocus(confirmNewPassword);
                                inputLayoutCode.setErrorEnabled(false);
                                inputLayoutNewPassword.setErrorEnabled(false);
                                inputLayoutConfirmNewPassword.setError("Password Must be 8 Character");
                            }else{
                                if(confirmNewPassword.getText().toString().equals(newPassword.getText().toString())){
                                    inputLayoutConfirmNewPassword.setErrorEnabled(false);
                                    inputLayoutCode.setErrorEnabled(false);
                                    inputLayoutNewPassword.setErrorEnabled(false);
                                    loader("Change Password");
                                }else{
                                    requestFocus(newPassword);
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

    public void forgetPassword(){
        retrofit2.Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .forgetPassword(userName);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.getInt("status") == 404){
                            snackBar("Something Wrong !!",R.color.dark_red);
                        }else{
                            slug = jsonObject.getString("slug");
                            confirmOldPasswordLayout.setVisibility(View.GONE);
                            changePasswordLayout.setVisibility(View.VISIBLE);
                            changeOrOkButton.setText("Change Password");
                            hintChangePassword.setText(R.string.changePasswordHints2);
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
                .resetPassword(slug,changePasswordCode.getText().toString().trim(),newPassword.getText().toString().trim(),confirmNewPassword.getText().toString().trim());
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
                            showLogoutPopUp();
                        }else{
                            snackBar("Invalid Reset Code",R.color.dark_red);
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void loader(String action){
        postDialog.setContentView(R.layout.gif_view);
        postDialog.setCancelable(false);
        Objects.requireNonNull(postDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(action.equals("Change Password")){
            resetPassword(userName);
        }
        postDialog.show();
    }

    public void showLogoutPopUp() {
        Button yesButton,noButton;
        TextView textView;
        SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();

        myDialog.setContentView(R.layout.custom_logout_pop_up);
        yesButton = myDialog.findViewById(R.id.yesButton);
        noButton = myDialog.findViewById(R.id.noButton);
        textView = myDialog.findViewById(R.id.logOutPopUpTextView);

        textView.setText(R.string.changePasswordLogOutHints);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("password",newPassword.getText().toString());
                editor.apply();
                myDialog.dismiss();
                finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("isLoggedIn",false);
                editor.putBoolean("firstTime",false);
                editor.putString("token","");
                editor.putString("userName","");
                editor.putString("userProfilePicture","");
                editor.putString("id","");
                editor.putString("password","");
                editor.apply();
                startActivity(new Intent(ChangePasswordActivity.this, SignInActivity.class));
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    public void snackBar(String text,int color){
        CustomSnackbar sb = new CustomSnackbar(ChangePasswordActivity.this);
        sb.message(text);
        sb.padding(15);
        sb.textColorRes(color);
        sb.backgroundColorRes(R.color.colorPrimaryDark);
        sb.cornerRadius(15);
        sb.duration(Snackbar.LENGTH_LONG);
        sb.show();
    }
}