package com.anikrakib.tourday.Activity.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.Profile.MyProfileActivity;
import com.anikrakib.tourday.Models.Profile.Token;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kishandonga.csbx.CustomSnackbar;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public static final Pattern USER_NAME = Pattern.compile("^([a-z])+([\\w.]{2,})+$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
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

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            finish();
        });

        forgetPassword.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this,ForgetPassword.class)));

        signInButton.setOnClickListener((View v) -> {
            submitForm();
        });

    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void submitForm() {
        if (!validateUserNameOrEmail()) {
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
    public void setTimeForRunLoader(String toast, boolean succes){
        Handler handler;
        handler = new Handler();
        handler.postDelayed(() -> {
            try {
                if(succes){
                    DynamicToast.makeSuccess(getApplicationContext(), toast).show();
                }else {
                    inputUsername.setText("");
                    inputPassword.setText("");
                    requestFocus(inputUsername);
                    snackBar(toast,R.color.dark_red);
                }
                postDialog.dismiss();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }, 1500);
    }

    private boolean validateUserNameOrEmail() {
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
        postDialog.show();
        if(validateEmail()) accessDataUsingEmail();
        else accessDataUserName();
    }

    private void accessDataUsingEmail() {
        if(isConnected(SignInActivity.this)){
            showNoInternetPopUp();
        }else{
            //setTimeForRunLoder();
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
                        editor.putString("password",getHash(inputPassword.getText().toString()));
                        editor.apply();
                        startActivity(new Intent(SignInActivity.this, MyProfileActivity.class));
//                        DynamicToast.makeSuccess(getApplicationContext(), "Login Success").show();
                        setTimeForRunLoader("Log In Success",true);
                        finish();
                    }else{
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            //DynamicToast.makeError(getApplicationContext(), jObjError.getJSONArray("non_field_errors").getString(0)).show();
                            setTimeForRunLoader(jObjError.getJSONArray("non_field_errors").getString(0),false);
                        } catch (Exception e) {
                            snackBar(e.getMessage(),R.color.dark_red);
                            postDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                    snackBar(t.getMessage(),R.color.dark_red);
                    postDialog.dismiss();
                }
            });
        }
    }

    private void accessDataUserName() {
        if(isConnected(SignInActivity.this)){
            showNoInternetPopUp();
        }else{
            Call<Token> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .logInUsingUserName(inputUsername.getText().toString().trim(),inputPassword.getText().toString().trim());
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
                        editor.putString("password",getHash(inputPassword.getText().toString()));
                        editor.apply();
                        startActivity(new Intent(SignInActivity.this, MyProfileActivity.class));
                        setTimeForRunLoader("Log In Success", true);
                        finish();
                    }else{
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            //DynamicToast.makeError(getApplicationContext(), jObjError.getJSONArray("non_field_errors").getString(0)).show();
                            setTimeForRunLoader(jObjError.getJSONArray("non_field_errors").getString(0), false);
                        } catch (Exception e) {
                            snackBar(e.getMessage(),R.color.dark_red);
                            postDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                    snackBar(t.getMessage(),R.color.dark_red);
                    postDialog.dismiss();
                }
            });
        }
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

        return (wifi == null || !wifi.isConnected()) && (mobileData == null || !mobileData.isConnected());
    }
    private void showNoInternetPopUp(){
        postDialog.setContentView(R.layout.custom_no_internet_pop_up);
        postDialog.setCancelable(true);
        Objects.requireNonNull(postDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        postDialog.show();
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
    public void snackBar(String text,int color){
        CustomSnackbar sb = new CustomSnackbar(SignInActivity.this);
        sb.message(text);
        sb.padding(15);
        sb.textColorRes(color);
        sb.backgroundColorRes(R.color.colorPrimaryDark);
        sb.cornerRadius(15);
        sb.duration(Snackbar.LENGTH_LONG);
        sb.show();
    }
}
