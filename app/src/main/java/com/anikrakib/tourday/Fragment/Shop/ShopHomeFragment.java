package com.anikrakib.tourday.Fragment.Shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Activity.ExploreActivity;
import com.anikrakib.tourday.Activity.SearchAllActivity;
import com.anikrakib.tourday.Activity.Shop.ProductCategoryOrSearchActivity;
import com.anikrakib.tourday.Adapter.Shop.AdapterAllProduct;
import com.anikrakib.tourday.Adapter.Shop.BannerPagerAdapter;
import com.anikrakib.tourday.Fragment.Search.ProductSearchAll;
import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.Models.Shop.ProductResponse;
import com.anikrakib.tourday.Models.Shop.ProductResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.kishandonga.csbx.CustomSnackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopHomeFragment extends Fragment {

    RecyclerView allProductRecyclerView;
    AdapterAllProduct adapterAllProduct;
    GridLayoutManager gridLayoutManager;
    RelativeLayout progressBar;
    TextView totalProduct;
    EditText searchText;
    ImageButton searchButton;
    List<Integer> bannerList;
    ViewPager viewPager;
    CircleIndicator indicator;
    Timer timer;
    Handler handler;

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
        viewPager = view.findViewById(R.id.bannerViwPager);
        indicator = view.findViewById(R.id.indicator);
        searchText = view.findViewById(R.id.editText);
        searchButton= view.findViewById(R.id.searchButton);

        adapterAllProduct = new AdapterAllProduct(getContext());
        allProductRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        allProductRecyclerView.setItemAnimator(new DefaultItemAnimator());
        allProductRecyclerView.setLayoutManager(gridLayoutManager);
        //allProductRecyclerView.setNestedScrollingEnabled(false);
        allProductRecyclerView.setAdapter(adapterAllProduct);

        // banner pager and Adapter
        bannerList = new ArrayList<>();
        bannerList.add(R.drawable.banner_1);
        bannerList.add(R.drawable.banner_2);
        bannerList.add(R.drawable.banner_3);
        bannerList.add(R.drawable.banner_4);
        bannerList.add(R.drawable.banner_5);

        BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(getContext(),bannerList);
        viewPager.setAdapter(bannerPagerAdapter);
        indicator.setViewPager(viewPager);
        // optional
        bannerPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

        timer = new Timer();
        handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int i = viewPager.getCurrentItem();
                        if(i == bannerList.size() - 1){
                            i = 0;
                            viewPager.setCurrentItem(i,true);
                        }else {
                            i++;
                            viewPager.setCurrentItem(i,true);
                        }

                    }
                });
            }
        },4000,4000);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchText.getText().toString().isEmpty()){
                    startActivity(new Intent(getActivity(), ProductCategoryOrSearchActivity.class).putExtra("keyword",searchText.getText().toString()));
                }else {
                    snackBar("Write Something For Explore",R.color.whiteColor);
                }
            }
        });

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

    public void snackBar(String text,int color){
        CustomSnackbar sb = new CustomSnackbar(getActivity());
        sb.message(text);
        sb.padding(15);
        sb.textColorRes(color);
        sb.backgroundColorRes(R.color.colorPrimaryDark);
        sb.cornerRadius(15);
        sb.duration(Snackbar.LENGTH_LONG);
        sb.show();
    }

}