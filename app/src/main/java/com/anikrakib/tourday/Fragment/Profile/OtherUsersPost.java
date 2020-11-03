package com.anikrakib.tourday.Fragment.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anikrakib.tourday.Adapter.Profile.OtherUserAdapterPost;
import com.anikrakib.tourday.Models.Profile.PostItem;
import com.anikrakib.tourday.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class OtherUsersPost extends Fragment {

    private RecyclerView otherUsersPostRecyclerView;
    private OtherUserAdapterPost mPostAdapter;
    private ArrayList<PostItem> mPostItem;
    private RequestQueue mRequestQueue;
    SwipeRefreshLayout swipeRefreshLayout;
    String url = "https://www.tourday.team/api/get_posts/";
    Dialog myDialog;


    public OtherUsersPost() {
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
        View view = inflater.inflate(R.layout.fragment_other_users_post, container, false);


        /////*     initialize view   */////
        otherUsersPostRecyclerView = view. findViewById(R.id.otherUsersPostRecyclerView);
        swipeRefreshLayout = view. findViewById(R.id.swipeRefreshLayoutOthersUsers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        otherUsersPostRecyclerView.setFocusable(false);

        myDialog = new Dialog(Objects.requireNonNull(getContext()));

        SharedPreferences userPref = getContext().getSharedPreferences("otherUser", Context.MODE_PRIVATE);
        String userName = userPref.getString("otherUsersUserName","");


        otherUsersPostRecyclerView.setHasFixedSize(true);
        otherUsersPostRecyclerView.setLayoutManager(layoutManager);
        otherUsersPostRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostItem = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPostItem = new ArrayList<>();
                getOtherUserPost(url+userName);
            }


        });



        getOtherUserPost(url+userName);


        return view;
    }

    private void getOtherUserPost(String url) {
        swipeRefreshLayout.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            String nextPage = response.getString("next");
                            SharedPreferences userPref = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
                            String id = userPref.getString("id","");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject hit = jsonArray.getJSONObject(i);
                                String post = hit.getString("post");
                                String date = hit.getString("date");
                                String location = hit.getString("location");
                                String imageUrl = hit.getString("image");
                                JSONArray likeArray = hit.getJSONArray("likes");
                                int likeCount = likeArray.length();
                                String postId = hit.getString("id");
                                boolean selfLike = false;
                                for (int j = 0; j < likeArray.length(); j++)
                                    if (likeArray.getInt(j) == Integer.parseInt(id)) {
                                        selfLike = true;
                                        break;
                                    }

                                mPostItem.add(new PostItem(imageUrl, post, location, date, likeCount, postId, selfLike));
                            }
                            if(!nextPage.isEmpty()){
                                getOtherUserPost(nextPage);
                            }
                            mPostAdapter = new OtherUserAdapterPost(getContext(), mPostItem);
                            otherUsersPostRecyclerView.setAdapter(mPostAdapter);
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


}