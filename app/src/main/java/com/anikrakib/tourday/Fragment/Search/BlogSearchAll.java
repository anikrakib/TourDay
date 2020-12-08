package com.anikrakib.tourday.Fragment.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anikrakib.tourday.Activity.SearchAllActivity;
import com.anikrakib.tourday.Adapter.Search.AdapterAllBlogSearch;
import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogSearchAll extends Fragment {
    RecyclerView blogSearchAllRecyclerView;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    SpinKitView spinKitView;
    TextView noMoreResult;
    AdapterAllBlogSearch adapterSearchBlog;

    private static final int PAGE = 1;
    private static int BLOG_ITEM = 1;
    private boolean isLoadingAllBlog = false;
    private boolean isLastPageAllBlog = false;
    private static int TOTAL_ITEM_ALL_BLOG;
    private int currentPage = PAGE;
    String keyword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog_search_all, container, false);

        blogSearchAllRecyclerView = view.findViewById(R.id.searchAllBlogRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.searchAllBlogSwipeRefreshLayout);
        spinKitView = view.findViewById(R.id.spin_kit);
        noMoreResult = view.findViewById(R.id.noMoreResult);

        adapterSearchBlog = new AdapterAllBlogSearch(getContext());
        blogSearchAllRecyclerView.setHasFixedSize(true);
//        adapterAllUserSearch.getAllProfileResults();
        layoutManager = new LinearLayoutManager(getContext());
        blogSearchAllRecyclerView.setItemAnimator(new DefaultItemAnimator());
        blogSearchAllRecyclerView.setLayoutManager(layoutManager);
        blogSearchAllRecyclerView.setAdapter(adapterSearchBlog);
        keyword = SearchAllActivity.keyWordText;

        blogSearchAllRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingAllBlog = true;
                currentPage += 1;
                spinKitView.setVisibility(View.VISIBLE);
                if (BLOG_ITEM == TOTAL_ITEM_ALL_BLOG) handlerNoMoreResult();
                getAllUserNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_ITEM_ALL_BLOG;
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                spinKitView.setVisibility(View.GONE);
                noMoreResult.setVisibility(View.GONE);
                currentPage = 1;
                isLastPageAllBlog = false;
                isLoadingAllBlog = false;
                adapterSearchBlog.getAllProfileResults().clear();
                adapterSearchBlog.notifyDataSetChanged();
                getAllUSerFirstPage();

            }
        });

        getAllUSerFirstPage();


        return view;
    }

    private List<AllBlogResult> fetchResultsAllUser(Response<AllBlogResponse> response) {
        AllBlogResponse allBlogResponse = response.body();
        assert allBlogResponse != null;
        return allBlogResponse.getResults();
    }

    private void getAllUSerFirstPage() {
        swipeRefreshLayout.setRefreshing(false);
        Call<AllBlogResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchBlog(SearchAllActivity.keyWordText,currentPage);
        popular.enqueue(new Callback<AllBlogResponse>() {
            @Override
            public void onResponse(Call<AllBlogResponse> call, retrofit2.Response<AllBlogResponse> response) {
                if (response.isSuccessful()) {
                    AllBlogResponse allBlogResponse = response.body();
                    TOTAL_ITEM_ALL_BLOG = allBlogResponse.getCount();

                    List<AllBlogResult> allBlogResults = fetchResultsAllUser(response);
                    adapterSearchBlog.addAll(allBlogResults);
                    swipeRefreshLayout.setRefreshing(false);
                    BLOG_ITEM = adapterSearchBlog.getAllProfileResults().size();
                    //showLoadingIndicator(false);

                    if (BLOG_ITEM == TOTAL_ITEM_ALL_BLOG) handlerNoMoreResult();

                }
            }
            @Override
            public void onFailure(Call<AllBlogResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getAllUserNextPage() {
        swipeRefreshLayout.setRefreshing(false);
        Call<AllBlogResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchBlog(SearchAllActivity.keyWordText,currentPage);
        popular.enqueue(new Callback<AllBlogResponse>() {
            @Override
            public void onResponse(Call<AllBlogResponse> call, retrofit2.Response<AllBlogResponse> response) {
                if (response.isSuccessful()) {
                    isLoadingAllBlog = false;

                    List<AllBlogResult> profiles = fetchResultsAllUser(response);
                    adapterSearchBlog.addAll(profiles);
                    swipeRefreshLayout.setRefreshing(false);
                    spinKitView.setVisibility(View.GONE);
                    noMoreResult.setVisibility(View.GONE);
                    BLOG_ITEM = BLOG_ITEM + adapterSearchBlog.getAllProfileResults().size();

                    if (BLOG_ITEM == TOTAL_ITEM_ALL_BLOG) handlerNoMoreResult();

                }
            }
            @Override
            public void onFailure(Call<AllBlogResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void handlerNoMoreResult(){
        noMoreResult.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                noMoreResult.setVisibility(View.GONE);
            }
        },1000);
    }

}