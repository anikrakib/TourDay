package com.anikrakib.tourday.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.AdapterGalleryImage;
import com.anikrakib.tourday.Adapter.AdapterPost;
import com.anikrakib.tourday.Models.PostItem;
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
import retrofit2.Response;


public class OtherUsersGallery extends Fragment {

    RecyclerView galleryRecyclerView;
    ArrayList<PostItem> postItems = new ArrayList();
    AdapterGalleryImage adapterGalleryImage;
    SwipeRefreshLayout swipeRefreshLayout;


    public OtherUsersGallery() {
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
        View view=  inflater.inflate(R.layout.fragment_other_users_gallery, container, false);

        galleryRecyclerView = view.findViewById(R.id.galleryRecyclerView);
        swipeRefreshLayout = view. findViewById(R.id.gallerySwipeRefreshLayout);

        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        getData();

        return view;
    }

    private void getData() {
        postItems = new ArrayList<>();
        SharedPreferences userPref = Objects.requireNonNull(getContext()).getSharedPreferences("otherUser", Context.MODE_PRIVATE);
        String userName = userPref.getString("otherUsersUserName","");
        swipeRefreshLayout.setRefreshing(true);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPhoto(userName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
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

                        adapterGalleryImage = new AdapterGalleryImage(getContext(),postItems);
                        galleryRecyclerView.setAdapter(adapterGalleryImage);


                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    swipeRefreshLayout.setRefreshing(false);

                }
//
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }
}