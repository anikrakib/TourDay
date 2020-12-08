package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.anikrakib.tourday.Adapter.Blog.AdapterSearchBlogHistory;
import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.Models.Blog.SearchBlogHistory;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    Animation rightToLeft,leftToRight;
    ImageButton backButton;
    ImageView clearSearch;
    SearchView blogSearchView;
    RelativeLayout searchHistoryLayout;
    LinearLayout resultLayout;
    CardView cardView;

    private AdapterSearchBlog adapterSearchBlog;
    private AdapterSearchBlogHistory adapterSearchBlogHistory;
    public List<SearchBlogHistory> searchBlogHistories = new ArrayList<>();
    private List<BlogItem> allBlogResults;
    String url;

    private RequestQueue mRequestQueue;
    RecyclerView blogRecyclerView,searchHistoryRecyclerView;

    @SuppressLint("Assert")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        constraintLayout = findViewById(R.id.constraintLayout);
        backButton = findViewById(R.id.searchBarBackButton);
        blogRecyclerView = findViewById(R.id.blogRecyclerView);
        searchHistoryRecyclerView = findViewById(R.id.blogSearchHistory);
        blogSearchView = findViewById(R.id.blogSearchBar);
        searchHistoryLayout = findViewById(R.id.searchHistoryLayout);
        clearSearch = findViewById(R.id.clearSearch);
        cardView = findViewById(R.id.emptyCardView);
        resultLayout = findViewById(R.id.resultLayout);

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        rightToLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        leftToRight = AnimationUtils.loadAnimation(this, R.anim.left_to_right);

        constraintLayout.startAnimation(rightToLeft);

        searchBlogHistories = readListFromPref(this);

        if (searchBlogHistories == null){
            searchBlogHistories = new ArrayList<>();
            searchHistoryLayout.setVisibility(View.GONE);
        }

        //assert false;
            //searchBlogHistories.add(new SearchBlogHistory("null"));

        // search blog history recycler view setup
        blogRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        blogRecyclerView.setLayoutManager(layoutManager);
        blogRecyclerView.setAdapter(adapterSearchBlog);

        // search blog history recycler view setup
        LinearLayoutManager layoutManagerSearchHistory = new LinearLayoutManager(getApplicationContext());
        searchHistoryRecyclerView.setLayoutManager(layoutManagerSearchHistory);
        searchHistoryRecyclerView.setHasFixedSize(true);
        adapterSearchBlogHistory = new AdapterSearchBlogHistory(getApplicationContext(), searchBlogHistories, blogSearchView);
        searchHistoryRecyclerView.setAdapter(adapterSearchBlogHistory);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.left_to_right);
            }
        });

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllListInPref(SearchActivity.this);
                searchHistoryLayout.setVisibility(View.GONE);
            }
        });

        blogSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //SearchBlogHistory searchBlogHistory = new SearchBlogHistory("query");
                if (searchBlogHistories == null) {
                    searchBlogHistories = new ArrayList<>();
                    searchBlogHistories.add(new SearchBlogHistory(query));
                    writeListInPref(SearchActivity.this, searchBlogHistories);
                    adapterSearchBlogHistory.setTaskModelList(searchBlogHistories);
                }else{
                    blogSearchView.clearFocus();
                    searchBlogHistories.add(new SearchBlogHistory(query));
                    writeListInPref(SearchActivity.this, searchBlogHistories);
                    Collections.reverse(searchBlogHistories);
                    adapterSearchBlogHistory.setTaskModelList(searchBlogHistories);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                if(newText.isEmpty()){
                    resultLayout.setVisibility(View.GONE);
                    searchHistoryLayout.setVisibility(View.VISIBLE);

                }else {
                    allBlogResults = new ArrayList<>();
                    resultLayout.setVisibility(View.VISIBLE);
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
                            adapterSearchBlog = new AdapterSearchBlog(SearchActivity.this, (ArrayList<BlogItem>) allBlogResults, blogSearchView, searchBlogHistories);
                            blogRecyclerView.setAdapter(adapterSearchBlog);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        checkFoundOrNot();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    public void writeListInPref(Context context, List<SearchBlogHistory> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);
        SharedPreferences user = context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = user.getString("userName","");
        SharedPreferences userPref = context.getSharedPreferences(userName, MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString("searchHistoryKey", jsonString);
        editor.apply();
    }

    public List<SearchBlogHistory> readListFromPref(Context context) {
        SharedPreferences user = context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = user.getString("userName","");
        SharedPreferences userPref = context.getSharedPreferences(userName, Context.MODE_PRIVATE);
        String jsonString = userPref.getString("searchHistoryKey", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<SearchBlogHistory>>() {}.getType();
        return gson.fromJson(jsonString, type);
    }

    public void removeAllListInPref(Context context) {
        SharedPreferences user = context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = user.getString("userName","");
        SharedPreferences userPref = context.getSharedPreferences(userName, MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.remove("searchHistoryKey");
        editor.apply();
    }

    public void checkFoundOrNot(){
        if(allBlogResults.isEmpty()){
            cardView.setVisibility(View.VISIBLE);
            blogRecyclerView.setVisibility(View.GONE);
        }else{
            cardView.setVisibility(View.GONE);
            blogRecyclerView.setVisibility(View.VISIBLE);

        }
    }
    // this method will load the Night Mode State
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }
    public Boolean loadNightModeState (){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("nightMode", Context.MODE_PRIVATE);
        return userPref.getBoolean("night_mode",false);
    }

}