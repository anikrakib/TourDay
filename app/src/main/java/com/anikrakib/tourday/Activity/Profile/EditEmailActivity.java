package com.anikrakib.tourday.Activity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEmailActivity extends AppCompatActivity {

    TextView currentEmail;
    EditText newEmail;
    ImageView saveEmailImageButton;
    ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        currentEmail = findViewById(R.id.currentEmailTextView);
        newEmail = findViewById(R.id.editEmailEditText);
        saveEmailImageButton = findViewById(R.id.clickOkEmailImageButton);
        backButton = findViewById(R.id.editEmailBackButton);

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


        newEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //checkInputs();
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        saveEmailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentEmail.getText().toString().equals(newEmail.getText().toString())){
                    DynamicToast.makeError(Objects.requireNonNull(getApplicationContext()), "Enter New Email").show();
                }else{
                    if(validateEmail()){
                        updateEmail();
                        saveEmailImageButton.setEnabled(false);
                        saveEmailImageButton.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getApplicationContext()),R.color.color_secondary_text));
                    }else{
                        DynamicToast.makeError(Objects.requireNonNull(getApplicationContext()), "Enter Valid Email").show();
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        showUserData();

    }

    private void updateEmail() {
        SharedPreferences userPref = Objects.requireNonNull(getApplicationContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateEmail("Token "+token,newEmail.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(Objects.requireNonNull(getApplicationContext()), "Email Update Successfully").show();
                    currentEmail.setText(newEmail.getText().toString());
                    saveEmailImageButton.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getApplicationContext()),R.color.color_secondary_text));
                    newEmail.setText("");
                }else{
                    DynamicToast.makeError(Objects.requireNonNull(getApplicationContext()), "Email Already Exists!").show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateEmail() {
        String email = newEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            saveEmailImageButton.setEnabled(false);
            saveEmailImageButton.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getApplicationContext()),R.color.color_secondary_text));
            return false;
        } else {
            saveEmailImageButton.setEnabled(true);
            saveEmailImageButton.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getApplicationContext()),R.color.black));
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void showUserData() {
        SharedPreferences userPref = Objects.requireNonNull(getApplicationContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token", "");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        currentEmail.setText(profile.getString("email"));

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Token Not Correct", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail!", Toast.LENGTH_LONG).show();

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