package com.anikrakib.tourday.Fragment.Blog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
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
import com.anikrakib.tourday.Adapter.Blog.AdapterAllBlog;
import com.anikrakib.tourday.Adapter.Blog.AdapterBlog;
import com.anikrakib.tourday.Adapter.Blog.AdapterDivisionBlog;
import com.anikrakib.tourday.Adapter.Event.AdapterAllEvent;
import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.Models.Event.AllEventResponse;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Blog extends Fragment {

    private LinearLayoutManager layoutManager;
    private AdapterAllBlog adapterAllBlog;
    private SwipeRefreshLayout blogRefreshLayout;
    private ShimmerFrameLayout shimmerViewContainer;

    private static final int PAGE = 1;
    private boolean isLoadingAllBlog = false;
    private final boolean isLoadingSearchAllBlog = false;
    private final boolean isLastPageAllBlog = false;
    private static int TOTAL_PAGES_ALL_Blog;
    private int currentPage = PAGE;


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
        RecyclerView blogRecyclerView = v.findViewById(R.id.blogRecyclerView);
        blogRefreshLayout = v. findViewById(R.id.blogRefreshLayout);
        shimmerViewContainer = v.findViewById(R.id.shimmer_view_container);

        showLoadingIndicator(true);

        adapterAllBlog = new AdapterAllBlog(getContext());

        blogRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        blogRecyclerView.setLayoutManager(layoutManager);
        blogRecyclerView.setAdapter(adapterAllBlog);


        getAllBlogFirstPage();


        blogRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingAllBlog = true;
                currentPage +=1;
                getAllBlogNextPage();

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES_ALL_Blog;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageAllBlog;
            }

            @Override
            public boolean isLoading() {
                return isLoadingAllBlog;
            }
        });

        blogRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                currentPage = 1;
                isLoadingAllBlog = false;
                adapterAllBlog.getAllBlogResults().clear();
                adapterAllBlog.notifyDataSetChanged();
                getAllBlogFirstPage();
            }
        });

        return v;
    }

    private List<AllBlogResult> fetchResultsAllBlog(retrofit2.Response<AllBlogResponse> response) {
        AllBlogResponse allBlogResponse = response.body();
        assert allBlogResponse != null;
        return allBlogResponse.getResults();
    }

    private void getAllBlogFirstPage() {
        Call<AllBlogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllBlogPost(currentPage);
        call.enqueue(new Callback<AllBlogResponse>() {
            @Override
            public void onResponse(Call<AllBlogResponse> call, retrofit2.Response<AllBlogResponse> response) {
                if(response.isSuccessful()) {

                    List<AllBlogResult> results = fetchResultsAllBlog(response);
                    adapterAllBlog.addAll(results);
                    blogRefreshLayout.setRefreshing(false);
                    showLoadingIndicator(false);
                }

            }

            @Override
            public void onFailure(Call<AllBlogResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Unable To Load !!\n   Check Internet",Toast.LENGTH_SHORT).show();
                showLoadingIndicator(false);
                blogRefreshLayout.setRefreshing(false);
            }

        });

    }

    private void getAllBlogNextPage() {

        Call<AllBlogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllBlogPost(currentPage);
        call.enqueue(new Callback<AllBlogResponse>() {
            @Override
            public void onResponse(Call<AllBlogResponse> call, retrofit2.Response<AllBlogResponse> response) {
                if(response.isSuccessful()) {

                   isLoadingAllBlog = false;

                   List<AllBlogResult> results = fetchResultsAllBlog(response);
                   adapterAllBlog.addAll(results);
                }

            }

            @Override
            public void onFailure(Call<AllBlogResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Unable To Load !!\n   Check Internet",Toast.LENGTH_SHORT).show();
                showLoadingIndicator(false);
                blogRefreshLayout.setRefreshing(false);
            }

        });

    }

    public void showLoadingIndicator(boolean active) {
        if (active) {
            shimmerViewContainer.setVisibility(View.VISIBLE);
            shimmerViewContainer.startShimmerAnimation();
        } else {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    shimmerViewContainer.stopShimmerAnimation();
                    shimmerViewContainer.setVisibility(View.GONE);
                }
            }, 1000);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

}