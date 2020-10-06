package com.anikrakib.tourday.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anikrakib.tourday.Adapter.AdapterBlog;
import com.anikrakib.tourday.Adapter.AdapterPost;
import com.anikrakib.tourday.Models.BlogItem;
import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Blog extends Fragment {

    private RecyclerView blogRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterBlog mAdapterBlog;
    private ArrayList<BlogItem> mBlogItem;
    private RequestQueue mRequestQueue;
    String url;

    public Blog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog, container, false);

        /////*     initialize view   */////
        blogRecyclerView = v. findViewById(R.id.blogRecyclerView);


        layoutManager = new LinearLayoutManager(getContext());
        blogRecyclerView.setFocusable(false);


        blogRecyclerView.setHasFixedSize(true);
        blogRecyclerView.setLayoutManager(layoutManager);
        blogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBlogItem = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getContext());


        url = "https://tourday.team/api/blog/allpost/";
        parseJSON(url);



        return v;
    }

    private void parseJSON(String url) {
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

                                mBlogItem.add(new BlogItem(imageUrl,blogTitle,blogDescription,blogDivision,date,authorName,id));
                            }
                            if(!nextPage.isEmpty()){
                                parseJSON(nextPage);
                            }
                            mAdapterBlog = new AdapterBlog(getContext(), mBlogItem);
                            blogRecyclerView.setAdapter(mAdapterBlog);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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