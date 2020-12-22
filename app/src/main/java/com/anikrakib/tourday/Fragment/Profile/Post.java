package com.anikrakib.tourday.Fragment.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anikrakib.tourday.Adapter.Profile.AdapterGalleryImage;
import com.anikrakib.tourday.Adapter.Profile.AdapterPost;
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


public class Post extends Fragment {

    private RecyclerView recyclerView;
    private AdapterPost mPostAdapter;
    private ArrayList<PostItem> mPostItem;
    private RequestQueue mRequestQueue;
    SwipeRefreshLayout swipeRefreshLayout;
    String token,userName;
    LinearLayoutManager layoutManager;
    boolean firstTime,checkInternet;
    CardView cardView,notFound;
    LottieAnimationView lottieAnimationView;
    TextView emptyPostTextView1,emptyPostTextView2;
    int limit = 0, offSet = 0;



    public Post() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        /////*     initialize view   */////
        recyclerView = v. findViewById(R.id.postRecyclerView);
        swipeRefreshLayout = v. findViewById(R.id.swipeRefreshLayout);
        cardView = v. findViewById(R.id.emptyPostCardView);
        notFound = v. findViewById(R.id.emptyCardView);
        lottieAnimationView = v. findViewById(R.id.noResultGifView);
        emptyPostTextView1 = v. findViewById(R.id.emptyPostTextView);
        emptyPostTextView2 = v. findViewById(R.id.emptyPostTextView2);

        SharedPreferences userPref = Objects.requireNonNull(requireContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token","");
        firstTime = userPref.getBoolean("firstTime",false);
        userName = userPref.getString("userName","");

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setFocusable(false);
        checkInternet = CheckInternet.isConnected(requireContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mPostItem = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(Objects.requireNonNull(requireContext()));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPostItem = new ArrayList<>();
                getData();
            }
        });


        if(firstTime){
            setUserName();
        }else{
            mPostItem = new ArrayList<>();
            getData();
        }

        return v;
    }

    @SuppressLint("SetTextI18n")
    private void getData() {
        if(!checkInternet){
            notFound.setVisibility(View.VISIBLE);
            lottieAnimationView.setAnimation(R.raw.no_internet);
            emptyPostTextView2.setText("Check You Data Connection or Wifi Connection");
            emptyPostTextView1.setText("Sorry Internet Not Connected");
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
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
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

                                mPostItem.add(new PostItem(imageUrl, post, location, date, likeCount, id, selfLike));
                            }
                            if (!next.isEmpty()) {
                                limit = 10;
                                offSet = 10;
                                getDataNext(limit, offSet, userName);
                            }

                            mPostAdapter = new AdapterPost(getContext(), mPostItem,cardView);
                            recyclerView.setAdapter(mPostAdapter);

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
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
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

                                mPostItem.add(new PostItem(imageUrl, post, location, date, likeCount, id, selfLike));
                            }
                            if (!next.isEmpty()) {
                                limit = 10;
                                offSet = 10;
                                getDataNext(limit, offSet, userName);
                            }

                            mPostAdapter = new AdapterPost(getContext(), mPostItem,cardView);
                            recyclerView.setAdapter(mPostAdapter);

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
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
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

                            mPostItem.add(new PostItem(imageUrl, post, location, date, likeCount, id, selfLike));
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

    public void checkPostEmptyOrNot(){
        if(mPostItem.isEmpty()){
            cardView.setVisibility(View.VISIBLE);
        }else{
            cardView.setVisibility(View.GONE);
        }
        swipeRefreshLayout.setRefreshing(false);

    }

}