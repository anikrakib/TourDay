package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Models.Token;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    Toolbar toolbarBack;
    TextView signUp;
    private EditText inputUsername, inputPassword;
    private TextInputLayout inputLayoutUsername, inputLayoutPassword;
    Dialog postDialog ;
    Button signInButton;
    private static String token;



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



        postDialog = new Dialog(this);

        setSupportActionBar(toolbarBack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
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

    private void submitForm() {
        if (!validateUsername()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        loader();

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

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_username:
                    validateUsername();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }

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

    private boolean validateUsername() {
        if (inputUsername.getText().toString().trim().isEmpty()) {
            inputLayoutUsername.setError(getString(R.string.err_msg_userName1));
            requestFocus(inputUsername);
            return false;
        }
        else {
            inputLayoutUsername.setErrorEnabled(false);
        }

        return true;
    }

    public void loader(){
        postDialog.setContentView(R.layout.gif_view);
        postDialog.setCancelable(false);
        postDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTimeForRunLoder();
        postDialog.show();
        if(validateEmail()){
            accessDataUsingEmail();
        }else {
            accessDataUserName();
        }

    }

    private void accessDataUsingEmail() {
        setTimeForRunLoder();
        Call<Token> call = RetrofitClient
                .getInstance()
                .getApi()
                .logInUsingUserName(inputUsername.getText().toString().trim(),inputPassword.getText().toString().trim());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getApplicationContext(), "Login Success").show();
                    //make shared preference user
                    SharedPreferences userPref =getApplicationContext().getSharedPreferences("user",getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",response.body().getKey());
                    editor.putBoolean("isLoggedIn",true);
                    editor.putString("username",inputUsername.getText().toString().trim());
                    editor.apply();
                    startActivity(new Intent(SignInActivity.this, MyProfileActivity.class));
                    token=response.body().getKey();
                    finish();
                }else{
                    try {
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
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void accessDataUserName() {
        setTimeForRunLoder();
        Call<Token> call = RetrofitClient
                .getInstance()
                .getApi()
                .logInUsingUserName(inputUsername.getText().toString().trim(),inputPassword.getText().toString().trim());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getApplicationContext(), "Login Success").show();
                    //make shared preference user
                    SharedPreferences userPref =getApplicationContext().getSharedPreferences("user",getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",response.body().getKey());
                    editor.putBoolean("isLoggedIn",true);
                    editor.putString("username",inputUsername.getText().toString().trim());
                    editor.apply();
                    startActivity(new Intent(SignInActivity.this, MyProfileActivity.class));
                    token=response.body().getKey();
                    finish();
                }else{
                    try {
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
            public void onFailure(Call<Token> call, Throwable t) {
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

        if (email.isEmpty() || !isValidEmail(email)) {
            return false;
        } else {
            return true;
        }

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static String getToken() {
        return token;
    }
}
