package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anikrakib.tourday.Adapter.Blog.AdapterAllBlog;
import com.anikrakib.tourday.Adapter.Blog.AdapterSearchBlog;
import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    Animation rightToLeft,leftToRight;
    ImageButton backButton;
    SearchView blogSearchView;
    LinearLayout searchHistoryLayout;

    private AdapterSearchBlog adapterSearchBlog;
    List<BlogItem> allBlogResults;
    String url;

    private RequestQueue mRequestQueue;
    RecyclerView blogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        constraintLayout = findViewById(R.id.constraintLayout);
        backButton = findViewById(R.id.searchBarBackButton);
        blogRecyclerView = findViewById(R.id.blogRecyclerView);
        blogSearchView = findViewById(R.id.blogSearchBar);
        searchHistoryLayout = findViewById(R.id.searchHistoryLayout);

        rightToLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        leftToRight = AnimationUtils.loadAnimation(this, R.anim.left_to_right);

        constraintLayout.startAnimation(rightToLeft);

        blogRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        blogRecyclerView.setLayoutManager(layoutManager);
        blogRecyclerView.setAdapter(adapterSearchBlog);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.left_to_right);
            }
        });

        blogSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                if(newText.isEmpty()){
                    blogRecyclerView.setVisibility(View.GONE);
                    searchHistoryLayout.setVisibility(View.VISIBLE);

                }else {
                    allBlogResults = new ArrayList<>();
                    blogRecyclerView.setVisibility(View.VISIBLE);
                    searchHistoryLayout.setVisibility(View.GONE);
                    url = "https://www.tourday.team/api/blog/query?search="+newText;
                    searchBlog(url);
                }
                return true;
            }
        });

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.left_to_right);
    }


    private void searchBlog(String url) {
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

                                allBlogResults.add(new BlogItem(imageUrl,blogTitle,blogDescription,blogDivision,date,authorName,id));
                            }
                            if(!nextPage.isEmpty()){
                                searchBlog(nextPage);
                            }
                            adapterSearchBlog = new AdapterSearchBlog(SearchActivity.this, (ArrayList<BlogItem>) allBlogResults);
                            blogRecyclerView.setAdapter(adapterSearchBlog);
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