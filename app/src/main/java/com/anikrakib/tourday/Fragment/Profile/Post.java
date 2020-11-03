package com.anikrakib.tourday.Fragment.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anikrakib.tourday.Adapter.Profile.AdapterPost;
import com.anikrakib.tourday.Models.Profile.PostItem;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.squareup.picasso.Picasso;

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
    Boolean isScrolling = false;
    String token,userName;
    LinearLayoutManager layoutManager;
    boolean firstTime;


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

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setFocusable(false);

        SharedPreferences userPref = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token","");
        firstTime = userPref.getBoolean("firstTime",false);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostItem = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    parseJSON();
            }
        });


        if(firstTime){
            setUserName();
        }else{
            parseJSON();
        }

        return v;
    }

    private void parseJSON() {
        mPostItem = new ArrayList<>();
        SharedPreferences userPref = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        String userName = userPref.getString("userName","");
        swipeRefreshLayout.setRefreshing(true);

        String url = "https://www.tourday.team/api/get_posts/"+userName;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

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
                            mPostAdapter = new AdapterPost(getContext(), mPostItem);
                            recyclerView.setAdapter(mPostAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    private void parseJSONFirstTime(String userName) {
        mPostItem = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);

        String url = "https://www.tourday.team/api/get_posts/"+userName;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

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
                            SharedPreferences userPref = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPref.edit();
                            editor.putBoolean("firstTime",false);
                            editor.apply();
                            mPostAdapter = new AdapterPost(getContext(), mPostItem);
                            recyclerView.setAdapter(mPostAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    public void setUserName(){
        SharedPreferences userPref = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
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
                        parseJSONFirstTime(userName);

                    } catch (JSONException | IOException e) {
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