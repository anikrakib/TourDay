package com.anikrakib.tourday.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anikrakib.tourday.Adapter.AdapterPost;
import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Post extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterPost mPostAdapter;
    private ArrayList<PostItem> mPostItem;
    private RequestQueue mRequestQueue;
    SwipeRefreshLayout swipeRefreshLayout;


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


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostItem = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    parseJSON();

            }


        });


        parseJSON();


        return v;
    }

    private void parseJSON() {
        mPostItem = new ArrayList<>();
        SharedPreferences userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userName = userPref.getString("userName","");
        swipeRefreshLayout.setRefreshing(true);

        String url = "https://tourday.team/api/get_posts/"+userName;

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


}