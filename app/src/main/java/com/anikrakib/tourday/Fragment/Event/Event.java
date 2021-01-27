package com.anikrakib.tourday.Fragment.Event;

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
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Event.AdapterAllEvent;
import com.anikrakib.tourday.Models.Event.AllEventResponse;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  Event extends Fragment {
    private LinearLayoutManager layoutManager;
    private AdapterAllEvent adapterAllEvent;
    private SwipeRefreshLayout eventRefreshLayout;
    private ShimmerFrameLayout shimmerViewContainer;



    private static final int LIMIT = 10;
    private static final int OFFSET = 0;
    private boolean isLoadingAllEvent = false;
    private boolean isLastPageAllEvent = false;
    private static int TOTAL_PAGES_ALL_EVENT;
    private int currentOffset = OFFSET;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        /////*     initialize view   */////
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.eventRecyclerView);
        shimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        eventRefreshLayout = view. findViewById(R.id.allEventRefreshLayout);

        // start facebook shimmer animation
        showLoadingIndicator(true);


        adapterAllEvent = new AdapterAllEvent(getContext());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterAllEvent);


        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingAllEvent = true;
                currentOffset += 10;
                getAllEventNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES_ALL_EVENT;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageAllEvent;
            }

            @Override
            public boolean isLoading() {
                return isLoadingAllEvent;
            }
        });

        eventRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentOffset = 0;
                isLastPageAllEvent = false;
                isLoadingAllEvent = false;
                adapterAllEvent.getAllEventResults().clear();
                adapterAllEvent.notifyDataSetChanged();
                getAllEvent();
            }
        });

        getAllEvent();

        return  view;
    }

    private List<AllEventResult> fetchResultsAllEvent(Response<AllEventResponse> response) {
        AllEventResponse allEventResponse = response.body();
        assert allEventResponse != null;
        return allEventResponse.getResults();
    }

    private void getAllEvent() {
        Call<AllEventResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllEvent(LIMIT,currentOffset);
        popular.enqueue(new Callback<AllEventResponse>() {
            @Override
            public void onResponse(Call<AllEventResponse> call, retrofit2.Response<AllEventResponse> response) {
                if (response.isSuccessful()) {

                    List<AllEventResult> results = fetchResultsAllEvent(response);
                    adapterAllEvent.addAll(results);
                    eventRefreshLayout.setRefreshing(false);
                    showLoadingIndicator(false);

                }
            }
            @Override
            public void onFailure(Call<AllEventResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Unable To Load !!\n   Check Internet",Toast.LENGTH_SHORT).show();
                showLoadingIndicator(false);
                eventRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getAllEventNextPage() {
        Call<AllEventResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllEvent(LIMIT,currentOffset);
        popular.enqueue(new Callback<AllEventResponse>() {
            @Override
            public void onResponse(Call<AllEventResponse> call, retrofit2.Response<AllEventResponse> response) {
                if (response.isSuccessful()) {

                    isLoadingAllEvent = false;

                    List<AllEventResult> results = fetchResultsAllEvent(response);
                    adapterAllEvent.addAll(results);

                }
            }
            @Override
            public void onFailure(Call<AllEventResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Unable To Load !!\n   Check Internet",Toast.LENGTH_SHORT).show();
                showLoadingIndicator(false);
                eventRefreshLayout.setRefreshing(false);
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