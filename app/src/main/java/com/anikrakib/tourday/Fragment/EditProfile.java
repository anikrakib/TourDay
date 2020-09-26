package com.anikrakib.tourday.Fragment;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.ExploreActivity;
import com.anikrakib.tourday.Activity.LocationActivity;
import com.anikrakib.tourday.Activity.SignInActivity;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.uncopt.android.widget.text.justify.JustifiedEditText;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfile extends Fragment {

    TextView userEmailTextView,userLocationTextView,userNameTextView;
    EditText userEmailEditText;
    ImageButton saveEmailImageButton,saveBioImageButton;
    LinearLayout userEmailLayout,editBioLayout;
    Intent intent;
    JustifiedTextView aboutBio;
    JustifiedEditText aboutBioEdit;

    public EditProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userEmailLayout = view.findViewById(R.id.editEmailLayout);
        editBioLayout = view.findViewById(R.id.editBioLayout);
        userEmailTextView = view.findViewById(R.id.editEmailTextView);
        userLocationTextView = view.findViewById(R.id.editLocationTextView);
        userEmailEditText = view.findViewById(R.id.editEmailEditText);
        saveEmailImageButton = view.findViewById(R.id.clickOkEmailImageButton);
        saveBioImageButton = view.findViewById(R.id.clickOkBioImageButton);
        userNameTextView = view.findViewById(R.id.editUsernameTextView);
        aboutBio = view.findViewById(R.id.aboutBio);
        aboutBioEdit = view.findViewById(R.id.aboutBioEdit);


        showUserData();

        userEmailEditText.addTextChangedListener(new TextWatcher() {
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
        aboutBioEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBioImageButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userEmailTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                userEmailLayout.setVisibility(View.VISIBLE);
                userEmailEditText.setText(userEmailTextView.getText());
                userEmailTextView.setVisibility(View.GONE);
                return true;
            }
        });
        saveEmailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail()){
                    updateEmail();
                    userEmailTextView.setVisibility(View.VISIBLE);
                    userEmailLayout.setVisibility(View.GONE);
                    saveEmailImageButton.setEnabled(false);
                    saveEmailImageButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.color_secondary_text));
                }else{
                    DynamicToast.makeError(getContext(), "Enter Valid Email").show();
                }
            }
        });

        userLocationTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                intent = new Intent(getActivity(),LocationActivity.class);
                intent.putExtra("recentLocation",userLocationTextView.getText());
                getActivity().startActivity(intent);
                return true;
            }
        });
        aboutBio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aboutBio.setVisibility(View.GONE);
                editBioLayout.setVisibility(View.VISIBLE);
                return true;
            }
        });
        userEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getContext(), "Press Long Click To Edit Email").show();
            }
        });
        userLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getContext(), "Press Long Click To Edit Location").show();
            }
        });
        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getContext(), "Username Can't be Changed").show();
            }
        });
        aboutBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getContext(), "Press Long Click To Edit Bio").show();
            }
        });

        return view;

    }

    public void showUserData(){
        SharedPreferences userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token "+ token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        userEmailTextView.setText(profile.getString("email"));
                        userLocationTextView.setText(profile.getString("city"));
                        aboutBio.setText(profile.getString("bio"));
                        userNameTextView.setText(jsonObject.getString("username"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(),"Token Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updateEmail() {
        SharedPreferences userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateEmail("Token "+token,userEmailEditText.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getContext(), "Email Update Successfully").show();
                    userEmailTextView.setText(userEmailEditText.getText().toString());
                }else{
                    DynamicToast.makeError(getContext(), "Email Already Exists!").show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateEmail() {
        String email = userEmailEditText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            saveEmailImageButton.setEnabled(false);
            saveEmailImageButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.color_secondary_text));
            return false;
        } else {
            saveEmailImageButton.setEnabled(true);
            saveEmailImageButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}