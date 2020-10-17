package com.anikrakib.tourday.Activity.Blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anikrakib.tourday.Adapter.Blog.AdapterBlog;
import com.anikrakib.tourday.Adapter.Blog.AdapterDivisionBlog;
import com.anikrakib.tourday.Adapter.Profile.AdapterGalleryImage;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.Models.Profile.PostItem;
import com.anikrakib.tourday.R;
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

public class DivisionBlogActivity extends AppCompatActivity {

    Intent intent;
    TextView divisionName;
    ImageButton backButton;
    RecyclerView divisionBlogRecyclerView;
    SwipeRefreshLayout divisionBlogSwipeRefreshLayout;
    ArrayList<BlogItem> mBlogItem;
    AdapterDivisionBlog adapterDivisionBlog;
    private RecyclerView.LayoutManager layoutManager;
    String division;
    int page = 1;
    int limit = 1;
    NestedScrollView nestedScrollView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division_blog);

        divisionName = findViewById(R.id.divisionNameTextView);
        backButton = findViewById(R.id.backDivisionBlogImageButton);
        divisionBlogRecyclerView = findViewById(R.id.divisionBlogRecyclerView);
        divisionBlogSwipeRefreshLayout = findViewById(R.id.divisionBlogItemRefreshLayout);
        //nestedScrollView = findViewById(R.id.nestedScrollView);

        intent = getIntent();
        Bundle extras = intent.getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        assert extras != null;
        division = extras.getString("divisionName");

        assert division != null;
        if(division.equals("Chattogram")){
            division = "Chittagong";
        }

        divisionName.setText(division);

        mBlogItem = new ArrayList<>();


        layoutManager = new LinearLayoutManager(getApplicationContext());
        divisionBlogRecyclerView.setFocusable(false);


        divisionBlogRecyclerView.setHasFixedSize(true);
        divisionBlogRecyclerView.setLayoutManager(layoutManager);
        divisionBlogRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        divisionBlogSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDivisionBlog(division);
                //getAllPost(page,limit);
            }
        });

        getDivisionBlog(division);

        //getAllPost(page,limit);

    }


    private void getDivisionBlog(String divisionName) {
        mBlogItem = new ArrayList<>();
        divisionBlogSwipeRefreshLayout.setRefreshing(true);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDivisionBlog(divisionName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        String nextPage = jsonObject.getString("next");

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

                        adapterDivisionBlog = new AdapterDivisionBlog(DivisionBlogActivity.this, mBlogItem);
                        divisionBlogRecyclerView.setAdapter(adapterDivisionBlog);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    divisionBlogSwipeRefreshLayout.setRefreshing(false);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }
}