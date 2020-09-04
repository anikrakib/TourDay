package com.anikrakib.tourday.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anikrakib.tourday.Adapter.AdapterEvent;
import com.anikrakib.tourday.Adapter.AdapterYourEvent;
import com.anikrakib.tourday.Models.PostEvent;
import com.anikrakib.tourday.R;

import java.util.ArrayList;
import java.util.List;


public class YourEvent extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


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
        recyclerView = (RecyclerView)view. findViewById(R.id.yourEventRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setFocusable(false);

        //create dummy data to show in list
        List<PostEvent> list = new ArrayList();
        list.add(new PostEvent("QUOTES - 2", "Tis better to have loved and lost than never to have loved at all. St. Augustine", "22-01-2020", "30-02-2020","7:00 AM","8:00 AM","Laksam,Cumilla",R.drawable.event_pic4));
        list.add(new PostEvent("QUOTES - 3", "Not how long, but how well you have lived is the main thing. Seneca", "20-08-2020", "22-09-2020","8:00 AM","8:00 PM","Mirpur1, Dhaka",R.drawable.event_pic5));
        list.add(new PostEvent("QUOTES - 5", "In the end, it’s not the years in your life that count. It’s the life in your years. Abraham Lincoln", "20-08-2020", "22-09-2020","8:00 AM","8:00 PM","New Market, Dhaka",R.drawable.event_pic5));


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ///  add items to the adapter
        mAdapter = new AdapterYourEvent(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(mAdapter);

        return  view;
    }
}