package com.anikrakib.tourday.Activity.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Shop.AdapterAllProduct;
import com.anikrakib.tourday.Fragment.Search.ProductSearchAll;
import com.anikrakib.tourday.Models.Shop.ProductResponse;
import com.anikrakib.tourday.Models.Shop.ProductResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductCategoryOrSearchActivity extends AppCompatActivity {
    ImageButton favouriteButton;
    TextView name;
    AdapterAllProduct adapterAllProduct;
    GridLayoutManager gridLayoutManager;
    RecyclerView allProductRecyclerView;
    RelativeLayout progressBar;
    SpinKitView spinKitView;
    TextView noMoreResult;
    CardView notFound;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final int LIMIT = 10;
    private static final int OFFSET = 0;
    private boolean isLoadingAllProduct = false;
    private boolean isLastPageAllUser = false;
    private static int TOTAL_PAGES_ALL_User;
    private int currentOffset = OFFSET;
    String keyword;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category_or_search);

        favouriteButton = findViewById(R.id.favouriteBlogIcon);
        name = findViewById(R.id.divisionNameTextView);
        //progressBar = findViewById(R.id.progressBar);
        allProductRecyclerView = findViewById(R.id.searchAllProductRecyclerView);
        spinKitView = findViewById(R.id.spin_kit);
        noMoreResult = findViewById(R.id.noMoreResult);
        notFound = findViewById(R.id.emptyCardView);
        swipeRefreshLayout = findViewById(R.id.searchAllProductSwipeRefreshLayout);

        adapterAllProduct = new AdapterAllProduct(getApplicationContext());
        allProductRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        allProductRecyclerView.setItemAnimator(new DefaultItemAnimator());
        allProductRecyclerView.setLayoutManager(gridLayoutManager);
        allProductRecyclerView.setAdapter(adapterAllProduct);

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        keyword = extras.getString("keyword");

        favouriteButton.setVisibility(View.GONE);
        if(keyword.equals("Headphone Turbine Cable Watch Power Bank")){
            name.setText("Gadget & Accessories");
        }else {
            name.setText(keyword);
        }


        allProductRecyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingAllProduct = true;
                currentOffset += 10;
                spinKitView.setVisibility(View.VISIBLE);
                getAllProductNextPage();
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
                return isLoadingAllProduct;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                spinKitView.setVisibility(View.GONE);
                noMoreResult.setVisibility(View.GONE);
                currentOffset = 0;
                isLastPageAllUser = false;
                isLoadingAllProduct = false;
                adapterAllProduct.getAllProductResults().clear();
                adapterAllProduct.notifyDataSetChanged();
                getAllProductFirstPage();

            }
        });

        getAllProductFirstPage();

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
                .getAllSearchProduct(keyword,LIMIT,currentOffset);
        call.enqueue(new Callback<ProductResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                if(response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    TOTAL_PAGES_ALL_User = productResponse.getCount();

                    List<ProductResult> results = fetchResultsAllProduct(response);
                    if(results.isEmpty()) notFound.setVisibility(View.VISIBLE);
                    else {
                        adapterAllProduct.addAll(results);
                        swipeRefreshLayout.setRefreshing(false);
                        //showLoadingIndicator(false);

                        if (!(currentOffset <= TOTAL_PAGES_ALL_User)) isLastPageAllUser = true;

                        if(isLastPageAllUser) handlerNoMoreResult();
                    }
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
                .getAllSearchProduct(keyword,LIMIT,currentOffset);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                if(response.isSuccessful()) {
                    isLoadingAllProduct = false;

                    List<ProductResult> results = fetchResultsAllProduct(response);
                    adapterAllProduct.addAll(results);
                    swipeRefreshLayout.setRefreshing(false);
                    spinKitView.setVisibility(View.GONE);
                    noMoreResult.setVisibility(View.GONE);
                    //progressBar.setVisibility(View.GONE);

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

    public void handlerNoMoreResult(){
        noMoreResult.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                noMoreResult.setVisibility(View.GONE);
            }
        },1000);
    }

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