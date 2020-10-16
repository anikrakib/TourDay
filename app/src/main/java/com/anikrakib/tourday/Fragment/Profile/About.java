package com.anikrakib.tourday.Fragment.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class About extends Fragment {

    RecyclerView recyclerView;
    TextView userEmail,userUserName,userLocation,userBio;
    String token;


    public About() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);
        userEmail = view.findViewById(R.id.email);
        userLocation = view.findViewById(R.id.location);
        userUserName = view.findViewById(R.id.userName);
        userBio = view.findViewById(R.id.userBio);

        SharedPreferences userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token","");

        showUserData();


        return view;
    }

    public void showUserData(){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token "+ token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    //DynamicToast.makeError(getApplicationContext(), "Login Success").show();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        //save Username for changing menu item profile name
                        SharedPreferences userPref =getContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("userName",jsonObject.getString("username"));
                        editor.apply();
                        userEmail.setText(profile.getString("email"));
                        userLocation.setText(profile.getString("city"));
                        userBio.setText(profile.getString("bio"));
                        userUserName.setText(jsonObject.getString("username"));


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
}