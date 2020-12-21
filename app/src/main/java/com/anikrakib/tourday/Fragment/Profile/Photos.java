package com.anikrakib.tourday.Fragment.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.anikrakib.tourday.Adapter.Profile.AdapterGalleryImage;
import com.anikrakib.tourday.Models.Profile.PostItem;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.CheckInternet;
import com.anikrakib.tourday.WebService.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Photos extends Fragment {
    RecyclerView galleryRecyclerView;
    ArrayList<PostItem> postItems;
    AdapterGalleryImage adapterGalleryImage;
    SwipeRefreshLayout swipeRefreshLayout;
    int limit = 0, offSet = 0;
    CardView cardView,notFound;
    boolean firstTime,checkInternet;
    String token,userName;
    TextView emptyPostTextView,emptyPostTextView1,emptyPostTextView2;
    LottieAnimationView lottieAnimationView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_photos, container, false);

        galleryRecyclerView = view.findViewById(R.id.postPhotosRecyclerView);
        swipeRefreshLayout = view. findViewById(R.id.swipeRefreshLayout);
        cardView = view. findViewById(R.id.emptyPostCardView);
        emptyPostTextView = view. findViewById(R.id.emptyPostTextView);
        notFound = view. findViewById(R.id.emptyCardView);
        lottieAnimationView = view. findViewById(R.id.noResultGifView);
//        emptyPostTextView1 = view. findViewById(R.id.emptyPostTextView1);
        emptyPostTextView2 = view. findViewById(R.id.emptyPostTextView2);

        checkInternet = CheckInternet.isConnected(getContext());


        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        //galleryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences userPref = Objects.requireNonNull(requireContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token","");
        firstTime = userPref.getBoolean("firstTime",false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postItems = new ArrayList<>();
                getData();
            }
        });

        postItems = new ArrayList<>();

        if(firstTime){
            setUserName();
        }else{
            getData();
        }

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getData() {
        if(!checkInternet){
            notFound.setVisibility(View.VISIBLE);
            lottieAnimationView.setAnimation(R.raw.no_internet);
            emptyPostTextView2.setText("Check You Data Connection or Wifi Connection");
            emptyPostTextView.setText("Sorry Internet Not Connected");
            swipeRefreshLayout.setRefreshing(false);
        }else {
            SharedPreferences userPref = Objects.requireNonNull(requireContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
            String userName = userPref.getString("userName", "");
            swipeRefreshLayout.setRefreshing(true);
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getPhoto(userName);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            String next = jsonObject.getString("next");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject hit = jsonArray.getJSONObject(i);
                                String post = hit.getString("post");
                                String date = hit.getString("date");
                                String location = hit.getString("location");
                                String imageUrl = hit.getString("image");
                                JSONArray likeArray = hit.getJSONArray("likes");
                                int likeCount = likeArray.length();
                                int user = hit.getInt("user");
                                String id = hit.getString("id");
                                boolean selfLike = false;
                                for (int j = 0; j < likeArray.length(); j++)
                                    if (likeArray.getInt(j) == user) {
                                        selfLike = true;
                                        break;
                                    }

                                postItems.add(new PostItem(imageUrl, post, location, date, likeCount, id, selfLike));
                            }
                            if (!next.isEmpty()) {
                                limit = 10;
                                offSet = 10;
                                getDataNext(limit, offSet, userName);
                            }

                            adapterGalleryImage = new AdapterGalleryImage(getContext(), postItems);
                            galleryRecyclerView.setAdapter(adapterGalleryImage);

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                    checkPostEmptyOrNot();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Fail!", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void getDataFirstTime(String userName) {
        if(!checkInternet){
            notFound.setVisibility(View.VISIBLE);
            lottieAnimationView.setAnimation(R.raw.no_internet);
            emptyPostTextView2.setText("Check You Data Connection or Wifi Connection");
            emptyPostTextView1.setText("Sorry Internet Not Connected");
            swipeRefreshLayout.setRefreshing(false);
        }else {
            swipeRefreshLayout.setRefreshing(true);
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getPhoto(userName);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            String next = jsonObject.getString("next");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject hit = jsonArray.getJSONObject(i);
                                String post = hit.getString("post");
                                String date = hit.getString("date");
                                String location = hit.getString("location");
                                String imageUrl = hit.getString("image");
                                JSONArray likeArray = hit.getJSONArray("likes");
                                int likeCount = likeArray.length();
                                int user = hit.getInt("user");
                                String id = hit.getString("id");
                                boolean selfLike = false;
                                for (int j = 0; j < likeArray.length(); j++)
                                    if (likeArray.getInt(j) == user) {
                                        selfLike = true;
                                        break;
                                    }

                                postItems.add(new PostItem(imageUrl, post, location, date, likeCount, id, selfLike));
                            }
                            if (!next.isEmpty()) {
                                limit = 10;
                                offSet = 10;
                                getDataNext(limit, offSet, userName);
                            }

                            adapterGalleryImage = new AdapterGalleryImage(getContext(), postItems);
                            galleryRecyclerView.setAdapter(adapterGalleryImage);

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                    checkPostEmptyOrNot();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Fail!", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void getDataNext(int l,int o,String userName) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPhotoNext(userName,String.valueOf(l),String.valueOf(o));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        String next = jsonObject.getString("next");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject hit = jsonArray.getJSONObject(i);
                            String post = hit.getString("post");
                            String date = hit.getString("date");
                            String location = hit.getString("location");
                            String imageUrl = hit.getString("image");
                            JSONArray likeArray = hit.getJSONArray("likes");
                            int likeCount = likeArray.length();
                            int user = hit.getInt("user");
                            String id = hit.getString("id");
                            boolean selfLike = false;
                            for (int j = 0; j < likeArray.length(); j++)
                                if (likeArray.getInt(j) == user) {
                                    selfLike = true;
                                    break;
                                }

                            postItems.add(new PostItem(imageUrl, post, location, date, likeCount, id, selfLike));
                        }
                        if(!next.isEmpty()){
                            limit+=10;
                            offSet+=10;
                            getDataNext(limit,offSet,userName);
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(getContext(),"Fail!",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setUserName(){
        SharedPreferences userPref = Objects.requireNonNull(requireContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token "+token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");

                        userName = jsonObject.getString("username");
                        getDataFirstTime(userName);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(),"Token Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(getContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }



    @SuppressLint("SetTextI18n")
    public void checkPostEmptyOrNot(){
        if(postItems.isEmpty()){
            cardView.setVisibility(View.VISIBLE);
            emptyPostTextView.setText("You Have No Photo !!");
        }else{
            cardView.setVisibility(View.GONE);
        }
        swipeRefreshLayout.setRefreshing(false);

    }
}