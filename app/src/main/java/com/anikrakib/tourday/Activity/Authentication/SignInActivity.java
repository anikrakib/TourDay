package com.anikrakib.tourday.Activity.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.anikrakib.tourday.Activity.Profile.MyProfileActivity;
import com.anikrakib.tourday.Models.Profile.Token;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity {

    Toolbar toolbarBack;
    TextView signUp,forgetPassword;
    private EditText inputUsername, inputPassword;
    private TextInputLayout inputLayoutUsername, inputLayoutPassword;
    Dialog postDialog ;
    Button signInButton;
    private static String token;
    public static final Pattern USER_NAME = Pattern.compile("^([a-z])+([\\w.]{2,})+$");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setTitle("");

        toolbarBack = findViewById(R.id.toolbarBack);
        signUp = findViewById(R.id.signUpTextView);
        inputLayoutUsername = findViewById(R.id.input_layout_username);
        inputLayoutPassword = findViewById(R.id.input_layout_password);
        inputUsername = findViewById(R.id.userNameSignIn);
        inputPassword = findViewById(R.id.passwordSignIn);
        signInButton = findViewById(R.id.signInButton);
        forgetPassword = findViewById(R.id.forgetPasswordTextView);

        postDialog = new Dialog(this);


        setSupportActionBar(toolbarBack);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,ForgetPassword.class));
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isConnected(SignInActivity.this)){
//                    submitForm();
//                }else{
//                    showNoInternetPopUp();
//                }
                submitForm();
            }
        });

    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void submitForm() {
        if (!validateUsernameorEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        loader();

    }

    private boolean validatePassword() {
        if(inputPassword.getText().toString().length() <=7) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }
    public void setTimeForRunLoder(){
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

            }
        }, 1500);
    }

    private boolean validateUsernameorEmail() {
        if (!(isValidUserName() || validateEmail())) {
            inputLayoutUsername.setError(getString(R.string.err_msg_userName1));
            requestFocus(inputUsername);
            return true;
        }
        else {
            inputLayoutUsername.setErrorEnabled(false);
        }

        return true;
    }
    public boolean isValidUserName(){
        Matcher matcher = USER_NAME.matcher(inputUsername.getText().toString());
        return matcher.matches();
    }

    public void loader(){
        postDialog.setContentView(R.layout.gif_view);
        postDialog.setCancelable(false);
        Objects.requireNonNull(postDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTimeForRunLoder();
        postDialog.show();

        if(validateEmail()) accessDataUsingEmail();
        else accessDataUserName();
    }

    private void accessDataUsingEmail() {
        if(!isConnected(SignInActivity.this)){
            showNoInternetPopUp();
            return;
        }else{
            setTimeForRunLoder();
        }
        Call<Token> call = RetrofitClient
                .getInstance()
                .getApi()
                .logInUsingEmail(inputUsername.getText().toString().trim(),inputPassword.getText().toString().trim());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                if(response.isSuccessful()){
                    //make shared preference user
                    SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    assert response.body() != null;
                    editor.putString("token",response.body().getKey());
                    editor.putBoolean("isLoggedIn",true);
                    editor.putBoolean("firstTime",true);
                    editor.putString("password",inputPassword.getText().toString());
                    editor.apply();
                    startActivity(new Intent(SignInActivity.this, MyProfileActivity.class));
                    DynamicToast.makeSuccess(getApplicationContext(), "Login Success").show();
                    token=response.body().getKey();
                    finish();
                }else{
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        DynamicToast.makeError(getApplicationContext(), jObjError.getJSONArray("non_field_errors").getString(0)).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    inputUsername.setText("");
                    inputPassword.setText("");
                    requestFocus(inputUsername);
                    postDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void accessDataUserName() {
        if(!isConnected(SignInActivity.this)){
            showNoInternetPopUp();
            return;
        }else{
            setTimeForRunLoder();
        }
        Call<Token> call = RetrofitClient
                .getInstance()
                .getApi()
                .logInUsingUserName(inputUsername.getText().toString().trim(),inputPassword.getText().toString().trim());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getApplicationContext(), "Login Success").show();
                    //make shared preference user
                    SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    assert response.body() != null;
                    editor.putString("token",response.body().getKey());
                    editor.putBoolean("isLoggedIn",true);
                    editor.putBoolean("firstTime",true);
                    editor.putString("password",inputPassword.getText().toString());
                    editor.apply();
                    startActivity(new Intent(SignInActivity.this, MyProfileActivity.class));
                    token=response.body().getKey();
                    finish();
                }else{
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        DynamicToast.makeError(getApplicationContext(), jObjError.getJSONArray("non_field_errors").getString(0)).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    inputUsername.setText("");
                    inputPassword.setText("");
                    requestFocus(inputUsername);
                    postDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateEmail() {
        String email = inputUsername.getText().toString().trim();

        return !email.isEmpty() && isValidEmail(email);

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isConnected(SignInActivity signInActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) signInActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
