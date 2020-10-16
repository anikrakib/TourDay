package com.anikrakib.tourday.Fragment.Blog;

import android.content.Context;
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
import com.anikrakib.tourday.Adapter.Blog.AdapterYourBlog;
import com.anikrakib.tourday.Models.Blog.YourBlogItem;
import com.anikrakib.tourday.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YourBlog extends Fragment {

    private RecyclerView yourBlogRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterYourBlog adapterYourBlog;
    private ArrayList<YourBlogItem> yourBlogItems;
    private RequestQueue mRequestQueue;
    private SwipeRefreshLayout yourBlogRefreshLayout;
    String url;


    public YourBlog() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_your_blog, container, false);

        /////*     initialize view   */////
        yourBlogRecyclerView = v. findViewById(R.id.yourBlogRecyclerView);
        yourBlogRefreshLayout = v. findViewById(R.id.yourBlogRefreshLayout);


        layoutManager = new LinearLayoutManager(getContext());
        yourBlogRecyclerView.setFocusable(false);


        yourBlogRecyclerView.setHasFixedSize(true);
        yourBlogRecyclerView.setLayoutManager(layoutManager);
        yourBlogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        yourBlogItems = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getContext());


        // Using SharedPreferences collect user name from SharedPreferences

        SharedPreferences userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = userPref.getString("userName","");

        yourBlogRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                url = "https://tourday.team/api/blog/user/"+username;
                yourBlogItems = new ArrayList<>();
                parseJSON(url);
            }
        });

        url = "https://tourday.team/api/blog/user/"+username;
        parseJSON(url);



        return v;
    }

    private void parseJSON(String url) {
        yourBlogRefreshLayout.setRefreshing(true);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            String nextPage = response.getString("next");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String id = hit.getString("id");
                                String authorName = hit.getString("slug");
                                String blogTitle = hit.getString("title");
                                String date = hit.getString("date");
                                String blogDescription = hit.getString("description");
                                String imageUrl = hit.getString("image");
                                String blogDivision = hit.getString("division");

                                yourBlogItems.add(new YourBlogItem(imageUrl,blogTitle,blogDescription,blogDivision,date,authorName,id));
                            }
                            if(!nextPage.isEmpty()){
                                parseJSON(nextPage);
                            }
                            adapterYourBlog = new AdapterYourBlog(getContext(), yourBlogItems);
                            yourBlogRecyclerView.setAdapter(adapterYourBlog);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        yourBlogRefreshLayout.setRefreshing(false);
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