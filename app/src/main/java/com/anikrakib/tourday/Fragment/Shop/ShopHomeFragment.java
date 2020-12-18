package com.anikrakib.tourday.Fragment.Shop;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Shop.AdapterAllProduct;
import com.anikrakib.tourday.Fragment.Search.ProductSearchAll;
import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.Models.Shop.ProductResponse;
import com.anikrakib.tourday.Models.Shop.ProductResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopHomeFragment extends Fragment {

    RecyclerView allProductRecyclerView;
    AdapterAllProduct adapterAllProduct;
    GridLayoutManager gridLayoutManager;
    RelativeLayout progressBar;
    TextView totalProduct;

    private static final int PAGE = 1;
    private boolean isLoadingAllProduct = false;
    private boolean isLastPageAllProduct = false;
    private static int TOTAL_PAGES_ALL_Product;
    private int currentPage = PAGE;
    private int currentOffset = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_home, container, false);

        allProductRecyclerView = view.findViewById(R.id.allProductRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        totalProduct = view.findViewById(R.id.totalProductCount);

        adapterAllProduct = new AdapterAllProduct(getContext());
        allProductRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        allProductRecyclerView.setItemAnimator(new DefaultItemAnimator());
        allProductRecyclerView.setLayoutManager(gridLayoutManager);
        //allProductRecyclerView.setNestedScrollingEnabled(false);

        allProductRecyclerView.setAdapter(adapterAllProduct);



        allProductRecyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingAllProduct =true;
                currentPage+=1;
                currentOffset+=15;
                getAllProductNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES_ALL_Product;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageAllProduct;
            }

            @Override
            public boolean isLoading() {
                return isLoadingAllProduct;
            }
        });

        getAllProductFirstPage();
        return view;
    }

    private List<ProductResult> fetchResultsAllProduct(retrofit2.Response<ProductResponse> response) {
        ProductResponse allBlogResponse = response.body();
        assert allBlogResponse != null;
        return allBlogResponse.getProfiles();
    }

    private void getAllProductFirstPage() {
        Call<ProductResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllProduct(currentPage);
        call.enqueue(new Callback<ProductResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                if(response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    TOTAL_PAGES_ALL_Product = productResponse.getCount();

                    totalProduct.setText(productResponse.getCount()+" Products in our Shop");

                    List<ProductResult> results = fetchResultsAllProduct(response);
                    adapterAllProduct.addAll(results);

                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }

    private void getAllProductNextPage() {
        Call<ProductResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllProduct(currentPage);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                if(response.isSuccessful()) {
                    isLoadingAllProduct = false;

                    List<ProductResult> results = fetchResultsAllProduct(response);
                    adapterAllProduct.addAll(results);
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }
}