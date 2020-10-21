package com.anikrakib.tourday.Fragment.Event;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.anikrakib.tourday.Adapter.Blog.AdapterBlog;
import com.anikrakib.tourday.Adapter.Event.AdapterYourEvent;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.Models.Event.PendingPayment;
import com.anikrakib.tourday.Models.Event.PostEvent;
import com.anikrakib.tourday.Models.Event.YourEventModel;
import com.anikrakib.tourday.Models.Profile.PostItem;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
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


public class YourEvent extends Fragment {
    private RecyclerView yourEventRecyclerView;
    private ArrayList<YourEventModel> yourEventModels;
    private SwipeRefreshLayout eventRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<PendingPayment> pendingPayment;


    public YourEvent() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_event, container, false);
        /////*     initialize view   */////
        yourEventRecyclerView = view. findViewById(R.id.yourEventRecyclerView);
        eventRefreshLayout = view. findViewById(R.id.yourEventRefreshLayout);

        yourEventModels = new ArrayList<>();

        yourEventRecyclerView.setFocusable(false);
        layoutManager = new LinearLayoutManager(getContext());

        yourEventRecyclerView.setHasFixedSize(true);
        yourEventRecyclerView.setLayoutManager(layoutManager);
        yourEventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                yourEventModels = new ArrayList<>();
                getAllYourEvent();
            }
        });

        getAllYourEvent();

        return  view;
    }


    private void getAllYourEvent() {
        SharedPreferences userPref = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        String userName = userPref.getString("userName","");
        String token = userPref.getString("token","");

        eventRefreshLayout.setRefreshing(true);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getYourEvent("Token "+token,userName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        String nextPage = jsonObject.getString("next");

                        result(jsonArray,nextPage);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"Fail!",Toast.LENGTH_LONG).show();

            }

        });

    }

    private void result(JSONArray jsonArray, String nextPage) {

        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject hit = jsonArray.getJSONObject(i);
                int eventId = hit.getInt("id");
                String eventTitle = hit.getString("title");
                String eventLocation = hit.getString("location");
                String eventDate = hit.getString("date");
                String eventDetails = hit.getString("details");
                String pay1 = hit.getString("pay1");
                String pay1Method = hit.getString("pay1_method");
                String pay2 = hit.getString("pay2");
                String pay2Method = hit.getString("pay2_method");
                int eventCapacity = hit.getInt("capacity");
                int eventCost = hit.getInt("cost");
                String eventImageUrl = hit.getString("image");
                int eventHostId = hit.getInt("host");
                JSONArray eventGoingArray = hit.getJSONArray("going");
                JSONArray eventPendingArray = hit.getJSONArray("pending");
                ArrayList<Integer> arrayList = new ArrayList<>();

                for (int j = 0; j < eventPendingArray.length(); j++) {
                    arrayList.add(eventPendingArray.getInt(j));
                }


                yourEventModels.add(new YourEventModel(eventId,eventTitle,eventLocation,eventDate,eventDetails,pay1,pay1Method,pay2,pay2Method,eventImageUrl,eventCapacity,eventCost,eventHostId,eventGoingArray.length(),eventPendingArray.length(),arrayList));

            }

            AdapterYourEvent adapterYourEvent = new AdapterYourEvent(getContext(), yourEventModels);
            yourEventRecyclerView.setAdapter(adapterYourEvent);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        eventRefreshLayout.setRefreshing(false);

    }
}