package com.anikrakib.tourday.Activity.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Activity.Profile.MyProfileActivity;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    GifDrawable gifDrawable;
    GifImageView gifImageView;
    LinearLayout signUpBody;
    Button signUpButton;
    private EditText inputUsername, inputEmail, inputPassword;
    private TextInputLayout inputLayoutUsername, inputLayoutEmail, inputLayoutPassword;
    Dialog postDialog ;
    Toolbar toolbarBack;
    TextView signIn;
    public static final Pattern USER_NAME = Pattern.compile("^([a-z])+([\\w.]{2,})+$");



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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

        setTitle("");

        gifImageView = findViewById(R.id.loadingGif);
        signUpBody = findViewById(R.id.signUpBody);
        signUpButton = findViewById(R.id.signUpButton);
        inputLayoutUsername = findViewById(R.id.input_layout_username);
        inputLayoutEmail = findViewById(R.id.input_layout_email);
        inputLayoutPassword = findViewById(R.id.input_layout_password);
        inputUsername = findViewById(R.id.userNameSignUp);
        inputEmail = findViewById(R.id.emailSignUp);
        inputPassword = findViewById(R.id.passwordSignUp);


        inputUsername.addTextChangedListener(new MyTextWatcher(inputUsername));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        toolbarBack = findViewById(R.id.toolbarBack);
        signIn = findViewById(R.id.signInTextView);


        postDialog = new Dialog(this);

        //gifImageView.setVisibility(View.GONE);


        setSupportActionBar(toolbarBack);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void activity(){
        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
        startActivity(intent);
        finish();

    }


    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateUsername()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        loder();

    }

    public void loder(){
        sendData();
        postDialog.setContentView(R.layout.gif_view);
        postDialog.setCancelable(false);
        Objects.requireNonNull(postDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        postDialog.show();
    }

    private void sendData(){
        if(!isConnected(SignUpActivity.this)){
            showNoInternetPopUp();
            return;
        }else{
            setTimeForRunLoder();
        }
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(inputUsername.getText().toString().trim(),inputEmail.getText().toString().trim(),inputPassword.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        String token = jsonObject.getString("token");
                        //make shared preference user
                        SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("token",token);
                        editor.putBoolean("isLoggedIn",true);
                        editor.putString("password",getHash(inputPassword.getText().toString()));
                        editor.apply();
                        DynamicToast.makeSuccess(getApplicationContext(), "Registration Success").show();
                        startActivity(new Intent(SignUpActivity.this, MyProfileActivity.class));
                        finish();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        DynamicToast.makeError(getApplicationContext(), jObjError.getJSONArray("username").getString(0)).show();
                        inputUsername.setText("");
                        requestFocus(inputUsername);
                    } catch (Exception e) {
                        DynamicToast.makeError(getApplicationContext(),"Email Already Used!").show();
                        requestFocus(inputEmail);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }


    public void setTimeForRunLoder(){
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                postDialog.cancel();
                postDialog.dismiss();
            }
        }, 2000);
    }

    private boolean validateUsername() {
        if (!isValidUserName()) {
            inputLayoutUsername.setError(getString(R.string.err_msg_userName2));
            requestFocus(inputUsername);
            return false;
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

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }




    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @SuppressLint("NonConstantResourceId")
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.userNameSignUp:
                    validateUsername();
                    break;
                case R.id.emailSignUp:
                    validateEmail();
                    break;
                case R.id.passwordSignUp:
                    validatePassword();
                    break;
            }
        }

    }

    private boolean isConnected(SignUpActivity signUpActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) signUpActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
