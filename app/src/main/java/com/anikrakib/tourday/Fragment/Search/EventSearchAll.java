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
import com.anikrakib.tourday.Adapter.Search.AdapterAllEventSearch;
import com.anikrakib.tourday.Adapter.Search.AdapterAllUserSearch;
import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.Models.Event.AllEventResponse;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Profile.Profile;
import com.anikrakib.tourday.Models.SearchResponse;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventSearchAll extends Fragment {

    RecyclerView userSearchAllRecyclerView;
    LinearLayoutManager layoutManager;
    AdapterAllEventSearch adapterAllUserSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    SpinKitView spinKitView;
    TextView noMoreResult;

    private static final int LIMIT = 10;
    private static final int OFFSET = 0;
    private boolean isLoadingAllUser = false;
    private boolean isLastPageAllUser = false;
    private static int TOTAL_PAGES_ALL_User;
    private int currentOffset = OFFSET;
    String keyword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_search_all, container, false);

        userSearchAllRecyclerView = view.findViewById(R.id.searchAllUserRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.searchAllUserSwipeRefreshLayout);
        spinKitView = view.findViewById(R.id.spin_kit);
        noMoreResult = view.findViewById(R.id.noMoreResult);

        swipeRefreshLayout.setRefreshing(true);

        adapterAllUserSearch = new AdapterAllEventSearch(getContext());
        userSearchAllRecyclerView.setHasFixedSize(true);
//        adapterAllUserSearch.getAllProfileResults();
        layoutManager = new LinearLayoutManager(getContext());
        userSearchAllRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userSearchAllRecyclerView.setLayoutManager(layoutManager);
        userSearchAllRecyclerView.setAdapter(adapterAllUserSearch);
        keyword = SearchAllActivity.keyWordText;


        userSearchAllRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingAllUser = true;
                currentOffset += 10;
                spinKitView.setVisibility(View.VISIBLE);
                getAllUserNextPage();
                if(isLastPageAllUser) handlerNoMoreResult();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES_ALL_User;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageAllUser;
            }

            @Override
            public boolean isLoading() {
                return isLoadingAllUser;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                spinKitView.setVisibility(View.GONE);
                noMoreResult.setVisibility(View.GONE);
                currentOffset = 0;
                isLastPageAllUser = false;
                isLoadingAllUser = false;
                adapterAllUserSearch.getAllProfileResults().clear();
                adapterAllUserSearch.notifyDataSetChanged();
                getAllUSerFirstPage();

            }
        });

        getAllUSerFirstPage();


        return view;
    }

    private List<AllEventResult> fetchResultsAllUser(Response<AllEventResponse> response) {
        AllEventResponse searchResponse = response.body();
        assert searchResponse != null;
        return searchResponse.getResults();
    }

    private void getAllUSerFirstPage() {
        swipeRefreshLayout.setRefreshing(false);
        Call<AllEventResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchEvent(keyword,LIMIT,currentOffset);
        popular.enqueue(new Callback<AllEventResponse>() {
            @Override
            public void onResponse(Call<AllEventResponse> call, retrofit2.Response<AllEventResponse> response) {
                if (response.isSuccessful()) {
                    AllEventResponse searchResponse = response.body();
                    TOTAL_PAGES_ALL_User = searchResponse.getCount();

                    List<AllEventResult> profiles = fetchResultsAllUser(response);
                    adapterAllUserSearch.addAll(profiles);
                    swipeRefreshLayout.setRefreshing(false);
                    //showLoadingIndicator(false);


                    if (!(currentOffset <= TOTAL_PAGES_ALL_User)) isLastPageAllUser = true;

                    if(isLastPageAllUser) handlerNoMoreResult();

                }
            }
            @Override
            public void onFailure(Call<AllEventResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getAllUserNextPage() {
        Call<AllEventResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchEvent(keyword,LIMIT,currentOffset);
        popular.enqueue(new Callback<AllEventResponse>() {
            @Override
            public void onResponse(Call<AllEventResponse> call, retrofit2.Response<AllEventResponse> response) {
                if (response.isSuccessful()) {

                    isLoadingAllUser = false;

                    List<AllEventResult> profiles = fetchResultsAllUser(response);
                    adapterAllUserSearch.addAll(profiles);
                    swipeRefreshLayout.setRefreshing(false);
                    spinKitView.setVisibility(View.GONE);
                    noMoreResult.setVisibility(View.GONE);
                    //showLoadingIndicator(false);

                    if (!(currentOffset <= TOTAL_PAGES_ALL_User)) isLastPageAllUser = true;

                    if(isLastPageAllUser) handlerNoMoreResult();


                }
            }
            @Override
            public void onFailure(Call<AllEventResponse> call, Throwable t) {
                t.printStackTrace();
                //showErrorView(t);
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