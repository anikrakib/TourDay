package com.anikrakib.tourday.Fragment.Search;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import com.anikrakib.tourday.Adapter.Search.AdapterAllUserSearch;
import com.anikrakib.tourday.Models.Profile.Profile;
import com.anikrakib.tourday.Models.Shop.SearchResponse;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSearchAll extends Fragment {
    RecyclerView userSearchAllRecyclerView;
    LinearLayoutManager layoutManager;
    AdapterAllUserSearch adapterAllUserSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    SpinKitView spinKitView;
    TextView noMoreResult;
    CardView notFound;

    private static final int LIMIT = 10;
    private static final int OFFSET = 0;
    private boolean isLoadingAllUser = false;
    private boolean isLastPageAllUser = false;
    private static int TOTAL_PAGES_ALL_User;
    private int currentOffset = OFFSET;
    String keyword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_search_all, container, false);

        userSearchAllRecyclerView = view.findViewById(R.id.searchAllUserRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.searchAllUserSwipeRefreshLayout);
        spinKitView = view.findViewById(R.id.spin_kit);
        noMoreResult = view.findViewById(R.id.noMoreResult);
        notFound = view.findViewById(R.id.emptyCardView);

        swipeRefreshLayout.setRefreshing(true);

        adapterAllUserSearch = new AdapterAllUserSearch(getContext());
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

    private List<Profile> fetchResultsAllUser(Response<SearchResponse> response) {
        SearchResponse searchResponse = response.body();
        assert searchResponse != null;
        return searchResponse.getProfiles();
    }

    private void getAllUSerFirstPage() {
        swipeRefreshLayout.setRefreshing(false);
        Call<SearchResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchUser(keyword,LIMIT,currentOffset);
        popular.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, retrofit2.Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    SearchResponse searchResponse = response.body();
                    TOTAL_PAGES_ALL_User = searchResponse.getCount();

                    List<Profile> profiles = fetchResultsAllUser(response);
                    if(profiles.isEmpty()) notFound.setVisibility(View.VISIBLE);
                    else {
                        adapterAllUserSearch.addAll(profiles);
                        swipeRefreshLayout.setRefreshing(false);
                        //showLoadingIndicator(false);


                        if (!(currentOffset <= TOTAL_PAGES_ALL_User)) isLastPageAllUser = true;

                        if(isLastPageAllUser) handlerNoMoreResult();
                    }
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getAllUserNextPage() {
        Call<SearchResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchUser(keyword,LIMIT,currentOffset);
        popular.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, retrofit2.Response<SearchResponse> response) {
                if (response.isSuccessful()) {

                    isLoadingAllUser = false;

                    List<Profile> profiles = fetchResultsAllUser(response);
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
            public void onFailure(Call<SearchResponse> call, Throwable t) {
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