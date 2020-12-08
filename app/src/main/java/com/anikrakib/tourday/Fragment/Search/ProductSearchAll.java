package com.anikrakib.tourday.Fragment.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.SearchAllActivity;
import com.anikrakib.tourday.Adapter.Search.AdapterAllEventSearch;
import com.anikrakib.tourday.Adapter.Search.AdapterAllProductSearch;
import com.anikrakib.tourday.Models.Event.AllEventResponse;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.ProductResponse;
import com.anikrakib.tourday.Models.ProductResult;
import com.anikrakib.tourday.Models.SearchResponse;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductSearchAll extends Fragment {
    RecyclerView userSearchAllRecyclerView;
    GridLayoutManager gridLayoutManager;
    AdapterAllProductSearch adapterAllUserSearch;
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
        View view = inflater.inflate(R.layout.fragment_product_search_all, container, false);
        userSearchAllRecyclerView = view.findViewById(R.id.searchAllProductRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.searchAllProductSwipeRefreshLayout);
        spinKitView = view.findViewById(R.id.spin_kit);
        noMoreResult = view.findViewById(R.id.noMoreResult);

        swipeRefreshLayout.setRefreshing(true);

        adapterAllUserSearch = new AdapterAllProductSearch(getContext());
        userSearchAllRecyclerView.setHasFixedSize(true);
//        adapterAllUserSearch.getAllProfileResults();
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        userSearchAllRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userSearchAllRecyclerView.setLayoutManager(gridLayoutManager);
        userSearchAllRecyclerView.setAdapter(adapterAllUserSearch);
        keyword = SearchAllActivity.keyWordText;


        userSearchAllRecyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
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
                adapterAllUserSearch.getAllProductResults().clear();
                adapterAllUserSearch.notifyDataSetChanged();
                getAllProductFirstPage();

            }
        });

        getAllProductFirstPage();


        return view;
    }

    private List<ProductResult> fetchResultsAllUser(Response<ProductResponse> response) {
        ProductResponse productResponse = response.body();
        assert productResponse != null;
        return productResponse.getProfiles();
    }

    private void getAllProductFirstPage() {
        swipeRefreshLayout.setRefreshing(false);
        Call<ProductResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchProduct(keyword,LIMIT,currentOffset);
        popular.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    TOTAL_PAGES_ALL_User = productResponse.getCount();

                    List<ProductResult> productResults = fetchResultsAllUser(response);
                    adapterAllUserSearch.addAll(productResults);
                    swipeRefreshLayout.setRefreshing(false);
                    //showLoadingIndicator(false);


                    if (!(currentOffset <= TOTAL_PAGES_ALL_User)) isLastPageAllUser = true;

                    if(isLastPageAllUser) handlerNoMoreResult();

                }
            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getAllUserNextPage() {
        Call<ProductResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchProduct(keyword,LIMIT,currentOffset);
        popular.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                if (response.isSuccessful()) {

                    isLoadingAllUser = false;

                    List<ProductResult> productResults = fetchResultsAllUser(response);
                    adapterAllUserSearch.addAll(productResults);
                    swipeRefreshLayout.setRefreshing(false);
                    spinKitView.setVisibility(View.GONE);
                    noMoreResult.setVisibility(View.GONE);
                    //showLoadingIndicator(false);

                    if (!(ProductSearchAll.this.currentOffset <= TOTAL_PAGES_ALL_User)) isLastPageAllUser = true;

                    if(isLastPageAllUser) handlerNoMoreResult();


                }
            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
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