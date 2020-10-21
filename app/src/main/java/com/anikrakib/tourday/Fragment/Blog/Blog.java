package com.anikrakib.tourday.Fragment.Blog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.anikrakib.tourday.Adapter.Blog.AdapterBlog;
import com.anikrakib.tourday.Adapter.Blog.AdapterDivisionBlog;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Blog extends Fragment {

    private RecyclerView blogRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterBlog mAdapterBlog;
    private ArrayList<BlogItem> mBlogItem;
    private RequestQueue mRequestQueue;
    private SwipeRefreshLayout blogRefreshLayout;
    String url;
    private List<String> lastSearches;
    private MaterialSearchBar searchBar;
    boolean search = false;
    int page = 1;

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
        blogRefreshLayout = v. findViewById(R.id.blogRefreshLayout);

        searchBar = (MaterialSearchBar) v.findViewById(R.id.blogSearchBar);
        searchBar.setHint("Search blog ...");
        searchBar.setSearchIcon(R.drawable.ic_search_black_24dp);
        searchBar.setSpeechMode(true);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                url = "https://www.tourday.team/api/blog/query?search="+searchBar.getText();
                mBlogItem = new ArrayList<>();
                searchBlog(url);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        mBlogItem = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getContext());
        blogRecyclerView.setFocusable(false);

        blogRecyclerView.setHasFixedSize(true);
        blogRecyclerView.setLayoutManager(layoutManager);
        blogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRequestQueue = Volley.newRequestQueue(getContext());

        blogRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mBlogItem = new ArrayList<>();
                page = 1;
                getAllPost(page);
            }
        });

        getAllPost(page);

        return v;
    }


    private void getAllPost(int page) {
        blogRefreshLayout.setRefreshing(true);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllBlogPost(page);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        String nextPage = jsonObject.getString("next");

                        result(jsonArray,nextPage);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"Fail!",Toast.LENGTH_LONG).show();

            }

        });

    }

    private void result(JSONArray jsonArray, String nextPage) {

        try {

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
                page++;
                getAllPost(page);
            }

            mAdapterBlog = new AdapterBlog(getContext(), mBlogItem);
            blogRecyclerView.setAdapter(mAdapterBlog);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        blogRefreshLayout.setRefreshing(false);

    }

    private void searchBlog(String url) {
        blogRefreshLayout.setRefreshing(true);
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
                                search = true;

                                mBlogItem.add(new BlogItem(imageUrl,blogTitle,blogDescription,blogDivision,date,authorName,id,search));
                            }
                            if(!nextPage.isEmpty()){
                                searchBlog(nextPage);
                            }
                            mAdapterBlog = new AdapterBlog(getContext(), mBlogItem);
                            blogRecyclerView.setAdapter(mAdapterBlog);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        blogRefreshLayout.setRefreshing(false);
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